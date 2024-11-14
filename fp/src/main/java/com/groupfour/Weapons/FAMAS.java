package com.groupfour.Weapons;

import static com.almasb.fxgl.dsl.FXGL.run;

import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class FAMAS extends WeaponComponent {

    private Sound shootSound;
    private AudioPlayer audioPlayer;



    public FAMAS() {
        super("FAMAS", 160, 30, 30, 0.8, 20);
        this.shootSound = FXGL.getAssetLoader().loadSound("BerettaM9_Shoot.mp3");
        this.audioPlayer = FXGL.getAudioPlayer(); 
    }
    private boolean firingFlagger=false;

    @Override
    public void fire(Entity player) {

        if (ammo >= 3 && !getIsReloading() && !firingFlagger) {
            isFiring = true;
            firingFlagger=true;
            for (int i = 0; i < 3; i++) {
                FXGL.runOnce(() -> {
                    ammo--;
                    audioPlayer.playSound(shootSound);
                    
                    Point2D position = player.getCenter();
                    Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();
                    spawnBullet(position, direction);
                }, Duration.seconds(i * 0.1));
            }
            FXGL.runOnce(() -> firingFlagger = false, Duration.seconds(fireRate));
        } else {
            System.out.println(name + " fire rate delay");
        }
        if (ammo == 0) {
            System.out.println(name + " out of ammo");
        }
    }
    
    //placeholder
    public void stopFiring() {
        // isFiring = false;
    }

}
