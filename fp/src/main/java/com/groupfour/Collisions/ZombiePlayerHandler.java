package com.groupfour.Collisions;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.App;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ZombiePlayerHandler extends CollisionHandler {

    public ZombiePlayerHandler() {
        super(EntityType.ZOMBIE, EntityType.PLAYER);
    }

    public void flashTintRed() {
    Rectangle redFlash = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.RED);
    redFlash.setOpacity(0); 
    FXGL.getGameScene().addUINode(redFlash);

    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), redFlash);
    fadeTransition.setFromValue(0.4); 
    fadeTransition.setToValue(0);
    fadeTransition.setOnFinished(e -> FXGL.getGameScene().removeUINode(redFlash)); 

    fadeTransition.play();
}
    
    @Override
    protected void onCollisionBegin(Entity zombie, Entity player) {
        inflictDamage(zombie, player);
    }

    // set how much you want the screen to shake here when player takes damage - yuri
    public void inflictDamage(Entity zombie, Entity player) {
        PlayerComponent playerComp = player.getComponent(PlayerComponent.class);
        playerComp.takeDamage(10);
        flashTintRed();
        getGameScene().getViewport().shake(15, 5);
    }
}
