package com.groupfour.Objects;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.UI.MainUI;
import com.groupfour.mygame.App;

public class Armory extends Component {

    private MainUI mainUI;

    public Armory(MainUI mainUI) {
        this.mainUI = mainUI;
    }


    public void interact() {
        mainUI.showArmoryUI();
    }
}
