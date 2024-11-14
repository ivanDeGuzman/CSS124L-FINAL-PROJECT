package com.groupfour.Weapons;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AK47 extends WeaponComponent {


    public AK47() {
        super("AK47", 180, 800, 30, 0.35, 15);
    }

    @Override
    public void fire(Entity player) {
        isFiring = true;
        if(!initialized){
            FXGL.run(() -> {
                if(isFiring){
                    if (ammo < 1||getIsReloading()) {
                        isFiring=false;
                    }
                    shoot(player);
                }
            }, Duration.seconds(fireRate));
        initialized=true;
        }
    }

    public void stopFiring() {
        isFiring = false;
    }

    private void shoot(Entity player) {
        //playing with sounds, dont mind it - padua
        //if (isFiring) FXGL.play("AK47.wav");
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
