package com.groupfour.UI;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.groupfour.Components.PlayerComponent;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainUI extends Parent {
    private Text goldText;
    private Text healthText;
    private Text showAmmo;
    private Text currentGun;
    private StackPane stack;
    private Region healthBar;
    private Region healthBarBorder;
    private int maxHealth = 100;

    public MainUI() {
        goldUI();
        healthBar();
        gunUI();
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

    public void goldUI() {
        goldText = new Text();
        goldText.setFill(Color.GOLD);
        goldText.setFont(Font.font("Dosis", 25));
        goldText.setTranslateX(20);
        goldText.setTranslateY(0);
        getChildren().add(goldText);
    }

    public void healthBar() {
        stack = new StackPane();
        healthBar = new Region();
        healthBar.setStyle("-fx-background-color: green;");

        healthBarBorder = new Region();

        healthBarBorder.setTranslateX(18);
        healthBarBorder.setTranslateY(18);

        healthText = new Text();
        healthText.setFont(Font.font("Comic Sans MS", 20));
        healthText.setFill(Color.WHITE);


        stack.getChildren().addAll(healthBar, healthText);
        stack.setTranslateX(20);
        stack.setTranslateY(20);
        
        getChildren().addAll(healthBarBorder, stack);
    }

    public void gunUI() {
        VBox gunBox = new VBox();
        showAmmo = new Text();
        currentGun = new Text();
        showAmmo.setFont(Font.font("Comic Sans MS", 15));
        currentGun.setFont(Font.font("Comic Sans MS", 20));
        showAmmo.setTranslateX(20);
        showAmmo.setTranslateY(100);
        currentGun.setTranslateX(20);
        currentGun.setTranslateY(80);

        gunBox.getChildren().addAll(currentGun, showAmmo);
        getChildren().add(gunBox);
    }

    public void updateGunUI(int ammo, int ammoCount, String name) {
        showAmmo.setText("" + ammo + "/" + ammoCount);
        currentGun.setText(name);
    }
    
    public void updateGold(int gold) {   
        goldText.setText("Gold: " + gold);
    }

    public void updateHealthBar(int health) {

        healthBarBorder.setMinWidth(205);
        healthBarBorder.setMinHeight(33);
        healthBarBorder.setStyle("-fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");

        double healthPercent = (double) health / maxHealth;
        healthBar.setMinWidth(200 * healthPercent);
        healthText.setText("" + health);

        if (healthPercent == 0) {
            healthBar.setStyle("-fx-background-color: transparent");
            healthText.setFill(Color.RED);
            healthText.setText("DEAD");
        }
    }
}
