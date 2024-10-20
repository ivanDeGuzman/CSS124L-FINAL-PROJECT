open module com.groupfour.mygame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.almasb.fxgl.all;
    requires transitive com.almasb.fxgl.entity;
    requires javafx.base;
    requires com.almasb.fxgl.core;
    requires com.almasb.fxgl.io;
    
    exports com.groupfour.Components;
    exports com.groupfour.mygame;
}
