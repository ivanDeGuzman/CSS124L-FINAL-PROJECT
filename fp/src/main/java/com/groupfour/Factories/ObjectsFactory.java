package com.groupfour.Factories;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.ObjectComponent;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.groupfour.Components.MapComponent;
import com.groupfour.Objects.VendingMachine;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ObjectsFactory implements EntityFactory {

    @Spawns("vmachine")
    public Entity vMachine(SpawnData data) {
        return entityBuilder(data)
        .type(EntityType.VENDING_MACHINE)
        .viewWithBBox(new Rectangle(20, 50, Color.BLUE))
        .collidable()
        .at(100, 100)
        .with(new NetworkComponent())
        .with(new MapComponent())
        .build();
    }
}
