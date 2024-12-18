package com.groupfour.Collisions;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.Factories.ParticleFactory;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.util.Duration;

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

        ParticleEmitter bloodEmitter = ParticleFactory.createBloodEmitter();
        ParticleComponent bloodParticles = new ParticleComponent(bloodEmitter);

        Entity bloodEffectEntity = new Entity();
        bloodEffectEntity.setPosition(zombie.getPosition());
        bloodEffectEntity.addComponent(bloodParticles);

        FXGL.getGameWorld().addEntity(bloodEffectEntity);

        FXGL.getGameTimer().runOnceAfter(bloodEffectEntity::removeFromWorld, Duration.seconds(1.5));
    }
}
