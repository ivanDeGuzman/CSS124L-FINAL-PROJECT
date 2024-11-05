package com.groupfour.Weapons;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class M16A1 extends WeaponComponent {
    private boolean isFiring = false;

    public M16A1(boolean isServer, Connection<Bundle> connection) {
        super("M16A1", 180, 30, 30, 0.5, 10, isServer, connection);
    }

    @Override
    public void fire(Entity player) {
        System.out.println(offsetX + offsetY);
        if (ammo > 0 && !getIsReloading() && !isFiring) {
            isFiring = true;
            FXGL.run(() -> {
                if (ammo > 0 && isFiring) {
                    shoot(player);
                } else {
                    isFiring = false;
                }
            }, Duration.seconds(fireRate));
        }
    }

    public void stopFiring() {
        isFiring = false;
    }

    private void shoot(Entity player) {
        //playing with sounds, dont mind it - padua
        //if (isFiring) FXGL.play("m16a1.wav");
        ammo--;
        System.out.println(name + " fired. Ammo left: " + ammo);
        Point2D position = player.getCenter();
        Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();
        spawnBullet(position, direction);
        if (ammo == 0) {
            System.out.println(name + " out of ammo");
        }
    }
}
