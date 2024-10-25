package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.texture;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.InBoundsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SpawnFactory implements EntityFactory {

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {

        var p1Texture = texture("1P_Walk.gif");
        p1Texture.setScaleX(1.5);
        p1Texture.setScaleY(1.5);

        return entityBuilder(data)
            .type(EntityType.PLAYER)
            .at(100, 100)
            .viewWithBBox(p1Texture)
            .with(new NetworkComponent())
            .collidable()
            .with(new PlayerComponent(100))
            .build();
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
