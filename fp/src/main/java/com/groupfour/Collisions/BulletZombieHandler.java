package com.groupfour.Collisions;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

public class BulletZombieHandler extends CollisionHandler {
    public BulletZombieHandler() {
        super(EntityType.BULLET, EntityType.ZOMBIE);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity zombie) {
        double damage = bullet.getComponent(BulletComponent.class).getDamage();
        zombie.getComponent(ZombieComponent.class).takeDamage((int) damage);
        bullet.removeFromWorld();
        System.out.println(zombie.getComponent(ZombieComponent.class).getHealth());
    }
}
