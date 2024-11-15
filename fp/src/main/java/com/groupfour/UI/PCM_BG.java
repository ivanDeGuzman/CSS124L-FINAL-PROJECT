package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PCM_BG extends SubScene {

    public PCM_BG() {
        StackPane stackpane = new StackPane();
        Image bgImage = FXGL.image("banner.png");


        ImageView backgroundImageView = new ImageView(bgImage);
        backgroundImageView.setFitWidth(getAppWidth());
        backgroundImageView.setFitHeight(getAppHeight());
        
        stackpane.getChildren().add(backgroundImageView);
        getContentRoot().getChildren().add(stackpane);
    }
}