package com.groupfour.Components;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Objects.Microwave;
import com.groupfour.Objects.VendingMachine;

public class MapComponent extends Component {
    private List<ObjectsComponent> objects = new ArrayList<>();
    private int currentObjectIndex;
    
    public MapComponent() {
        objects.add(new VendingMachine(false, null));
        objects.add(new Microwave(false, null));
    }
    public void setCurrentObjectIndex(int currentObjectIndex) {
        this.currentObjectIndex = currentObjectIndex;
    }
    public ObjectsComponent getCurrentObject() {
        return objects.get(currentObjectIndex);
    }
}
