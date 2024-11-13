package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.texture;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.components.AutoRotationComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.groupfour.Components.BulletComponent;
import com.groupfour.Components.ZombieComponents.*;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ZombieFactory implements EntityFactory {

    private static final int SPAWN_DISTANCE = 500;


    private static final Point2D[] spawnPoints = new Point2D[] {
            new Point2D(SPAWN_DISTANCE, SPAWN_DISTANCE),
            new Point2D(getAppWidth() - SPAWN_DISTANCE, SPAWN_DISTANCE),
            new Point2D(getAppWidth() - SPAWN_DISTANCE, getAppHeight() - SPAWN_DISTANCE),
            new Point2D(SPAWN_DISTANCE, getAppHeight() - SPAWN_DISTANCE)
    };

    // just some code i needed to test

    private static Point2D getRandomSpawnPoint() {
        var players = getGameWorld().getEntitiesByType(EntityType.PLAYER);
        int attempts = 0;
        int maxAttempts = 1000;

        while (attempts < maxAttempts) {
            Point2D potentialSpawnPoint = spawnPoints[FXGLMath.random(0, spawnPoints.length - 1)];
            boolean isSafe = players.stream().allMatch(player -> 
                potentialSpawnPoint.distance(player.getPosition()) >= SPAWN_DISTANCE
            );

            if (isSafe) {
                return potentialSpawnPoint;
            }
            attempts++;
        }

        //if player is in vicinity of spawn area, spawn at a new location
        return new Point2D(SPAWN_DISTANCE, SPAWN_DISTANCE);
    }

    private EntityBuilder commonZombieSetup(SpawnData data, int health) {
        return entityBuilder(data)
                .type(EntityType.ZOMBIE)
                .at(getRandomSpawnPoint())
                .collidable()
                .with(new NetworkComponent())
                .with(new ZombieComponent(health));

    }

    @Spawns("zombie")
    public Entity newZombie(SpawnData data) {
        return commonZombieSetup(data, 600)
                .bbox(new HitBox(new Point2D(5, 5), BoundingShape.box(35, 35)))
                .with(new CellMoveComponent(40, 40, 150))
                .build();
    }

    @Spawns("spitter")
    public Entity newSpitterZombie(SpawnData data) {
        return commonZombieSetup(data, 45)
                .viewWithBBox(new Rectangle(40, 40, Color.BLACK))
                .with(new CellMoveComponent(40, 40, 150))
                .with(new SpitterZombieComponent())
                .build();
    }


    @Spawns("spitProjectile")
    public Entity newProjectile(SpawnData data) {

        var expireClean = new ExpireCleanComponent(Duration.seconds(5)).animateOpacity();

        Rectangle bulletShape = new Rectangle(12, 10, Color.GREEN);

        return entityBuilder(data)
                .type(EntityType.ENEMY_PROJECTILE)
                .viewWithBBox(bulletShape)
                .with(new NetworkComponent())
                .collidable()
                .with(new BulletComponent())
                .with(expireClean)
                .build();

    }

    @Spawns("doctor")
    public Entity newDoctorZombie(SpawnData data) {
        return commonZombieSetup(data, 65)
                .viewWithBBox(new Rectangle(40, 40, Color.RED))
                .with(new CellMoveComponent(40, 40, 150))
                .with(new DoctorZombieComponent())
                .build();
    }

    @Spawns("healingCircle")
    public Entity newHealingCircle(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.ENEMY_PROJECTILE)
                .viewWithBBox(new Circle(200, Color.rgb(0, 255, 0, 0.3)))
                .with(new HealingCircleComponent(data.get("radius"), 0.20, Duration.seconds(1)))
                .with(new ExpireCleanComponent(Duration.seconds(10))) // lasts for 10 seconds
                .build();
    }

    @Spawns("guard")
    public Entity newGuardZombie(SpawnData data) {
        return commonZombieSetup(data, 200)
                .viewWithBBox(new Rectangle(40, 40, Color.PURPLE))
                .with(new CellMoveComponent(40, 40, 250))
                .with(new GuardZombieComponent())
                .build();
    }

    // @Spawns("hazmat_zombie")
    // public Entity newHazmatZombie(SpawnData data) {
    //     return commonZombieSetup(data, 60)
    //             .bbox(new HitBox(BoundingShape.box(50, 50)))
    //             .with(new CellMoveComponent(40, 40, 150))
    //             .build();
    // }

     @Spawns("welder")
     public Entity newWelderZombie(SpawnData data) {
         return commonZombieSetup(data, 70)
                 .viewWithBBox(new Rectangle(40, 40, Color.ORANGE))
                 .with(new CellMoveComponent(40, 40, 150))
                 .with(new WelderZombieComponent())
                 .build();
     }

    // @Spawns("warden")
    // public Entity newWarden(SpawnData data) {
    //     return commonZombieSetup(data, 60)
    //             .bbox(new HitBox(BoundingShape.box(50, 50)))
    //             .with(new CellMoveComponent(40, 40, 150))
    //             .build();
    // }



}
