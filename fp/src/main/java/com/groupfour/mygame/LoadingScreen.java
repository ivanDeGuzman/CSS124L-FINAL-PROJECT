package com.groupfour.mygame;

import com.almasb.fxgl.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoadingScreen extends SubScene {
    private Text text;

    public LoadingScreen(String message) {
        
        StackPane layout = new StackPane();
        text = new Text(message);
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.WHITE);
        
        layout.getChildren().add(text);
        
        layout.setPrefSize(800, 600);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        
        getContentRoot().getChildren().add(layout);
    }
}