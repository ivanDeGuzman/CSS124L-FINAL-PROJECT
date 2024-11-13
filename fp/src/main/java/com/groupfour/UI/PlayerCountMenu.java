package com.groupfour.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.groupfour.UI.MainUI;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import static com.almasb.fxgl.dsl.FXGL.*;
    public  class PlayerCountMenu extends FXGLMenu {
        MainUI ui = new MainUI();
        
        public PlayerCountMenu(Runnable onOnePlayer, Runnable onTwoPlayer) {
            super(MenuType.GAME_MENU);
            playerButton OnePlayerbutton = new playerButton("Play Solo", onOnePlayer);
            playerButton TwoPlayerbutton = new playerButton("Play With A Friend", onTwoPlayer);

            playerButton RTNbutton = new playerButton("Return", () -> {
                FXGL.getDialogService().showConfirmationBox("Are you sure you want to return to the main menu?", answer -> {
                    if (answer) {
                        getGameController().gotoMainMenu();
                        ui.playTitleMusic();
                    }
                });
            });
            

            var box = new VBox(15, OnePlayerbutton, TwoPlayerbutton, RTNbutton);
            box.setTranslateX(100);
            box.setTranslateY(450);
            
            getContentRoot().getChildren().addAll(box);
        }

        private static class playerButton extends StackPane {
            private String name;
            private Runnable action;
        
            private Text text;
        
            public playerButton(String name, Runnable action) {
                this.name = name;
                this.action = action;
        
                text = getUIFactoryService().newText(name, Color.BLACK, 15.0);
                
                getChildren().addAll(text);
                setOnMouseClicked(e -> {
                    action.run();
                });
            }
        }
    }
