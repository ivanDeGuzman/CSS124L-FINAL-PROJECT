package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class BulletComponent extends Component {
    private Point2D direction;
    private double speed;

    public BulletComponent() {
        this.speed = 600;
    }

    public void setDirection(Point2D direction) {
        this.direction = direction.normalize(); 
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void getbulletDamage() {

    }
    @Override
    public void onUpdate(double tpf) {
        if (direction != null) {
            entity.translate(direction.multiply(speed * tpf));
            double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
            entity.setRotation(angle);
        }
    }
}
    
