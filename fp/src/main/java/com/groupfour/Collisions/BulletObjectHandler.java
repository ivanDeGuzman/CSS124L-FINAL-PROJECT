package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletObjectHandler extends CollisionHandler {
    public BulletObjectHandler() {
        super(EntityType.BULLET, EntityType.VENDING_MACHINE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity vmachine) {
        bullet.removeFromWorld();
    }
}
