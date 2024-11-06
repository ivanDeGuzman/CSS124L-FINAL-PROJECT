package com.groupfour.Objects;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.ObjectsComponent;


public class Wall extends ObjectsComponent{
     public Wall(boolean isServer, Connection<Bundle> connection) {
        super("Wall", isServer, connection);
    }

}
