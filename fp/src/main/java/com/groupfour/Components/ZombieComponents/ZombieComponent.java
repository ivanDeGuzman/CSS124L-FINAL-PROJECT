package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import static com.almasb.fxgl.dsl.FXGL.texture;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;

public class ZombieComponent extends Component {
    private Entity target;
    private int health;

    public ZombieComponent(int initialHealth) {
        this.health = initialHealth;

    }


    @Override
    public void onUpdate(double tpf) {
        findClosestPlayer();

        if (target != null) {
            Point2D targetPosition = target.getPosition();
            moveTowardsTarget(targetPosition, tpf);
        }
    }

    public void findClosestPlayer() {
        var players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        if (players.isEmpty()) {
            target = null;
            return;
        }

        target = players.get(0);
        double closestDistance = target.getPosition().distance(entity.getPosition());

        for (Entity player : players) {
            double distance = player.getPosition().distance(entity.getPosition());
            if (distance < closestDistance) {
                closestDistance = distance;
                target = player;
            }
        }
    }


    private void moveTowardsTarget(Point2D targetPosition, double tpf) {
        Point2D zombiePosition = entity.getPosition(); 
        Point2D direction = targetPosition.subtract(zombiePosition).normalize();

        entity.translate(direction.multiply(100 * tpf));
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            entity.removeFromWorld();
            onDeath();
        }
    }

    public void onDeath() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> { 
            player.getComponent(PlayerComponent.class).setCurrency(10);
            
            
        });
    }

    
}
