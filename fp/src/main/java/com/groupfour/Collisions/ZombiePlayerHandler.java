package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes.EntityType;


public class ZombiePlayerHandler extends CollisionHandler {

    public ZombiePlayerHandler() {
        super(EntityType.ZOMBIE, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity zombie, Entity player) {
        inflictDamage(zombie, player);
    }

    public void inflictDamage(Entity zombie, Entity player) {
        PlayerComponent playerComp = player.getComponent(PlayerComponent.class);
        playerComp.takeDamage(10);
    }
}
