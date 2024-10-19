package com.groupfour.mygame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.multiplayer.*;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.ZombieComponent;
import com.groupfour.Components.EntityTypes.EntityType;

import java.util.Arrays;
import java.util.EnumSet;
import com.almasb.fxgl.app.MenuItem;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;



public class App extends GameApplication {
    private Entity player1, player2, zombie;
    private boolean isServer;
    private boolean isShootingP1 = false;
    private boolean isShootingP2 = false;
    private double timeSinceLastShotP1 = 0;
    private double timeSinceLastShotP2 = 0;
    private double shootInterval = 0.2; 

    private Input gameInput;
    private Connection<Bundle> connection;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Flatline; Miami");
        settings.setVersion("Alpha 0.3");
        settings.addEngineService(MultiplayerService.class);
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
                    App.this::startGame2P 
                );
            }
        });
}
    @Override
    protected void initInput() {
        if (!isServer) {
            onKey(KeyCode.W, () -> player1.translateY(-2));
            onKey(KeyCode.S, () -> player1.translateY(2));
            onKey(KeyCode.A, () -> player1.translateX(-2));
            onKey(KeyCode.D, () -> player1.translateX(2));
    
            getInput().addAction(new UserAction("Start Shooting for Player 1") {
                @Override
                protected void onActionBegin() {
                    isShootingP1 = true; 
                }
                @Override
                protected void onActionEnd() {
                    isShootingP1 = false; 
                }
            }, MouseButton.PRIMARY);
        }
    
        gameInput = new Input();
        
        onKeyBuilder(gameInput, KeyCode.W)
                .onAction(() -> player2.translateY(-2));
        onKeyBuilder(gameInput, KeyCode.S)
                .onAction(() -> player2.translateY(2));
        onKeyBuilder(gameInput, KeyCode.A)
                .onAction(() -> player2.translateX(-2));
        onKeyBuilder(gameInput, KeyCode.D)
                .onAction(() -> player2.translateX(2));
        
        gameInput.addAction(new UserAction("Start Shooting for Player 2") {
            @Override
            protected void onActionBegin() {
                isShootingP2 = true; 
            }
            @Override
            protected void onActionEnd() {
                isShootingP2 = false; 
            }
        }, MouseButton.PRIMARY);
    }
    
    @Override
    public void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.ZOMBIE) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity zombie) {
                bullet.removeFromWorld(); 
                if (isServer) {
                    zombie.removeFromWorld();
                    getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
                } else {
                    zombie.removeFromWorld();
                }
            }
        });
    }
        public void shoot(Point2D shootPoint, Entity player) {
        Point2D position = player.getCenter();
        Point2D vectorToMouse = shootPoint.subtract(position).normalize();

        spawnBullet(position, vectorToMouse);
    }

    private void spawnBullet(Point2D position, Point2D direction) {
        var data = new SpawnData(position.getX(), position.getY())
                .put("direction", direction);

        Entity bullet = spawn("bullet", data);
        bullet.getComponent(BulletComponent.class).setDirection(direction);

        if (isServer) {
            getService(MultiplayerService.class).spawn(connection, bullet, "bullet");
        }
    }



     @Override
     protected void initGame() {

        getPhysicsWorld().addCollisionHandler(new CollisionHandler("bullet", "zombie") {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity zombie) {
                bullet.removeFromWorld();
                zombie.removeFromWorld();
    
                if (isServer) {
                    getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
                } else {
                    zombie.removeFromWorld();
                }
            }
        });

    }
        
     @Override
     protected void initUI() {
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P, this::startGame2P)), Duration.seconds(.01));
     }

     public void showPlayerCountMenu() {
        PlayerCountMenu menu = new PlayerCountMenu(this::startGame1P, this::startGame2P);
    }

    public void startGame1P() {
        getSceneService().popSubScene();

        getGameWorld().addEntityFactory(new SpawnFactory());

        player1 = spawn("player");
        getInput();
        FXGL.run(() -> {
            zombie = spawn("zombie", player1.getCenter().getX() + 5, player1.getCenter().getY() + 5);
    
            if (zombie.hasComponent(ZombieComponent.class)) {
                zombie.getComponent(ZombieComponent.class).findClosestPlayer();
            }
        }, Duration.seconds(1));

        FXGL.run(() -> updateFollower(), Duration.seconds(1));
    }

    public void startGame2P() {
        getDialogService().showConfirmationBox("Are you the host?", yes -> {
            isServer = yes;
            getGameWorld().addEntityFactory(new SpawnFactory());
            if (yes) {
                var server = getNetService().newTCPServer(55555);
                server.setOnConnected(conn -> {
                    connection = conn;
                    getExecutor().startAsyncFX(() -> {
                        onServer();
                        getSceneService().popSubScene();
                        getSceneService().popSubScene();
                    });
                });
                server.startAsync();
                waitingForPlayers();
            } else {
                var client = getNetService().newTCPClient("10.1.21.51", 55555);
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
        player1 = spawn("player");
        getService(MultiplayerService.class).spawn(connection, player1, "player");

        player2 = spawn("player");
        getService(MultiplayerService.class).spawn(connection, player2, "player");
        
        FXGL.run(() -> {
            zombie = spawn("zombie", player1.getCenter().getX() + 5, player1.getCenter().getY() + 5);
            getService(MultiplayerService.class).spawn(connection, zombie, "zombie");
        }, Duration.seconds(1));

        getService(MultiplayerService.class).addInputReplicationReceiver(connection, gameInput);
        FXGL.run(() -> updateFollower(), Duration.seconds(1));
    }

    private void onClient() {
        player1 = spawn("player");

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

    @Override
    protected void onUpdate(double tpf) {
        if (isServer) {
            gameInput.update(tpf);
        }

        if (isShootingP1) {
            timeSinceLastShotP1 += tpf;
            if (timeSinceLastShotP1 >= shootInterval) {
                shoot(getInput().getMousePositionWorld(), player1); 
                timeSinceLastShotP1 = 0;
            }
        }

        if (isShootingP2) {
            timeSinceLastShotP2 += tpf;
            if (timeSinceLastShotP2 >= shootInterval) {
                shoot(getInput().getMousePositionWorld(), player2); 
                timeSinceLastShotP2 = 0;
            }
        }
    }
        public static void main(String[] args) {
        launch(args);
    }
    
}
