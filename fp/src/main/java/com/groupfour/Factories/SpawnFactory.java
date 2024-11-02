package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SpawnFactory implements EntityFactory {

    
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
            .type(EntityType.PLAYER)
            .bbox(new HitBox(BoundingShape.box(50, 50)))
            .with(new NetworkComponent())
            .collidable()
            .with(new PlayerComponent())
            .with(new AnimationComponent())
            .buildAndAttach();
    }

        @Spawns("bullet")
        public Entity newBullet(SpawnData data) {

            var expireClean = new ExpireCleanComponent(Duration.seconds(5)).animateOpacity();

            Rectangle bulletShape = new Rectangle(20, 5, Color.BLACK); 

            return entityBuilder(data)
            .type(EntityType.BULLET)
            .viewWithBBox(bulletShape)
            .with(new NetworkComponent())
            .collidable()
            .with(new BulletComponent()) 
            .with(expireClean)
            .build();
        }

}
