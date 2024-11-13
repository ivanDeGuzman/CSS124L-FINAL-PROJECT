package com.groupfour.mygame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.multiplayer.*;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.Server;
import com.groupfour.Collisions.BulletZombieHandler;
import com.groupfour.Collisions.ZombiePlayerHandler;
import com.groupfour.Components.BoundsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponents.PlayerAnimComp;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.Factories.ObjectsFactory;
import com.groupfour.Factories.SpawnFactory;
import com.groupfour.Factories.ZombieFactory;
import com.groupfour.Objects.Armory;
import com.groupfour.Objects.Microwave;
import com.groupfour.Objects.VendingMachine;
import com.groupfour.UI.LoadingScreen;
import com.groupfour.UI.MainUI;
import com.groupfour.UI.MultiplayerStart;
import com.groupfour.UI.ObjectsUI;
import com.groupfour.UI.PCM_BG;
import com.groupfour.UI.PlayerCountMenu;
import com.groupfour.mygame.EntityTypes.EntityType;


import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.app.MenuItem;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
public class App extends GameApplication {
    private List<Entity> players= new ArrayList<>();
    private Entity player, zombie;
    private int initPlayerID = 1;
    private boolean isServer;
    private PhysicsWorld physics;
    private ZombiePlayerHandler zombiePlayerHandler;
    private Connection<Bundle> connection;
    private Entity microwave;
    private Entity vmachine, armory;
    private MainUI ui;
    private int wave;
    private Entity newPlayer;
    private PlayerComponent playerComponent;
    private double waveMultiplier=10.5;
    private boolean waveCooldown = false;
    private boolean isWaveSpawning;
    private MultiplayerStart multiplayerStart;
    private boolean isServerStarted = false, isNearInteractable = false;
    private ObjectsUI objectsUI;
    private Server<Bundle> server;
    private Client<Bundle> client;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setTitle("Flatline: Oregon");
        settings.setVersion("Beta ?");
        settings.addEngineService(MultiplayerService.class);
        settings.setDeveloperMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSoundMenuPress("titleSelect.mp3");
        //implement later
        // settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        // settings.getCredits().addAll(Arrays.asList(
        //         "PutCreditsHere- PlaceHolderName"
        // ));

        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new PlayerCountMenu(
                    App.this::startGame1P,
                    App.this::startMultiplayer 
                );
            }
        });
    }

    @Override
    protected void onPreInit() {
        Music bgm = FXGL.getAssetLoader().loadMusic("titleBGM.mp3");
        getAudioPlayer().loopMusic(bgm);
    }
    
    @Override
    protected void initInput(){
        player = new Entity();
        zombie = new Entity();

        getInput().addAction(new UserAction("Move Upwards"){
            protected void onAction(){
                playerComponent.moveY(false);
            }
            protected void onActionEnd() {
                playerComponent.stopMoving();
            }
        },KeyCode.W);

        getInput().addAction(new UserAction("Move Down"){
            protected void onAction(){
                playerComponent.moveY(true);
            }
            protected void onActionEnd() {
                playerComponent.stopMoving();
            }
        },KeyCode.S);

        getInput().addAction(new UserAction("Move Left"){
            protected void onAction(){
                playerComponent.moveX(true);   
            }
            protected void onActionEnd() {
                playerComponent.stopMoving();
            }
        },KeyCode.A);

        getInput().addAction(new UserAction("Move Right"){
            protected void onAction(){
                playerComponent.moveX(false);
            }
            protected void onActionEnd() {
                playerComponent.stopMoving();
            }
        },KeyCode.D);

        getInput().addAction(new UserAction("Reload"){
            protected void onActionBegin(){
                playerComponent.getCurrentWeapon().reload();
            }
        },KeyCode.R);

        getInput().addAction(new UserAction("Shoot") {
            protected void onActionBegin() {
            playerComponent.getCurrentWeapon().fire(player);
            }
            protected void onActionEnd() {
            playerComponent.getCurrentWeapon().stopFiring();
            }
        }, MouseButton.PRIMARY);
        
        getInput().addAction(new UserAction("Switch Weapons") {
            protected void onActionBegin() {
                playerComponent.switchWeapon();
            }
        }, KeyCode.Q);
        getInput().addAction(new UserAction("Interact") {
            protected void onActionBegin() {
                interactWithObject();
            }
        }, KeyCode.F);

    }

    private void interactWithObject() { 
        if (player.distance(vmachine) < 70) {
            vmachine.getComponent(VendingMachine.class).interact();
        } else if (player.distance(microwave) < 70) { 
            microwave.getComponent(Microwave.class).interact();
        } else if (player.distance(armory) < 70) {
            if (waveCooldown)
                armory.getComponent(Armory.class).interact();
            else System.out.println("Only accessible after wave"); //placeholder, should be a UI
        }
    }

    @Override
    public void initGame() {
        //getAudioPlayer().stopAllMusic();
        getGameWorld().addEntityFactory(new SpawnFactory());
        getGameWorld().addEntityFactory(new ZombieFactory());
        getGameWorld().addEntityFactory(new ObjectsFactory());
        zombiePlayerHandler = new ZombiePlayerHandler();
        setLevelFromMap("Lobby.tmx");
    }

    @Override
    protected void initUI() {
        FXGL.runOnce(() -> {
            FXGL.getSceneService().pushSubScene(new PCM_BG());
            FXGL.getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startMultiplayer));
        }, Duration.seconds(0.01));
        ui = new MainUI();
        objectsUI = new ObjectsUI();
        addUINode(ui);
        addUINode(objectsUI);
    }

    @Override
    public void initPhysics() {
        physics = getPhysicsWorld();
            if (isServer) {
                physics.addCollisionHandler(new BulletZombieHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                
                FXGL.run(() -> checkCollisions(), Duration.seconds(1));
            } else {
                physics.addCollisionHandler(new BulletZombieHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                FXGL.run(() -> checkCollisions(), Duration.seconds(1));
            }
            FXGL.run(() -> {
                if(player.isActive()){
                    BoundsComponent.ObjectEntityCollision(player);
                }}, Duration.seconds(0.017));
    }

    public void startGame1P() {
        player = spawn("player", new Point2D(getAppWidth() / 2, getAppHeight() / 2));
        vmachine = spawn("vmachine");
        microwave = spawn("microwave");
        armory = spawn("armory");
        wave=0;

        waveAndDeathManager();
        
        playerComponent = player.getComponent(PlayerComponent.class);
        setUpPlayer();

        getSceneService().popSubScene();
        getSceneService().popSubScene();

    }

    private void waveAndDeathManager() {
        FXGL.run(() -> {
            if (getGameWorld().getEntitiesByType(EntityType.ZOMBIE).isEmpty() && !isWaveSpawning) {
                if (wave != 0) {
                    waveCooldown = true;
                    isWaveSpawning = true;
                    System.out.println(waveCooldown);
                    if (isServer)
                        sendWaveToClient();
                    runOnce(() -> {
                        wave++;
                        nextWave(wave, waveMultiplier);
                        waveCooldown = false;
                        isWaveSpawning = false;


                    }, Duration.seconds(20));
                } else {
                    wave++;
                    nextWave(wave, waveMultiplier);
                    if (isServer)
                        sendWaveToClient();
                }
            }

                if(playerComponent.isDead()){
                    playerComponent.setDeath(false);
                    getDialogService().showMessageBox("You Died! Back to Main Menu?", () -> {
                    ui.playTitleMusic();
                    getGameController().gotoMainMenu();
                    FXGL.runOnce(() -> {
                        resetGameWorld();
                    }, Duration.seconds(1));
                    });

                    if (isServer) {
                        sendDeathToClient();
                    }
                }
            },Duration.seconds(0.1));
    }

    private void sendWaveToClient() {
        Bundle bundle = new Bundle("waveUpdate");
        bundle.put("wave", wave);
        bundle.put("waveMultiplier", waveMultiplier);
        System.out.println("Sending wave update to client: wave=" + wave + ", waveMultiplier=" + waveMultiplier);
        server.broadcast(bundle);
    }
    
    private void sendDeathToClient() {
        if (playerComponent.getEntity().getComponent(IDComponent.class) != null) {
            Bundle bundle = new Bundle("PlayerDeath");
            bundle.put("playerID", playerComponent.getEntity().getComponent(IDComponent.class).getId());
            server.broadcast(bundle);
        }
    }
    

    private void nextWave(int wave, double waveMultiplier){
        int totalZombies = (int)(wave * waveMultiplier);
        Duration interval = Duration.seconds(1);
        for(int i = 0; i < totalZombies ; i++) {
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                System.out.println("zombie spawn");
                spawn("zombie");
            }, delay);
            // zombie = spawn("spitter");
            // zombie.getViewComponent(); 
            // zombie.getComponent(ZombieComponent.class).findClosestPlayer();
        }
    }

    public void startMultiplayer() {
        getDialogService().showConfirmationBox("Are you the host?", answer -> {
            player = spawn("player");
            player.addComponent(new IDComponent("playerID", initPlayerID));
            vmachine = spawn("vmachine");
            microwave = spawn("microwave");
            armory = spawn("armory");
            playerComponent = player.getComponent(PlayerComponent.class);
            multiplayerStart = new MultiplayerStart();
            isServer = answer;

            if (isServer) {
                players.add(player);
                server = getNetService().newTCPServer(55555);
                waitingForPlayers();
                server.startAsync();
                server.setOnConnected(conn -> {
                    connection = conn;
                    getExecutor().startAsyncFX(() -> {
                        if(players.size()==1){
                            getSceneService().popSubScene();
                            FXGL.getSceneService().pushSubScene(multiplayerStart);
                            multiplayerStart.setOnStartClick(e-> {
                                onServer();
                            });
                        }
                        multiplayerStart.addPlayer();
                        newPlayer = spawn("player");
                        newPlayer.addComponent(new IDComponent("playerID", players.size() + 1));
                        players.add(newPlayer);
                        // getService(MultiplayerService.class).spawn(connection, newPlayer, "player");
                        // newPlayer.getComponent(PlayerComponent.class).initClientInput();
                        // getService(MultiplayerService.class).addInputReplicationReceiver(connection, newPlayer.getComponent(PlayerComponent.class).getClientInput());
                    });
                });       
            } 
            //If Client WIP
            else {
                client = getNetService().newTCPClient("localhost", 55555);
                client.setOnConnected(conn -> {
                    onClient(conn);
                });
                client.connectAsync();
            }
        });
    }

    //moved it here so each player's cam is individualized instead of being cast to entity
    private void setUpPlayer() {
        Viewport viewport = getGameScene().getViewport();
        viewport.setLazy(true);
        viewport.bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);
        viewport.setZoom(1.5);
    }

    private void waitingForPlayers() {
        LoadingScreen loadingScreen = new LoadingScreen("Waiting for players...");
        FXGL.getSceneService().pushSubScene(loadingScreen);
    }

    private void onServer() {
        isServerStarted = true;
        players.forEach(player -> {
            connection.send(new Bundle("serverStarted"));
            getService(MultiplayerService.class).spawn(connection, player, "player");
            player.getComponent(PlayerComponent.class).initClientInput();
            getService(MultiplayerService.class).addInputReplicationReceiver(connection, player.getComponent(PlayerComponent.class).getClientInput());
            setUpPlayer();
            
        });

        wave = 0;
        waveAndDeathManager();
        
        List<Entity> zombies = getGameWorld().getEntitiesByType(EntityType.ZOMBIE); 
        zombies.forEach(zombie -> getService(MultiplayerService.class).spawn(connection, zombie, "zombie"));
        
        getSceneService().popSubScene();
        getSceneService().popSubScene();
        getSceneService().popSubScene();
    }

    private void onClient(Connection<Bundle> connection) {

        connection.addMessageHandlerFX((conn, message) -> { 
            Platform.runLater(() -> {
                if (message.getName().equals("serverStarted")) { 
                    isServerStarted = true; 
                    ui.removeWaitingUI(); 
                } else if (message.getName().equals("PlayerDeath")) { 
                    int playerId = message.get("playerID"); 
                    handlePlayerDeath(playerId); 
                }
            });
        });
        
        Platform.runLater(() -> {
            if (!isServerStarted) {
                ui.waitServerStart();
            } else {
                ui.removeWaitingUI();
            }
    
            player = spawn("player");
            setUpPlayer();
            getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
            
            
            getSceneService().popSubScene();
            getSceneService().popSubScene();
        });
    }
    

    private void handlePlayerDeath(int playerId) {
        getGameWorld().getEntitiesByComponent(IDComponent.class).forEach(entity -> {
            IDComponent idComponent = entity.getComponent(IDComponent.class);
            if (idComponent.getId() == playerId) {
                entity.getComponent(PlayerComponent.class).setDeath(true);
            }
        });
    }

    private void clientWaveUpd(int wave, double waveMultiplier) {
        this.wave = wave;
        this.waveMultiplier = waveMultiplier;
        System.out.println(" SERVER WAVE SYNC TEST ");
    }



    public void resetGameWorld() {
        getGameWorld().getEntities().forEach(entity -> entity.removeFromWorld());
        zombie = null;
        getSceneService().pushSubScene(new PCM_BG());
        getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startMultiplayer));
    }

    @Override
    protected void onUpdate(double tpf) {
        if(!player.isActive()){
            return;
        }

        if (player.distance(vmachine) < 70) {
            isNearInteractable = true;
        } else if (player.distance(microwave) < 70) { 
            isNearInteractable = true;
        } else if (player.distance(armory) < 70) {
            if (waveCooldown)
                isNearInteractable = true;
        } else {
            isNearInteractable = false;
        }

        if (isNearInteractable) { 
            if (objectsUI.canInteractNode == null) { 
                objectsUI.showCanInteract(); 
                
            } 
        } else { 
            objectsUI.hideCanInteract();
        }

        if (isServer) {
            for(int i=1;i<players.size();i++){
                players.get(i).getComponent(PlayerComponent.class).getClientInput().update(tpf);
            }
        }
        //ui.setupMinimap(getGameWorld());
        ui.updateGold(playerComponent.getCurrency());
        ui.updateHealthBar(playerComponent.getHealth());
        ui.updateGunUI(
            playerComponent.getCurrentWeapon().getAmmo(), 
            playerComponent.getCurrentWeapon().getAmmoCount(),
            playerComponent.getCurrentWeapon().getName()
        );
        
        ui.updateWave(wave, waveCooldown);
        player.getComponent(PlayerAnimComp.class).setWeaponType(playerComponent.getCurrentWeapon().getName());
        playerComponent.getCurrentWeapon().setPlayerRotation(player.getRotation());
        
    }

    private void checkCollisions() {
        getGameWorld().getEntitiesByType(EntityType.ZOMBIE).forEach(zombie -> {
            getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                if (zombie.isColliding(player)) {
                    zombiePlayerHandler.handleCollision(zombie, player);
                }
            });
        });    
    }


    public static void main(String[] args) {
        launch(args);
    }
}
