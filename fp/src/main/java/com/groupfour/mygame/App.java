package com.groupfour.mygame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.multiplayer.*;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Collisions.BulletZombieHandler;
import com.groupfour.Collisions.ZombiePlayerHandler;
import com.groupfour.Components.BoundsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponents.PlayerAnimComp;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.Factories.ObjectsFactory;
import com.groupfour.Factories.SpawnFactory;
import com.groupfour.Factories.ZombieFactory;
import com.groupfour.Objects.Microwave;
import com.groupfour.Objects.VendingMachine;
import com.groupfour.UI.LoadingScreen;
import com.groupfour.UI.MainUI;
import com.groupfour.UI.MultiplayerStart;
import com.groupfour.UI.PlayerCountMenu;
import com.groupfour.mygame.EntityTypes.EntityType;


import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.app.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class App extends GameApplication {
    
    private List<Entity> players= new ArrayList<>();
    private Entity player;
    private Entity zombie;
    private boolean isServer;
    private PhysicsWorld physics;
    private boolean gameStarted=false;
    private ZombiePlayerHandler zombiePlayerHandler;
    private Connection<Bundle> connection;
    private Entity microwave;
    private Entity vmachine;
    private MainUI ui;
    private int wave;
    private Entity newPlayer;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setTitle("Flatline: Oregon");
        settings.setVersion("Alpha 0.3");
        settings.addEngineService(MultiplayerService.class);
        settings.setDeveloperMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);

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
    protected void initInput(){
        player = new Entity();

        getInput().addAction(new UserAction("Move Upwards"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveY(false);
            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMoving();
            }
        },KeyCode.W);

        getInput().addAction(new UserAction("Move Down"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveY(true);
            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMoving();
            }
        },KeyCode.S);

        getInput().addAction(new UserAction("Move Left"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveX(true);   
            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMoving();
            }
        },KeyCode.A);

        getInput().addAction(new UserAction("Move Right"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveX(false);
            }
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMoving();
            }
        },KeyCode.D);

        getInput().addAction(new UserAction("Reload"){
            protected void onActionBegin(){
                player.getComponent(PlayerComponent.class).getCurrentWeapon().reload();
            }
        },KeyCode.R);

        getInput().addAction(new UserAction("Shoot") {
            protected void onActionBegin() {
               player.getComponent(PlayerComponent.class).getCurrentWeapon().fire(player);
               player.getComponent(PlayerComponent.class).setShooting(true);
               player.getComponent(PlayerAnimComp.class).setIsShooting(true);
            }
            protected void onActionEnd() {
               player.getComponent(PlayerComponent.class).setShooting(false);
               player.getComponent(PlayerComponent.class).getCurrentWeapon().stopFiring();
               player.getComponent(PlayerAnimComp.class).setIsShooting(false);
            }
        }, MouseButton.PRIMARY);
        getInput().addAction(new UserAction("Switch Weapons") {
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).switchWeapon();
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
        }
    }
    
    @Override
    public void initGame() {
        
        getGameWorld().addEntityFactory(new SpawnFactory());
        getGameWorld().addEntityFactory(new ZombieFactory());
        getGameWorld().addEntityFactory(new ObjectsFactory());

        setLevelFromMap("Lobby.tmx");
    }

    @Override
     protected void initUI() {
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startMultiplayer)), Duration.seconds(.01));
        ui = new MainUI();
        addUINode(ui, 30, 50);
    }
    
    @Override
    public void initPhysics() {
        physics = getPhysicsWorld();
            if (isServer) {
                physics.addCollisionHandler(new BulletZombieHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
                FXGL.run(() -> checkCollisions(), Duration.seconds(1));
            } else {
                physics.addCollisionHandler(new BulletZombieHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                FXGL.run(() -> checkCollisions(), Duration.seconds(1));
            }
            FXGL.run(() -> BoundsComponent.ObjectEntityCollision(player, zombie), Duration.seconds(0.0017));
    }
    
    public void startGame1P() {
        player = spawn("player");
        vmachine = spawn("vmachine");
        microwave = spawn("microwave");
        wave=0;
        double waveMultiplier=1.5;
        player.getComponent(PlayerComponent.class).setUpPlayer();
        zombiePlayerHandler = new ZombiePlayerHandler();

        gameStarted = true;
        getSceneService().popSubScene();

        FXGL.run(()->{
            if(getGameWorld().getEntitiesByType(EntityType.ZOMBIE).isEmpty()){
                if(wave!=0){
                    System.out.println(wave+" Clear");
                }
                wave++;
                nextWave(wave,waveMultiplier);
            }

            if(player.getComponent(PlayerComponent.class).isDead()){
                player.getComponent(PlayerComponent.class).setDeath(false);
                getDialogService().showMessageBox("You Died! Back to Main Menu?", () -> {
                getGameController().gotoMainMenu();
                FXGL.run(() -> {
                    resetGameWorld();
                }, Duration.seconds(5));
                });
            }
        },Duration.seconds(0.1));

        FXGL.run(() -> updateFollower(), Duration.seconds(1));
    }

    private void nextWave(int wave, double waveMultiplier){
        for(int i=0;i<wave*waveMultiplier;i++){
            System.out.println("zombie spawn");
            zombie = spawn("zombie", player.getCenter().getX() + 20, player.getCenter().getY() + 20);
            zombie.getViewComponent();
            zombie.getComponent(ZombieComponent.class).findClosestPlayer();
            // zombie = spawn("spitter");
            // zombie.getViewComponent(); 
            // zombie.getComponent(ZombieComponent.class).findClosestPlayer();
        }
    }

    public void startMultiplayer() {
        zombiePlayerHandler = new ZombiePlayerHandler();
        getDialogService().showConfirmationBox("Are you the host?", answer -> {
            player = spawn("player");
            player.getComponent(PlayerComponent.class).setUpPlayer();
            MultiplayerStart multiplayerStart = new MultiplayerStart();
            isServer = answer;

            if (isServer) {
                players.add(player);
                var server = getNetService().newTCPServer(55555);
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
                                gameStarted=true;
                                connection.send(new Bundle("gameStart"));
                            });
                        }
                        multiplayerStart.addPlayer();
                        newPlayer = spawn("player");
                        players.add(newPlayer);
                        getService(MultiplayerService.class).spawn(connection, newPlayer, "player");
                        newPlayer.getComponent(PlayerComponent.class).initClientInput();
                        getService(MultiplayerService.class).addInputReplicationReceiver(connection, newPlayer.getComponent(PlayerComponent.class).getClientInput());
                    });
                });       
            } 
            //If Client WIP
            else {
                var client = getNetService().newTCPClient("localhost", 55555);
                client.setOnConnected(conn -> {
                    connection = conn;
                    // connection.addMessageHandlerFX((c, message) -> {
                    //     if (message.getName().equals("gameStart")) {
                    //         getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
                    //         getSceneService().popSubScene();
                    //     }
                    // });
                    getExecutor().startAsyncFX(() -> {
                        onClient();
                    });
                });
                client.connectAsync();
            }
        });
    }

    //DONE
    private void waitingForPlayers() {
        LoadingScreen loadingScreen = new LoadingScreen("Waiting for players...");
        FXGL.getSceneService().pushSubScene(loadingScreen);
    }

    private void onServer() {
        getService(MultiplayerService.class).spawn(connection, player, "player");

        FXGL.run(() -> {
            zombie = spawn("zombie", player.getCenter().getX() + 20, player.getCenter().getY() + 20);
            getService(MultiplayerService.class).spawn(connection, zombie, "zombie");
            zombie.getViewComponent();
            zombie.getComponent(ZombieComponent.class).findClosestPlayer();
            updateFollower();
        }, Duration.seconds(1));

        // FXGL.run(() -> updateFollower(), Duration.seconds(1));
        
        getSceneService().popSubScene();
        getSceneService().popSubScene();
    }

    private void onClient() {
        player = spawn("player");
        player.getComponent(PlayerComponent.class).setUpPlayer();
        getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
        getService(MultiplayerService.class).addInputReplicationSender(connection, getInput());
        getSceneService().popSubScene();
    }
    
    private void updateFollower() {
        if (zombie.hasComponent(ZombieComponent.class)) {
            zombie.getComponent(ZombieComponent.class).onUpdate(0);
        } else {
            System.out.println("No more Zombies Left ");
        }
    }

     public void resetGameWorld() {
        getGameWorld().getEntities().forEach(entity -> entity.removeFromWorld());
        zombie = null;
        gameStarted = false;
        getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startMultiplayer));
    }

    @Override
    protected void onUpdate(double tpf) {
        if(!gameStarted){
            return;
        }
        if (isServer) {

            for(int i=1;i<players.size();i++){
                players.get(i).getComponent(PlayerComponent.class).getClientInput().update(tpf);
            }
        }
        ui.updateGold(player.getComponent(PlayerComponent.class).getCurrency());
        ui.updateHealthBar(player.getComponent(PlayerComponent.class).getHealth());
        ui.updateGunUI(
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getAmmo(), 
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getAmmoCount(),
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getName()
            );
        player.getComponent(PlayerAnimComp.class).setWeaponType(player.getComponent(PlayerComponent.class).getCurrentWeapon().getName());
        player.getComponent(PlayerComponent.class).getCurrentWeapon().setPlayerRotation(player.getRotation());
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
