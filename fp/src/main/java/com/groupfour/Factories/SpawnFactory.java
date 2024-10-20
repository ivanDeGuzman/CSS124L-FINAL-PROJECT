package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.InBoundsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SpawnFactory implements EntityFactory {

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {

        return entityBuilder(data)
            .type(EntityType.PLAYER)
            .at(100, 100)
            .viewWithBBox(new Polygon(0, 0, 40, 0, 20, 40))
            .with(new NetworkComponent())
            .collidable()
            .with(new InBoundsComponent(new Rectangle2D(0, 0, getAppWidth(), getAppHeight())))
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
