package com.groupfour.UI;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.WeaponComponent;
import com.groupfour.Weapons.FAMAS;
import com.groupfour.Weapons.M16A1;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    private Text showAmmo;
    private Text currentGun;
    private StackPane stack;
    private Region healthBar;
    private Region healthBarBorder;
    private int maxHealth = 100;
    private VBox armoryMenu;

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
        goldText.setFont(Font.font("Cambria Math", 25));
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

        if (healthPercent <= 0) {
            healthBar.setStyle("-fx-background-color: transparent");
            healthText.setFill(Color.RED);
            healthText.setText("DEAD");
        }
    }

    
    public void showArmoryUI() {
        armoryMenu = new VBox();
        armoryMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 10;");
        armoryMenu.setTranslateX(FXGL.getAppWidth() / 2);
        armoryMenu.setTranslateY(FXGL.getAppHeight() / 2);
    
        Text title = new Text("Armory");
        title.setFont(Font.font("Cambria Math", 20));
        title.setFill(Color.WHITE);
        
        GridPane weaponsForSale = new GridPane();
        weaponsForSale.setHgap(10);
        weaponsForSale.setVgap(10);
    
        // List of weapons
        String[] weaponNames = {"FAMAS", "M16A1"};
        String[] weaponPrices = {"$100", "$300"};
        String[] weaponImageLinks = {
            "/assets/textures/Weapons/Idle/AK47_Idle.png", 
            "/assets/textures/Weapons/Idle/M16_Idle.png"
        };
    
        for (int i = 0; i < weaponNames.length; i++) {
            
            ImageView weaponImage = new ImageView(getClass().getResource(weaponImageLinks[i]).toExternalForm());
            weaponImage.setFitWidth(50);
            weaponImage.setFitHeight(50);

            Text weaponName = new Text(weaponNames[i]);
            weaponName.setFill(Color.WHITE);
            weaponName.setFont(Font.font("Cambria Math", 14));

            Button buyButton = new Button(weaponPrices[i]);
            final int index = i;
            buyButton.setOnAction(e -> {
                purchaseWeapon(FXGL.getGameWorld().getSingleton(EntityType.PLAYER), weaponNames[index]);
            });

            VBox weaponBox = new VBox();
            weaponBox.setAlignment(Pos.CENTER);
            weaponBox.getChildren().addAll(weaponImage, weaponName, buyButton);

            weaponsForSale.add(weaponBox, i % 2, i / 2);
        }
    
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> hideArmoryUI());
    
        armoryMenu.getChildren().addAll(title, weaponsForSale, backBtn);
        
        FXGL.getGameScene().addUINode(armoryMenu);
    }
    
    public void purchaseWeapon(Entity player, String weaponName) {
        PlayerComponent pc = player.getComponent(PlayerComponent.class);

        WeaponComponent newWeapon = null;
        int price = 0;

        switch(weaponName) {
            case "FAMAS": 
                price = 100; 
                newWeapon = new FAMAS(false, null); 
                break; 
            case "M16A1":
                price = 300; 
                newWeapon = new M16A1(false, null); 
                break; 
            // case "AK47": 
            //     newWeapon = new AK47(false, null); 
            //     price = 250; 
            //     break;
        }
        if (pc.getCurrency() >= price) {
            pc.setCurrencyFromArmory(pc.getCurrency() - price);
            pc.addWeapon(newWeapon);
            hideArmoryUI();
        } else {
            FXGL.getNotificationService().pushNotification("Not enough money");
        }
    }

    public void hideArmoryUI() {
        FXGL.getGameScene().removeUINode(armoryMenu);
    }

    
}
