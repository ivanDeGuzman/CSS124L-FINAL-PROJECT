package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.image;

import com.almasb.fxgl.core.asset.AssetType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
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
    private Text showAmmo;
    private Text currentGun;
    private StackPane stack, waitLayout;
    private Region healthBar;
    private Region healthBarBorder;
    private int maxHealth = 100;
    private VBox armoryMenu, gunBox;
    private HBox ammoBox;
    private Text waveText, clientText;
    private Font customFont;
    private PlayerComponent pc;
    private ImageView gunImageView;
    private Image gunImage;
    private String gunLink, cansImageLink;

    public MainUI() {
        goldUI();
        healthBar();
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
                gunLink = "/assets/textures/Weapons/Idle/M16_Crop.png";
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
        healthBarBorder.setStyle("-fx-border-style: solid; -fx-border-width: 3; -fx-border-color: black; -fx-border-radius: 15px");
    
        double healthPercent = (double) health / maxHealth;
        healthBar.setMinWidth(200 * healthPercent);
    
        
        Color healthColor;
        if (healthPercent > 0.5) {
            healthColor = Color.GREEN.interpolate(Color.YELLOW, (1 - (healthPercent * 2)));
        } else {
            healthColor = Color.YELLOW.interpolate(Color.RED, (1 - (healthPercent * 2)));
        }
    
        healthBar.setStyle("-fx-background-color: " + toRgbString(healthColor) + "; -fx-background-radius: 15px");
    
        if (healthPercent <= 0) {
            healthBar.setStyle("-fx-background-color: transparent");
            healthText.setFill(Color.RED);
            healthText.setText("DEAD");
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

    public void ammoCansUI(String name) {
        StackPane cansUI = new StackPane();
        ImageView cansImage;
        switch(name) {
            case "energy":
                cansImageLink = "/assets/textures/SodaCans/Energy_Drink.png";
                break;
            case "fizzy":
                cansImageLink = "/assets/textures/SodaCans/Cactus_Mix.png";
                break;
        }
            cansImage = new ImageView(new Image(cansImageLink, 300, 300, false, true));
            cansUI.getChildren().add(cansImage);
            cansUI.setPrefSize(800, 600);
            cansUI.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5)");

            FXGL.getGameScene().addUINode(cansUI);

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), cansUI); 
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setDelay(Duration.seconds(0.3));
            fadeTransition.setOnFinished(e -> FXGL.getGameScene().removeUINode(cansUI));

            fadeTransition.play();
    }

    public void showArmoryUI() {
        customFont = loadFont("PIXELADE.TTF", 30);
        armoryMenu = new VBox();
        armoryMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 10; -fx-border-color: black;");
        armoryMenu.setTranslateX(300);
        armoryMenu.setTranslateY(200);
        armoryMenu.setMinSize(200, 200);
        armoryMenu.setAlignment(Pos.CENTER);

        Text title = new Text("Armory");
        title.setFont(customFont);
        title.setFill(Color.WHITE);
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-border-color: black; -fx-padding: 6px;" +
                "-fx-border-insets: 6px;" +
                "-fx-background-insets: 6px;");
        
        GridPane weaponsForSale = new GridPane();
        weaponsForSale.setHgap(10);
        weaponsForSale.setVgap(10);
    
        // List of weapons
        String[] weaponNames = {"FAMAS", "M16A1"};
        String[] weaponPrices = {"$100", "$300"};
        String[] weaponImageLinks = {
            "/assets/textures/Weapons/Idle/FAMAS_UI.png", 
            "/assets/textures/Weapons/Idle/M16A1_UI.png"
        };
    
        for (int i = 0; i < weaponNames.length; i++) {
            
            ImageView weaponImage = new ImageView(getClass().getResource(weaponImageLinks[i]).toExternalForm());
            weaponImage.setFitWidth(100);
            weaponImage.setFitHeight(100);
            customFont = loadFont("PIXELADE.TTF", 25);
            Text weaponName = new Text(weaponNames[i]);
            weaponName.setFill(Color.WHITE);
            weaponName.setFont(customFont);
            Text weaponPrice = new Text(weaponPrices[i]);
            weaponPrice.setFill(Color.WHITE);
            weaponPrice.setFont(customFont);

//            Button buyButton = new Button(weaponPrices[i]);
            final int index = i;
//            buyButton.setOnAction(e -> {
//                purchaseWeapon(FXGL.getGameWorld().getSingleton(EntityType.PLAYER), weaponNames[index]);
//            });

            VBox weaponBox = new VBox();
            weaponBox.setAlignment(Pos.CENTER);
            weaponBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
            weaponBox.setMinSize(85, 85);
            weaponBox.getChildren().addAll(weaponName, weaponImage, weaponPrice);
//            weaponBox.getChildren().addAll(weaponImage, weaponName, buyButton);
            weaponBox.setOnMouseClicked(e -> {
                purchaseWeapon(FXGL.getGameWorld().getSingleton(EntityType.PLAYER), weaponNames[index]);
            });

            weaponBox.setOnMouseEntered(e -> {
                weaponBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4);");
            });

            weaponBox.setOnMouseExited(e -> {
                weaponBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
            });

            weaponsForSale.add(weaponBox, i % 2, i / 2);
        }

        Text back = new Text("Return");
        back.setFill(Color.WHITE);
        back.setFont(Font.font("Cambria Math", 14));
        VBox backBtn = new VBox(back);
        backBtn.setAlignment(Pos.CENTER);
        backBtn.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-border-color: black; -fx-padding: 6px;" +
                "-fx-border-insets: 6px;" +
                "-fx-background-insets: 6px;");
        backBtn.setOnMouseEntered(e -> {
            backBtn.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: black; -fx-padding: 6px;" +
                    "-fx-border-insets: 6px;" +
                    "-fx-background-insets: 6px;");
        });
        backBtn.setOnMouseExited(e -> {
            backBtn.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-border-color: black; -fx-padding: 6px;" +
                    "-fx-border-insets: 6px;" +
                    "-fx-background-insets: 6px;");
        });
        backBtn.setOnMouseClicked(e -> hideArmoryUI());

        armoryMenu.getChildren().addAll(titleBox, weaponsForSale, backBtn);
        
        FXGL.getGameScene().addUINode(armoryMenu);
    }

    public void purchaseWeapon(Entity player, String weaponName) {
        pc = player.getComponent(PlayerComponent.class);

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
            FXGL.getNotificationService().pushNotification(weaponName + " purchased");
            hideArmoryUI();
        } else {
            FXGL.getNotificationService().pushNotification("Not enough money");
        }
    }

    public void hideArmoryUI() {
        FXGL.getGameScene().removeUINode(armoryMenu);
    }

    public void waveUI() {
        waveText = new Text();
        customFont = loadFont("PIXELADE.TTF", 40);
        waveText.setFont(customFont);
        waveText.setTranslateX(380);
        waveText.setTranslateY(50);
        getChildren().add(waveText);
    }

    public void updateWave(int wave) {
        waveText.setText("Wave " + wave);
    }

    
}
