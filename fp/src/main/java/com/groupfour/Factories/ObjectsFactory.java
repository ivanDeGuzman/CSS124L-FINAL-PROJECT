package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.groupfour.Objects.Armory;
import com.groupfour.Objects.Microwave;
import com.groupfour.Objects.VendingMachine;
import com.groupfour.UI.ObjectsUI;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ObjectsFactory implements EntityFactory {

    @Spawns("vmachine")
    public Entity vMachine(SpawnData data) {
        return entityBuilder(data)
        .type(EntityType.VENDING_MACHINE)
        .view("Interactables/vending_machine.png")
        .bbox(new HitBox(BoundingShape.box(67, 52)))
        .collidable()
        .at(600, 400)
        .with(new NetworkComponent())
        .with(new VendingMachine())
        .build();
    }

    @Spawns("microwave")
    public Entity microwave(SpawnData data) {
        return entityBuilder(data)
        .type(EntityType.MICROWAVE)
        .view("Interactables/microwave_close.png")
        .bbox(new HitBox(BoundingShape.box(68, 60)))
        .collidable()
        .at(1600, 600)
        .with(new NetworkComponent())
        .with(new Microwave(new ObjectsUI()))
        .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
        .type(EntityType.WALL)
        .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
        .collidable()
        .with(new NetworkComponent())
        .build();
    }

    @Spawns("armory")
    public Entity newArmory(SpawnData data) {
        return entityBuilder(data)
        .type(EntityType.ARMORY)
        .viewWithBBox("ArmoryTileset.png")
        .at(1000, 400)
        .collidable()
        .with(new NetworkComponent())
        .with(new Armory(new ObjectsUI()))
        .build();
    }
}
