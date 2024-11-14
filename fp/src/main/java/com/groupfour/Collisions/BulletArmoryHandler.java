package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletArmoryHandler extends CollisionHandler {
    public BulletArmoryHandler() {
        super(EntityType.BULLET, EntityType.ARMORY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity armory) {
        bullet.removeFromWorld();
    }
}
