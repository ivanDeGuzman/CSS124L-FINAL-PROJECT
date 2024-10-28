package com.groupfour.Collisions;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.UI.MainUI;
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

    public void inflictDamage(Entity zombie, Entity player) {
        
        player.getComponent(PlayerComponent.class).takeDamage(10);
        MainUI mainUI = new MainUI();
        mainUI.flashTintRed();
        getGameScene().getViewport().shake(15, 5);
    }
}
