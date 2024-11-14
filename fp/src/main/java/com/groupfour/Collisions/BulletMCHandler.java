package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletMCHandler extends CollisionHandler {
    public BulletMCHandler() {
        super(EntityType.BULLET, EntityType.MICROWAVE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity microwave) {
        bullet.removeFromWorld();
    }
}
