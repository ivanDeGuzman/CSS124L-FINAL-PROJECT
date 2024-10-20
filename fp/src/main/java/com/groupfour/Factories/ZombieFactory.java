package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.groupfour.Components.ZombieComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieFactory implements EntityFactory {

    private static final int SPAWN_DISTANCE = 50;

    private static final Point2D[] spawnPoints = new Point2D[] {
            new Point2D(SPAWN_DISTANCE, SPAWN_DISTANCE),
            new Point2D(getAppWidth() - SPAWN_DISTANCE, SPAWN_DISTANCE),
            new Point2D(getAppWidth() - SPAWN_DISTANCE, getAppHeight() - SPAWN_DISTANCE),
            new Point2D(SPAWN_DISTANCE, getAppHeight() - SPAWN_DISTANCE)
    };

    private static Point2D getRandomSpawnPoint() {
        return spawnPoints[FXGLMath.random(0, 3)];
    }

    @Spawns("zombie")
    public Entity newZombie(SpawnData data) {
    return entityBuilder(data)
            .type(EntityType.ZOMBIE)
            .at(getRandomSpawnPoint())
            .with(new AutoRotationComponent().withSmoothing())
            .collidable()
            .viewWithBBox(new Rectangle(30, 30, Color.GREEN))
            .with((new NetworkComponent()))
            .with(new CellMoveComponent(40, 40, 150))
            .with(new ZombieComponent(100))
            .build();
}
}
