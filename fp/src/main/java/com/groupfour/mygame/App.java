package com.groupfour.mygame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.multiplayer.*;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Collisions.BulletZombieHandler;
import com.groupfour.Collisions.ZombiePlayerHandler;
import com.groupfour.Components.ObjectsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponent;
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
    private int playerCount;
    private Entity zombie;
    private boolean isServer;
    private PhysicsWorld physics;
    private boolean gameStarted=false;
    private Input gameInput;
    private ZombiePlayerHandler zombiePlayerHandler;
    private Connection<Bundle> connection;
    private boolean factoryInitialized = false;
    private Entity microwave;
    private Entity vmachine;
    PlayerComponent placeholder;

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
        },KeyCode.W);

        getInput().addAction(new UserAction("Move Down"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveY(true);
            }
        },KeyCode.S);

        getInput().addAction(new UserAction("Move Left"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveX(true);
            }
        },KeyCode.A);

        getInput().addAction(new UserAction("Move Right"){
            protected void onAction(){
                player.getComponent(PlayerComponent.class).moveX(false);
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
            }
            protected void onActionEnd() {
               player.getComponent(PlayerComponent.class).setShooting(false);
               player.getComponent(PlayerComponent.class).getCurrentWeapon().stopFiring();
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
        if (player.distance(vmachine) < 30) {
            vmachine.getComponent(VendingMachine.class).interact(); 
        } else if (player.distance(microwave) < 40) { 
            microwave.getComponent(Microwave.class).interact(); 
        }
    }
    
    @Override
    public void initGame() {


        
        getGameWorld().addEntityFactory(new SpawnFactory());
        getGameWorld().addEntityFactory(new ZombieFactory());
        getGameWorld().addEntityFactory(new ObjectsFactory());
        
        player = spawn("player");
        vmachine = spawn("vmachine");
        microwave = spawn("microwave");


        player.setPosition(50, 50);
        player.getComponent(PlayerComponent.class).setUpPlayer();
        zombiePlayerHandler = new ZombiePlayerHandler();
    }

    @Override
    public void initPhysics() {
    physics = getPhysicsWorld();
        if (isServer) {
            physics.addCollisionHandler(new BulletZombieHandler());
            physics.addCollisionHandler(new ZombiePlayerHandler());
            getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
            FXGL.run(() -> checkCollisions(), Duration.seconds(1));
        } else
            physics.addCollisionHandler(new BulletZombieHandler());
            physics.addCollisionHandler(new ZombiePlayerHandler());
            FXGL.run(() -> checkCollisions(), Duration.seconds(1));
    }
        
     @Override
     protected void initUI() {
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startMultiplayer)), Duration.seconds(.01));
        var ui = new MainUI();
        addUINode(ui, 30, 50);
    }
    
    public void startGame1P() {
        getSceneService().popSubScene();
        gameStarted=true;
        FXGL.run(() -> {
            zombie = spawn("zombie", player.getCenter().getX() + 20, player.getCenter().getY() + 20);
            zombie.getViewComponent();
            zombie.getComponent(ZombieComponent.class).findClosestPlayer();
        }, Duration.seconds(1));

        FXGL.run(() -> updateFollower(), Duration.seconds(1));
        FXGL.run(()->{
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
    }

    public void startMultiplayer() {
        getDialogService().showConfirmationBox("Are you the host?", answer -> {
            MultiplayerStart multiplayerStart = new MultiplayerStart();
            isServer = answer;

            //If host
            if (answer) {
                players.add(player);
                var server = getNetService().newTCPServer(55555);
                server.startAsync();
                waitingForPlayers();

                //When someone connects
                server.setOnConnected(conn -> {
                    System.out.println("please work");
                    connection = conn;

                    //first one will pop the loading screen and display a scene with a start button
                    if(playerCount==1){
                        getExecutor().startAsyncFX(() -> {
                        getSceneService().popSubScene();
                        FXGL.getSceneService().pushSubScene(multiplayerStart);
                        multiplayerStart.addPlayer();
                        });
                    }
                    else{
                    multiplayerStart.addPlayer();
                    }
                    playerCount++;
                });
            } 

            //If Client
            else {
                players.add(player);
                var client = getNetService().newTCPClient("localhost", 55555);
                client.setOnConnected(conn -> {
                    connection = conn;
                    getExecutor().startAsyncFX(() -> {
                        onClient();
                        getSceneService().popSubScene();
                    });
                });
                client.connectAsync();
            }
        });
    }

    private void waitingForPlayers() {
        LoadingScreen loadingScreen = new LoadingScreen("Waiting for players...");
        FXGL.getSceneService().pushSubScene(loadingScreen);
    }

    private void onServer() {
        // player1 = spawn("player");
        for (Entity player :players){
        getService(MultiplayerService.class).spawn(connection, player, "player");

        }

        // getService(MultiplayerService.class).spawn(connection, player2, "player");
        
        FXGL.run(() -> {
            zombie = spawn("zombie", players.get(0).getCenter().getX() + 5, players.get(0).getCenter().getY() + 5);
            getService(MultiplayerService.class).spawn(connection, zombie, "zombie");
        }, Duration.seconds(1));

        // getService(MultiplayerService.class).addInputReplicationReceiver(connection, gameInput);
        // FXGL.run(() -> updateFollower(), Duration.seconds(1));
    }

    private void onClient() {
        getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
        getService(MultiplayerService.class).addInputReplicationSender(connection, getInput());
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
            gameInput.update(tpf);
        }
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
