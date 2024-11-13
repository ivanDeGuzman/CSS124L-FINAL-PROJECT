package com.groupfour.UI;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ObjectsUI extends Parent {
    
    private VBox armoryMenu;
    private Font customFont;
    private String cansImageLink;
    private PlayerComponent pc;
    public Text canInteractNode;

    public Font loadFont(String fontPath, int size) {
        FontFactory fontFac = getAssetLoader().load(AssetType.FONT, fontPath);
        return fontFac.newFont(size);
    }

    public void vmNoInteract() {
        customFont = loadFont("PIXELADE.TTF", 25);
        Text cantInteract = new Text("Vending machine on cooldown. Please wait 1 minute.");
        cantInteract.setFont(customFont);
        cantInteract.setTranslateX(getAppWidth() * 0.2);
        cantInteract.setTranslateY(getAppHeight() * 0.75);

        FXGL.getGameScene().addUINode(cantInteract);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), cantInteract); 
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(2.5));
        fadeTransition.setOnFinished(e -> FXGL.getGameScene().removeUINode(cantInteract));

        fadeTransition.play();
        
    }

    public void showCanInteract() {
        customFont = loadFont("PIXELADE.TTF", 25);
        
        Text canInteract = new Text("Press 'F' to interact");
        
        canInteract.setFont(customFont);
        canInteract.setTranslateX(getAppWidth() * 0.3);
        canInteract.setTranslateY(getAppHeight() * 0.75);
        
        FXGL.getGameScene().addUINode(canInteract);
        this.canInteractNode = canInteract;
    }
    
    
    public void hideCanInteract() {
        if (canInteractNode != null) {
            FXGL.getGameScene().removeUINode(canInteractNode);
            canInteractNode = null;
        }
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
            case "tsludge":
                cansImageLink = "/assets/textures/SodaCans/Toxic_Sludge.png";
                break;
            case "icecold":
                cansImageLink = "/assets/textures/SodaCans/Ice_Cold_Brew.png";
                break;
            case "atomic":
                cansImageLink = "/assets/textures/SodaCans/Atomic_Tonic.png";
                break;
            case "nuke":
                cansImageLink = "/assets/textures/SodaCans/Nuclear_Soda.png";
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
}
