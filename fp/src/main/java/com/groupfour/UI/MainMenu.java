package com.groupfour.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGL.*;

public class MainMenu extends FXGLMenu {

    public MainMenu(Runnable onSinglePlayer) {
        super(MenuType.MAIN_MENU);

        playerButton singlePlayerButton = new playerButton(FXGL.image("spbtn.png"), onSinglePlayer);
        playerButton returnButton = new playerButton(FXGL.image("mm.png"), () -> {
            getGameController().gotoMainMenu();
        });

        var box = new VBox(15, singlePlayerButton, returnButton);
        box.setTranslateX(getAppWidth() * 0.32);
        box.setTranslateY(getAppHeight() * 0.30);

        getContentRoot().getChildren().addAll(box);
    }

    private static class playerButton extends StackPane {
        public playerButton(Image image, Runnable action) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            getChildren().addAll(imageView);
            setOnMouseClicked(e -> action.run());
            setOnMouseEntered(e -> imageView.setOpacity(0.7));
            setOnMouseExited(e -> imageView.setOpacity(1.0));
        }
    }
}
