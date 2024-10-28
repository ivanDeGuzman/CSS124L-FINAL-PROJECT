package com.groupfour.Weapons;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class FAMAS extends WeaponComponent {

    private boolean canFire = true;

    public FAMAS(boolean isServer, Connection<Bundle> connection) {
        super("FAMAS", 100, 30, 30, 1.0, isServer, connection); 
    }

    @Override
    public void fire(Entity player) {
        if (ammo >= 3 && !getIsReloading() && canFire) {
            canFire = false;
            for (int i = 0; i < 3; i++) {
                ammo--;
                System.out.println(name + " fired. Ammo left: " + ammo);
                Point2D position = player.getCenter();
                Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();

                FXGL.runOnce(() -> spawnBullet(position, direction), Duration.seconds(i * 0.1));
            }
                FXGL.runOnce(() -> canFire = true, Duration.seconds(fireRate));
        } else {
            System.out.println(name + " fire rate delay");
        }
        if (ammo == 0) {
            System.out.println(name + " out of ammo");
        }
    }

}
