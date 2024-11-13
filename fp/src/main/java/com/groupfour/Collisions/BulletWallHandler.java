package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletWallHandler extends CollisionHandler {
    public BulletWallHandler() {
        super(EntityType.BULLET, EntityType.WALL);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity wall) {
        bullet.removeFromWorld();
    }
}
