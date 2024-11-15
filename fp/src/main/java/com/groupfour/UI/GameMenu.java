package com.groupfour.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameMenu extends FXGLMenu {

    public GameMenu() {
        super(MenuType.GAME_MENU);

        playerButton returnButton = new playerButton(FXGL.image("return.png"), () -> {
            fireResume();
        });
        playerButton mmButton = new playerButton(FXGL.image("mm.png"), () -> {
            getGameController().gotoMainMenu();
        });

        var box = new VBox(15, returnButton, mmButton);
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
