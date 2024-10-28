package com.groupfour.Components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.almasb.fxgl.net.Connection;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

public abstract class WeaponComponent {
    protected boolean hasAmmo = true;
    protected String name;
    protected int ammoCount;
    protected int ammo;         
    protected int maxAmmo;      
    protected double fireRate;
    protected boolean isServer;
    protected Connection<Bundle> connection;
    protected boolean isReloading = false;

    public WeaponComponent(String name, int ammoCount, int ammo, int maxAmmo, double fireRate, boolean isServer, Connection<Bundle> connection) {
        this.name = name;
        this.ammoCount = ammoCount;
        this.ammo = ammo;
        this.maxAmmo = maxAmmo;
        this.fireRate = fireRate;
        this.isServer = isServer;
        this.connection = connection;
    }

    public abstract void fire(Entity player);

    public void reload() {
        if (ammoCount > 0 && !isReloading) {
            int ammoNeeded = maxAmmo - ammo;
            int ammoToReload = Math.min(ammoCount, ammoNeeded); 
            isReloading = true;

            runOnce(() -> {
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

    public double getFireRate() {
        return fireRate;
    }

    public boolean getIsReloading() {
        return isReloading;
    }

    protected void spawnBullet(Point2D position, Point2D direction) {
        var data = new SpawnData(position.getX(), position.getY()).put("direction", direction);
        Entity bullet = FXGL.spawn("bullet", data);
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
}
