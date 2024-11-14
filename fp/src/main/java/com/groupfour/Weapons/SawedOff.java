package com.groupfour.Weapons;

import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

import java.util.Random;

import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

import com.almasb.fxgl.audio.AudioPlayer;

public class SawedOff extends WeaponComponent {

    private Sound shootSound;
    private AudioPlayer audioPlayer;

    public SawedOff() {
        super("Sawed Off", 20, 2, 2, 1.0, 15);
        this.shootSound = FXGL.getAssetLoader().loadSound("Sawed_Off_Shotgun_Shoot.mp3");
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
            runOnce(()->spawnBullet(position, direction.add( Math.random() * 0.5,  Math.random() * 0.5)), Duration.seconds(0.1));
            runOnce(()->spawnBullet(position, direction.add( Math.random() * 0.5,  Math.random() * 0.5)), Duration.seconds(0.1));
            runOnce(()->spawnBullet(position, direction.add( Math.random() * 1,  Math.random() * 1)), Duration.seconds(0.1));
            runOnce(()->spawnBullet(position, direction.add( Math.random() * -0.5,  Math.random() * -0.5)), Duration.seconds(0.1));
            runOnce(()->spawnBullet(position, direction.add( Math.random() * -0.5,  Math.random() * -0.5)), Duration.seconds(0.1));
            runOnce(()->spawnBullet(position, direction.add( Math.random() * 1,  Math.random() * 1)), Duration.seconds(0.1));

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

