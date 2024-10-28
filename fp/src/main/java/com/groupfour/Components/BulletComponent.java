package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class BulletComponent extends Component {
    private Point2D direction;
    private double speed;
    private double damage;

    public BulletComponent() {
        this.speed = 600;
    }

    public void setDirection(Point2D direction) {
        this.direction = direction.normalize();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    @Override
    public void onUpdate(double tpf) {
        if (direction != null) {
            entity.translate(direction.multiply(speed * tpf));
        }
    }
}
