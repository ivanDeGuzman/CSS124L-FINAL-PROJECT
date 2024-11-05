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

    public static void ObjectEntityCollision(Entity player, Entity zombie) { 
        List<Entity> objects = new ArrayList<>(); 
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.VENDING_MACHINE)); 
        objects.addAll(FXGL.getGameWorld().getEntitiesByType(EntityType.MICROWAVE)); 
        objects.forEach(object -> { 
            if (player.isColliding(object)) { 
                double overlapX = Math.min(player.getWidth(), object.getWidth()) - Math.abs(player.getX() - object.getX()); 
                double overlapY = Math.min(player.getHeight(), object.getHeight()) - Math.abs(player.getY() - object.getY()); 
                if (overlapX < overlapY) { 
                    if (player.getX() < object.getX()) {
                        player.setX(object.getX() - player.getWidth()); 
                    } else { 
                        player.setX(object.getX() + object.getWidth()); 
                    } 
                } else { 
                    if (player.getY() < object.getY()) { 
                        player.setY(object.getY() - player.getHeight()); 
                    } else { 
                        player.setY(object.getY() + object.getHeight()); 
                    }
                }
            }
        }); 
    }
}