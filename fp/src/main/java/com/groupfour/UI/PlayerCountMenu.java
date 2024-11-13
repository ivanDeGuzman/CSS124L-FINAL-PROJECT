package com.groupfour.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerCountMenu extends FXGLMenu {
    private MainUI ui = new MainUI();
    public PlayerCountMenu(Runnable onOnePlayer, Runnable onTwoPlayer) {
        super(MenuType.GAME_MENU);
        
        playerButton onePlayerButton = new playerButton(FXGL.image("spbtn.png"), onOnePlayer);
        playerButton twoPlayerButton = new playerButton(FXGL.image("mpbtn.png"), onTwoPlayer);
        playerButton returnButton = new playerButton(FXGL.image("return.png"), () -> {
            FXGL.getDialogService().showConfirmationBox("Are you sure you want to return to the main menu?", answer -> {
                if (answer) {
                    getGameController().gotoMainMenu();
                    ui.playTitleMusic();
                }
            });
        });

        var box = new VBox(15, onePlayerButton, twoPlayerButton, returnButton);
        box.setTranslateX(getAppWidth() * 0.32);
        box.setTranslateY(getAppHeight() * 0.30);

        getContentRoot().getChildren().addAll(box);
    }

    private static class playerButton extends StackPane {
        private Image image;
        private Runnable action;

        public playerButton(Image image, Runnable action) {
            this.image = image;
            this.action = action;
        
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            getChildren().addAll(imageView);
            setOnMouseClicked(e -> {
                action.run();
            });

            setOnMouseEntered(e -> imageView.setOpacity(0.7));
            setOnMouseExited(e -> imageView.setOpacity(1.0));
        }
    }
}
