package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.ZombieComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletZombieHandler extends CollisionHandler {
    public BulletZombieHandler() {
        super(EntityType.BULLET, EntityType.ZOMBIE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity zombie) {
        bullet.removeFromWorld();
        ZombieComponent zombieComp = zombie.getComponent(ZombieComponent.class);
        zombieComp.takeDamage(20);
    }
}
