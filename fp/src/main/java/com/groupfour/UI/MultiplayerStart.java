package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.getSceneService;

import com.almasb.fxgl.scene.SubScene;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MultiplayerStart extends SubScene {
    private Text text;
    private int playerCount=1;
    private Button startButton;
    private boolean start=false;

    public MultiplayerStart() {
        
        StackPane layout = new StackPane();
        text = new Text("Player Connected\n Start or Wait For More Players\nCurrent Player Count:"+playerCount);
        startButton = new Button();
        startButton.setText("Start");
        startButton.setOnAction(e->{
            start=true;
            getSceneService().popSubScene();
            getSceneService().popSubScene();
        });
        text.setFont(Font.font("Arial", 24));
        text.setFill(Color.WHITE);
        
        layout.getChildren().add(text);
        
        layout.setPrefSize(800, 600);
        layout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        
        getContentRoot().getChildren().add(layout);
    }

    public void addPlayer(){
        playerCount++;
    }
}