package com.groupfour.Objects;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.UI.MainUI;
import com.groupfour.UI.ObjectsUI;
import com.groupfour.mygame.App;

public class Armory extends Component {

    private ObjectsUI objectsUI;

    public Armory(ObjectsUI objectsUI) {
        this.objectsUI = objectsUI;
    }


    public void interact() {
        objectsUI.showArmoryUI();
    }
}
