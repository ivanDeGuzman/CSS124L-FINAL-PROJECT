package com.groupfour.Objects;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.ObjectsComponent;

public class Microwave extends ObjectsComponent {

    public Microwave(boolean isServer, Connection<Bundle> connection) {
        super("Microwave", isServer, connection);
    }

    
    public void interact() {
        System.out.println("Hello");
    }

}
