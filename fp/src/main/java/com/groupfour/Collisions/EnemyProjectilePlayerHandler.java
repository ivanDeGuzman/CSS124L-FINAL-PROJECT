package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

public class EnemyProjectilePlayerHandler extends CollisionHandler {
    public EnemyProjectilePlayerHandler() {
        super(EntityType.ENEMY_PROJECTILE, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity player) {
        double damage = bullet.getComponent(BulletComponent.class).getDamage();
        player.getComponent(PlayerComponent.class).takeDamage((int) damage);
        bullet.removeFromWorld();
    }
}
