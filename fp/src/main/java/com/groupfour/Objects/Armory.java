package com.groupfour.Objects;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.UI.MainUI;
import com.groupfour.mygame.App;

public class Armory extends Component {

    private MainUI mainUI;
    private App app;

    public Armory(MainUI mainUI, App app) {
        this.mainUI = mainUI;
        this.app = app;
    }

    public void interact() {
        System.out.println(app.isWaveCooldown());
        if (app.isWaveCooldown()) {
            mainUI.showArmoryUI(); 
        } else {
            System.out.println("The armory is only accessible during wave cooldown."); 
        }
    }
}
