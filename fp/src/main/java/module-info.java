module com.groupfour.mygame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.entity;
    requires javafx.base;

    opens com.groupfour.mygame to javafx.fxml;
    exports com.groupfour.mygame;
}
