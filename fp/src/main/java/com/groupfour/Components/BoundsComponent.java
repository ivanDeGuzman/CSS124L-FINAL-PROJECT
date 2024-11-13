package com.groupfour.Components;

import java.util.ArrayList;
import java.util.List;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Rectangle2D;

public class BoundsComponent extends Component {

    private Rectangle2D bounds;

    public BoundsComponent(Rectangle2D bounds) {
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

    public static void ObjectEntityCollision(Entity player) {
        List<Entity> objects = new ArrayList<>();
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.VENDING_MACHINE));
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.MICROWAVE));
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.WALL));
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.ARMORY));

        for (Entity object : objects) {
            if (player.isColliding(object)) {
                resolveCollision(player, object);
            }
        }
    }

    private static void resolveCollision(Entity entity, Entity object) {
        double halfEntityWidth = entity.getWidth() / 2;
        double halfEntityHeight = entity.getHeight() / 2;
        double halfObjectWidth = object.getWidth() / 2;
        double halfObjectHeight = object.getHeight() / 2;
    
        double overlapX = (halfEntityWidth + halfObjectWidth) - Math.abs((entity.getX() + halfEntityWidth) - (object.getX() + halfObjectWidth));
        double overlapY = (halfEntityHeight + halfObjectHeight) - Math.abs((entity.getY() + halfEntityHeight) - (object.getY() + halfObjectHeight));
    
        if (overlapX > 0 && overlapY > 0) {
            if (overlapX < overlapY) {
                if (entity.getX() < object.getX()) {
                    entity.setX(entity.getX() - overlapX);
                } else {
                    entity.setX(entity.getX() + overlapX);
                }
            } else {
                if (entity.getY() < object.getY()) {
                    entity.setY(entity.getY() - overlapY);
                } else {
                    entity.setY(entity.getY() + overlapY);
                }
            }
        }
    }
    
}
