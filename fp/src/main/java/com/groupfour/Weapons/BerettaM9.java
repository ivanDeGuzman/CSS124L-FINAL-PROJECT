package com.groupfour.Weapons;

import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

import com.almasb.fxgl.audio.AudioPlayer;

public class BerettaM9 extends WeaponComponent {

    private Sound shootSound;
    private AudioPlayer audioPlayer;

    public BerettaM9() {
        super("Beretta M9", 100, 15, 15, 1.0, 15);
        this.shootSound = FXGL.getAssetLoader().loadSound("BerettaM9_Shoot.mp3");
        this.audioPlayer = FXGL.getAudioPlayer();
    }

    @Override
    public void fire(Entity player) {
        if (ammo > 0 && !getIsReloading()) {
            isFiring = true;
            audioPlayer.playSound(shootSound);
            ammo--;
            Point2D position = player.getCenter();
            Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();
            spawnBullet(position, direction);
        } else {
            System.out.println(name + " is out of ammo.");
        }
        FXGL.runOnce(() -> isFiring = false, Duration.seconds(.1));
    }

    // Placeholder
    public void stopFiring() {
        isFiring = false;
    }
}

