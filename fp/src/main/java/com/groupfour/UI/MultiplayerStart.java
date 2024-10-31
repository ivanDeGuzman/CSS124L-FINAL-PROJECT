package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;

import com.almasb.fxgl.scene.SubScene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MultiplayerStart extends SubScene {
    private Text text;
    private int playerCount=1;
    private Button startButton;
    public MultiplayerStart() {
        
        StackPane layout = new StackPane();
        text = new Text();
        startButton = new Button();
        startButton.setText("Start");
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.WHITE);
        
        layout.getChildren().addAll(text, startButton);
        
        layout.setPrefSize(800, 600);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        
        getContentRoot().getChildren().add(layout);
    }
    public void setOnStartClick(EventHandler<ActionEvent> handler){
        startButton.setOnAction(handler);
    }

    public void addPlayer(){
        playerCount++;
        text.setText("Player Connected\nStart or Wait For More Players\nCurrent Player Count:"+playerCount);
    }
}