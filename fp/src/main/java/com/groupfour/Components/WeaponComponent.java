package com.groupfour.Components;

import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.almasb.fxgl.net.Connection;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getEventBus;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

public abstract class WeaponComponent extends Component {
    protected boolean hasAmmo = true;
    protected String name;
    protected int ammoCount;
    protected int ammo;         
    protected int maxAmmo;      
    protected double fireRate;
    protected double damage;
    protected boolean isServer;
    protected Connection<Bundle> connection;
    protected boolean isReloading = false;
    protected double originalDamage;
    protected int offsetX, offsetY;
    protected double playerRotation;
    protected boolean isFiring;
    private Sound reloadSound;
    private AudioPlayer audioPlayer;

    public WeaponComponent(String name, int ammoCount, int ammo, int maxAmmo, double fireRate, double damage) {
        this.name = name;
        this.ammoCount = ammoCount;
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.fireRate = fireRate;
        this.damage = damage;
        this.reloadSound = FXGL.getAssetLoader().loadSound("Reload.mp3");
        this.audioPlayer = FXGL.getAudioPlayer();
    }

    public abstract void fire(Entity player);
    public abstract void stopFiring();

    public void reload() {
        if (ammoCount > 0 && !isReloading) {
            int ammoNeeded = maxAmmo - ammo;
            int ammoToReload = Math.min(ammoCount, ammoNeeded); 
            isReloading = true;

            runOnce(() -> {
                audioPlayer.playSound(reloadSound);
                ammo += ammoToReload;
                ammoCount -= ammoToReload;
                isReloading = false;
                System.out.println("Reloaded! Ammo: " + ammo);
            }, Duration.seconds(1.5));
        }
    }
    
    public int getAmmo() {
        return ammo;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public boolean getIsFiring(){
        return isFiring;
    }

    public double getFireRate() {
        return fireRate;
    }

    public boolean getIsReloading() {
        return isReloading;
    }

    public double getDamage() {
        return damage;
    }

    protected void spawnBullet(Point2D position, Point2D direction) {
        double playerRotationRadians = Math.toRadians(playerRotation);

        double adjustedX = Math.cos(playerRotationRadians) - Math.sin(playerRotationRadians); 
        double adjustedY = Math.sin(playerRotationRadians) + Math.cos(playerRotationRadians);

        double bulletX = position.getX() + adjustedX;
        double bulletY = position.getY() + adjustedY;


        var data = new SpawnData(bulletX, bulletY)
            .put("direction", direction)
            .put("damage", getDamage());

        Entity bullet = FXGL.spawn("bullet", data);
        bullet.getComponent(BulletComponent.class).setDamage(damage);
        bullet.getComponent(BulletComponent.class).setDirection(direction);

        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        bullet.setRotation(angle);

        if (isServer) {
            FXGL.getService(MultiplayerService.class).spawn(connection, bullet, "bullet");
        }
    }

    public String getName() {
        return this.name;
    }

    public void increaseDamage(double bonusDamage) { 
        this.damage *= bonusDamage; 
    } 

    public void resetDamage() {
        this.damage = originalDamage; 
    }

    public void setPlayerRotation(double playerRotation) {
        this.playerRotation = playerRotation;
    }
}
