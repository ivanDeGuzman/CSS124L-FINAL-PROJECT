package com.groupfour.Components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.net.Connection;


public class ObjectsComponent extends Component {
    protected String name;
    protected boolean isServer;
    protected Connection<Bundle> connection;
    
    public ObjectsComponent(String name, boolean isServer, Connection<Bundle> connection) {
        this.name = name;
        this.isServer = isServer;
        this.connection = connection;
    }

}
