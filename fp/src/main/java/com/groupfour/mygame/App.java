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
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.components.IDComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.multiplayer.*;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.net.Server;
import com.groupfour.Collisions.BulletWallHandler;
import com.groupfour.Collisions.BulletZombieHandler;
import com.groupfour.Collisions.EnemyProjectilePlayerHandler;
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

import javafx.geometry.Point2D;
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
    private ZombiePlayerHandler zombiePlayerHandler;
    private Connection<Bundle> connection;
    private Entity microwave;
    private Entity vmachine, armory;
    private MainUI ui;
    private int wave;
    private Entity newPlayer;
    private double waveMultiplier=1; //10.5 is real, nerfed to test
    private boolean waveCooldown = false;
    private boolean isWaveSpawning;
    private MultiplayerStart multiplayerStart;
    private boolean isServerStarted = false, isNearInteractable = false;
    private ObjectsUI objectsUI;
    private double timeSinceLastCollisionCheck = 0;
    private double timeSinceLastUIUpdate =0;
    private double uiUpdateInterval = 0.2;
    private double collisionCheckInterval = 1.5;
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
                    App.this::startGame1P
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
            }
            protected void onActionEnd() {
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
        player = new Entity();
        zombie = new Entity();
        getGameWorld().addEntityFactory(new SpawnFactory());
        getGameWorld().addEntityFactory(new ZombieFactory());
        getGameWorld().addEntityFactory(new ObjectsFactory());
        zombiePlayerHandler = new ZombiePlayerHandler();
        setLevelFromMap("Warehouse.tmx");
    }

    @Override
    protected void initUI() {
        FXGL.runOnce(() -> {
            FXGL.getSceneService().pushSubScene(new PCM_BG());
            FXGL.getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P));
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
                physics.addCollisionHandler(new BulletWallHandler());
                physics.addCollisionHandler(new EnemyProjectilePlayerHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
                
            } else {
                physics.addCollisionHandler(new BulletZombieHandler());
                physics.addCollisionHandler(new BulletWallHandler());
                physics.addCollisionHandler(new EnemyProjectilePlayerHandler());
                physics.addCollisionHandler(new ZombiePlayerHandler());
                
            }
            FXGL.run(() -> {
                if(player.isActive()){
                    BoundsComponent.ObjectEntityCollision(player);
                    BoundsComponent.ZombieObjectCollision(zombie);
                }
            }, Duration.seconds(0.017));
    }

    public void startGame1P() {
        ui.stopTitleMusic();
        player = spawn("player", new Point2D(getAppWidth() / 2, getAppHeight() / 2));
        vmachine = spawn("vmachine");
        microwave = spawn("microwave");
        armory = spawn("armory");
        player.getComponent(PlayerComponent.class).setUpPlayer();
        wave=0;

        waveAndDeathManager();
        
        

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
                    
                    runOnce(() -> {
                        wave++;
                        nextWave(wave, waveMultiplier);
                        waveCooldown = false;
                        isWaveSpawning = false;

                    }, Duration.seconds(20));
                } else {
                    wave++;
                    nextWave(wave, waveMultiplier);
                }
            }

                if(player.getComponent(PlayerComponent.class).isDead()){
                    player.getComponent(PlayerComponent.class).setDeath(false);
                    getDialogService().showMessageBox("You Died! Back to Main Menu?", () -> {
                    ui.playTitleMusic();
                    getGameController().gotoMainMenu();
                    FXGL.runOnce(() -> {
                        resetGameWorld();
                    }, Duration.seconds(1));
                    });
                }
            },Duration.seconds(0.1));
    }

    private void nextWave(int wave, double waveMultiplier){
        int totalZombies = (int)(wave * waveMultiplier);
        int i;
        Duration interval = Duration.seconds(1);

        for(i = 0; i < totalZombies ; i++) {
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                spawn("zombie");
            }, delay);
        }

        for(int guardWave=wave;guardWave>6;guardWave/=2){
            i = 0;
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                spawn("guard");
            }, delay);
            i++;
        }

        for(int spitterWave=wave;spitterWave>4;spitterWave/=2){
            i = 0;
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                spawn("spitter");
            }, delay);
            i++;
        }

        for(int doctorWave=wave;doctorWave>8;doctorWave/=2){
            i = 0;
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                spawn("doctor");
            }, delay);
            i++;
        }

        for(int welderWave=wave;welderWave>2;welderWave/=2){
            i = 0;
            Duration delay = interval.multiply(i);
            runOnce(() -> {
                spawn("welder");
            }, delay);
            i++;
        }
    }
           



    public void resetGameWorld() {
        getGameWorld().getEntities().forEach(entity -> entity.removeFromWorld());
        zombie = null;
        getSceneService().pushSubScene(new PCM_BG());
        getSceneService().pushSubScene(new PlayerCountMenu(this::startGame1P));
    }

    @Override
    protected void onUpdate(double tpf) {
        System.out.println(players.size());
        timeSinceLastCollisionCheck += tpf;
        timeSinceLastUIUpdate += tpf;

        if (timeSinceLastCollisionCheck >= collisionCheckInterval) {
            timeSinceLastCollisionCheck = 0;
            checkCollisions();
        }


        if (timeSinceLastUIUpdate >= uiUpdateInterval) {
            timeSinceLastUIUpdate = 0;
            updateUI();
        }

        if (!player.isActive()) return;
        updateInteractableStatus();

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
        
        player.getComponent(PlayerAnimComp.class).setWeaponType(player.getComponent(PlayerComponent.class).getCurrentWeapon().getName());
        player.getComponent(PlayerComponent.class).getCurrentWeapon().setPlayerRotation(player.getRotation());
        

    }

    private void updateInteractableStatus() {
        isNearInteractable = player.distance(vmachine) < 70 ||
                player.distance(microwave) < 70 ||
                (player.distance(armory) < 70 && waveCooldown);
    }

    private void updateUI() {
        //ui.setupMinimap(getGameWorld());
        ui.updateGold(player.getComponent(PlayerComponent.class).getCurrency());
        ui.updateHealthBar(player.getComponent(PlayerComponent.class).getHealth());
        ui.updateGunUI(
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getAmmo(),
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getAmmoCount(),
            player.getComponent(PlayerComponent.class).getCurrentWeapon().getName()
        );

        ui.updateWave(wave, waveCooldown);
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
