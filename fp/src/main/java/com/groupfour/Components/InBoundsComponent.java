package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Rectangle2D;

public class InBoundsComponent extends Component {

    private Rectangle2D bounds;

    public InBoundsComponent(Rectangle2D bounds) {
        this.bounds = bounds;
    }

    @Override
    public void onUpdate(double tpf) {
        double x = entity.getX();
        double y = entity.getY();
        double width = entity.getWidth();
        double height = entity.getHeight();

        if (x < bounds.getMinX()) {
            entity.setX(bounds.getMinX());
        } else if (x + width > bounds.getMaxX()) {
            entity.setX(bounds.getMaxX() - width);
        }

        if (y < bounds.getMinY()) {
            entity.setY(bounds.getMinY());
        } else if (y + height > bounds.getMaxY()) {
            entity.setY(bounds.getMaxY() - height);
        }
    }
}