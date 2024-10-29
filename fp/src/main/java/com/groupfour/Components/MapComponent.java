package com.groupfour.Components;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Objects.VendingMachine;

public class MapComponent extends Component {
    private List<ObjectsComponent> objects = new ArrayList<>();
    private int currentObjectIndex = 0;
    
    public MapComponent() {
        objects.add(new VendingMachine(false, null));
    }

    public ObjectsComponent getCurrentObject() {
        return objects.get(currentObjectIndex);
    }
}
