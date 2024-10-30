package com.groupfour.UI;

import com.almasb.fxgl.dsl.FXGL;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainUI extends Parent {

    public MainUI() {
        //placeholder class



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
}

