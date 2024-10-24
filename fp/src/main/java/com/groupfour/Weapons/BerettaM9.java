package com.groupfour.Weapons;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.WeaponComponent;

import javafx.geometry.Point2D;

public class BerettaM9 extends WeaponComponent {

    public BerettaM9(
        //implement int ammoCount and ammo when enemy drops gets coded in
        //int ammoCount, int ammo
        //change ammoCount to whatever if 100 for playtesting isnt enough 
        boolean isServer, Connection<Bundle> connection) {
        super("Beretta M9", 100, 15, 1.0, isServer, connection);
    }

    @Override
    public void fire(Entity player) {
        if (ammo > 0 && !getIsReloading()) {
            ammo--;
            System.out.println(name + " fired. Ammo left: " + ammo); //remove in future
            Point2D position = player.getCenter();
            Point2D direction = FXGL.getInput().getMousePositionWorld().subtract(position).normalize();
            spawnBullet(position, direction);
        } else {
            System.out.println(name + " is out of ammo."); //remove in future
        }
    }
    
}