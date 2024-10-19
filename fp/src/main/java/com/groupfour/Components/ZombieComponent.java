package com.groupfour.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Components.EntityTypes.EntityType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;

public class ZombieComponent extends Component {
    private Entity target;

    @Override
    public void onUpdate(double tpf) {
        findClosestPlayer();

        if (target != null) {
            Point2D targetPosition = target.getPosition();
            moveTowardsTarget(targetPosition, tpf);
        }
    }

    public void findClosestPlayer() {
        // Get all player entities
        var players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        if (players.isEmpty()) {
            target = null; // No players available
            return;
        }

        // Initialize target to the first player
        target = players.get(0);
        double closestDistance = target.getPosition().distance(entity.getPosition());

        // Loop through players to find the closest one
        for (Entity player : players) {
            double distance = player.getPosition().distance(entity.getPosition());
            if (distance < closestDistance) {
                closestDistance = distance;
                target = player; // Update target to the closest player
            }
        }
    }

    private void moveTowardsTarget(Point2D targetPosition, double tpf) {
        // Calculate the direction towards the target
        Point2D zombiePosition = entity.getPosition();
        Point2D direction = targetPosition.subtract(zombiePosition).normalize();

        // Move the zombie towards the target
        entity.translate(direction.multiply(100 * tpf)); // Adjust speed as necessary
    }
}
