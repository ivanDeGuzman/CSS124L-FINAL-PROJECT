package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponents.ExplosionComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SpawnFactory implements EntityFactory {

    
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
            .type(EntityType.PLAYER)
            .bbox(new HitBox(new Point2D(12, 12), BoundingShape.box(30, 30))) 
            .with(new NetworkComponent())
            .collidable()
            .with(new PlayerComponent())
            .with(new Component() {
                @Override
                public void onAdded() {
                    entity.getTransformComponent().setRotationOrigin(new Point2D(20, 20)); 
                }
            })
            .build();
    }
    


    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {

        var expireClean = new ExpireCleanComponent(Duration.seconds(5)).animateOpacity();

        Rectangle bulletShape = new Rectangle(12, 3, Color.BLACK); 

        return entityBuilder(data)
        .type(EntityType.BULLET)
        .viewWithBBox(bulletShape)
        .with(new NetworkComponent())
        .collidable()
        .with(new BulletComponent()) 
        .with(expireClean)
        .build();
    }

    @Spawns("explosion")
    public Entity newExplosion(SpawnData data) {
        return  entityBuilder(data)
                .type(EntityType.EXPLOSION)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Circle(data.get("radius"), Color.RED))
                .with(new ExplosionComponent(data.get("radius"), 100))
                .with(new ExpireCleanComponent(Duration.seconds(0.5)))
                .build();
    }


}
