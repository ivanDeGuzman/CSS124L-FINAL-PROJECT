package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.image;

import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.asset.AssetType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.views.MinimapView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.ui.FontFactory;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.WeaponComponent;
import com.groupfour.Weapons.FAMAS;
import com.groupfour.Weapons.M16A1;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private Text staminaText;
    private Region staminaBar;
    private Region staminaBarBorder;
    private int maxStamina = 20;
    private Text showAmmo;
    private Text currentGun;
    private StackPane stack, waitLayout;
    private Region healthBar;
    private Region healthBarBorder;
    private int maxHealth = 100;
    private VBox gunBox;
    private HBox ammoBox;
    private Text waveText, clientText;
    private Font customFont;
    
    private ImageView gunImageView;
    private Image gunImage;
    private String gunLink;

    public MainUI() {
        goldUI();
        healthBar();
        staminaBar();
        gunUI();
        waveUI();
                
    }

    public Font loadFont(String fontPath, int size) {
        FontFactory fontFac = getAssetLoader().load(AssetType.FONT, fontPath);
        return fontFac.newFont(size);
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
        customFont = loadFont("PIXELADE.TTF", 50);
        ImageView goldImage = new ImageView(new Image("/assets/textures/Players/Gold.png"));
        HBox goldBox = new HBox();

        goldText = new Text();
        goldText.setFill(Color.GOLD);
        goldText.setFont(customFont);
        goldBox.setTranslateX(50);
        goldBox.setTranslateY(20);
        goldImage.setTranslateY(15);
        HBox.setMargin(goldText, new Insets(0, 0, 0, 10));
        goldBox.getChildren().addAll(goldImage, goldText);
        getChildren().add(goldBox);
    }

    public void healthBar() {
        customFont = loadFont("PIXELADE.TTF", 29);
        stack = new StackPane();
        healthBar = new Region();
        healthBar.setStyle("-fx-background-color: green; -fx-background-radius: 15px");

        healthBarBorder = new Region();

        healthBarBorder.setTranslateX(50);
        healthBarBorder.setTranslateY(70);

        healthText = new Text();
        healthText.setFont(customFont);
        healthText.setFill(Color.WHITE);


        stack.getChildren().addAll(healthBar, healthText);
        stack.setTranslateX(52);
        stack.setTranslateY(72);
        
        getChildren().addAll(healthBarBorder, stack);
    }

    public void staminaBar() {
        customFont = loadFont("PIXELADE.TTF", 29);
        stack = new StackPane();
        staminaBar = new Region();
        staminaBar.setStyle("-fx-background-color: yellow; -fx-background-radius: 15px");

        staminaBarBorder = new Region();

        staminaBarBorder.setTranslateX(50);
        staminaBarBorder.setTranslateY(110);

        staminaText = new Text();
        staminaText.setFont(customFont);
        staminaText.setFill(Color.WHITE);


        stack.getChildren().addAll(staminaBar, staminaText);
        stack.setTranslateX(52);
        stack.setTranslateY(112);
        
        getChildren().addAll(staminaBarBorder, stack);
    }
    
    public void gunUI() {

        VBox weaponBox = new VBox();
        ammoBox = new HBox();
        gunBox = new VBox();
        customFont = loadFont("PIXELADE.TTF", 35);
        showAmmo = new Text();
        currentGun = new Text();

        showAmmo.setFont(customFont);

        customFont = loadFont("PIXELADE.TTF", 30);
        currentGun.setFont(customFont);
        ImageView ammoImage = new ImageView(new Image("/assets/textures/Weapons/Ammo.png"));
        ammoImage.setTranslateY(3);

        HBox.setMargin(showAmmo, new Insets(0, 0, 0, 10));
        //ammoBox.setTranslateX(-30);
        ammoBox.getChildren().addAll(ammoImage, showAmmo);


        weaponBox.setStyle("-fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black; -fx-border-radius: 15px");
        ammoBox.setStyle("-fx-background-color: gray; -fx-background-radius: 0px 0px 15px 15px");
        gunBox.setPadding(new Insets(0, 0, 0, 5));
        weaponBox.setTranslateX(getAppWidth() * 0.80);
        weaponBox.setTranslateY(getAppHeight() * 0.67);
        gunBox.getChildren().addAll(currentGun);
        weaponBox.getChildren().addAll(gunBox, ammoBox);
        getChildren().add(weaponBox);

    }

    public void updateGunUI(int ammo, int ammoCount, String name) {
        currentGun.setText(name);
        if (name == "FAMAS") {
            currentGun.setText("   " + name);
        }
        showAmmo.setText("" + ammo + "/" + ammoCount);

        gunBox.getChildren().removeIf(node -> node instanceof ImageView);
        gunImageView = new ImageView();
        switch(name.toLowerCase()) {
            case "beretta m9":
                gunLink = "/assets/textures/Weapons/Idle/Beretta_M9_UI.png";
                break;
            case "famas":
                gunLink = "/assets/textures/Weapons/Idle/FAMAS_UI.png";
                break;
            case "m16a1":
                gunLink = "/assets/textures/Weapons/Idle/M16A1_UI.png";
                break;
            default:
                return;
        }
        gunImage = new Image(gunLink, 100, 100, true, true);
        gunImageView.setImage(gunImage);
        gunBox.getChildren().add(0, gunImageView);
    }

    
    public void updateGold(int gold) {   
        goldText.setText("" + gold);
    }

    public void updateHealthBar(int health) {
        healthBarBorder.setMinWidth(205);
        healthBarBorder.setMinHeight(33);
        healthBarBorder.setStyle("-fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
    
        double healthPercent = (double) health / maxHealth;
        healthBar.setMinWidth(200 * healthPercent);
    
        
        Color healthColor;
        if (healthPercent > 0.5) {
            healthColor = Color.GREEN.interpolate(Color.YELLOW, (1 - (healthPercent * 2)));
        } else {
            healthColor = Color.YELLOW.interpolate(Color.RED, (1 - (healthPercent * 2)));
        }
    
        healthBar.setStyle("-fx-background-color: " + toRgbString(healthColor) + ";");
    
        if (healthPercent <= 0) {
            healthBar.setStyle("-fx-background-color: transparent");
            healthText.setFill(Color.RED);
            healthText.setText("DEAD");
        }
    }
    
    public void updatestaminaBar(int stamina) {
        staminaBarBorder.setMinWidth(205);
        staminaBarBorder.setMinHeight(33);
        staminaBarBorder.setStyle("-fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black;");
    
        int staminaPercent = stamina / maxStamina;
        staminaBar.setMinWidth(40 * staminaPercent);
        
    
        if (staminaPercent <= 0) {
            staminaBar.setStyle("-fx-background-color: transparent");
            staminaText.setFill(Color.RED);
            staminaText.setText("RECHARGING");
        }
        else {
            staminaBar.setStyle("-fx-background-color: yellow");
            staminaText.setFill(Color.WHITE);
            staminaText.setText("");
        }
    }
    
    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
    }

    public void waitServerStart() {
        waitLayout = new StackPane();
        clientText = new Text("Server not started yet, please wait");
        waitLayout.getChildren().add(clientText);

        clientText.setFont(Font.font("Arial", 24));
        clientText.setFill(Color.WHITE);
        
        waitLayout.setPrefSize(800, 600);
        waitLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
        
        
        getChildren().add(waitLayout);
        
    }

    public void removeWaitingUI() { 
        getChildren().remove(waitLayout); 
    }

    public void setupMinimap(GameWorld gameWorld) {
         var minimap = new MinimapView(FXGL.getGameWorld(), 800, 800, 200, 100);
         minimap.setEntityColor(Color.GREEN);
    
         FXGL.getGameScene().addUINode(minimap);
    }
    
    public void waveUI() {
        waveText = new Text();
        customFont = loadFont("PIXELADE.TTF", 40);
        waveText.setFont(customFont);
        waveText.setTranslateX(380);
        waveText.setTranslateY(50);
        getChildren().add(waveText);
    }

    public void updateWave(int wave, boolean waveCooldown) {
        if (waveCooldown) {
            waveText.setText("Downtime. Prepare for Wave " + (wave + 1));
            waveText.setTranslateX(220);
        } else {
            waveText.setText("Wave " + wave);
            waveText.setTranslateX(380);
        }
    }

    public void playTitleMusic() {
        Music bgm = getAssetLoader().loadMusic("titleBGM.mp3");
        FXGL.getAudioPlayer().loopMusic(bgm);
    }

    public void stopTitleMusic() {
        // System.out.println("stopTitleMusic called " + stopMusicCallCount + " times.");
        FXGL.getAudioPlayer().stopAllMusic();
    }
}
