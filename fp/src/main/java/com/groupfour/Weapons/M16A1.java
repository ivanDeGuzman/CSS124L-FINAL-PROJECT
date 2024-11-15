package com.groupfour.Weapons;

import java.util.Timer;

import java.util.Timer;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class M16A1 extends WeaponComponent {

    private Sound shootSound;
    private AudioPlayer audioPlayer;

    public M16A1() {
        super("M16A1", 180, 30, 30, 0.2, 15);
        this.shootSound = FXGL.getAssetLoader().loadSound("BerettaM9_Shoot.mp3");
        this.audioPlayer = FXGL.getAudioPlayer(); 
    }
         
    @Override
    public void fire(Entity player) {
        isFiring = true;
        if(!initialized){
            FXGL.run(() -> {
                if(isFiring){
                    if (ammo > 0 && !getIsReloading()) {
                        audioPlayer.playSound(shootSound);
                        shoot(player);
                    } else isFiring = false;
                }
            }, Duration.seconds(fireRate));
        initialized=true;
        }
    }

    public void stopFiring() {
        isFiring = false;
    }

    private void shoot(Entity player) {
        ammo--;
        Point2D position = player.getCenter();
        Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();
        spawnBullet(position, direction);
    }
}
