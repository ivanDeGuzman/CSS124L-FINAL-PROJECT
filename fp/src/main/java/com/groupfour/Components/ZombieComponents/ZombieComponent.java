package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponents.ZombieAnimComp;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ZombieComponent extends Component {
    private Entity target;
    private int health;
    private ZombieAnimComp zac;
    private final double minRotate = 10.0;
    private final int maxHealth;

    public ZombieComponent(int initialHealth) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
    }

    @Override
    public void onAdded() {
        zac = new ZombieAnimComp();
        entity.addComponent(zac);
        entity.setZIndex(10);
    }

    @Override
    public void onUpdate(double tpf) {
        findClosestPlayer();

        if (target != null) {
            Point2D targetPosition = target.getPosition();
            moveTowardsTarget(targetPosition, tpf);
            rotateTowardsTarget(targetPosition);
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

    public void moveTowardsTarget(Point2D targetPosition, double tpf) {
            Point2D zombiePosition = entity.getPosition();
            Point2D direction = targetPosition.subtract(zombiePosition).normalize();

            entity.translate(direction.multiply(100 * tpf));
    }

    public void rotateTowardsTarget(Point2D targetPosition) {
        Point2D zombiePosition = entity.getPosition();
        Point2D direction = targetPosition.subtract(zombiePosition).normalize();
        double distance = zombiePosition.distance(targetPosition);

        if (distance > minRotate) {
            double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
            entity.setRotation(angle + 90);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
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
            player.getComponent(PlayerComponent.class).setCurrencyFromZombie(10);





            if (entity.hasComponent(DoctorZombieComponent.class)) {
                SpawnData data = new SpawnData(entity.getPosition());
                FXGL.spawn("healingCircle", data.put("radius", 100.0));
            }


            if (entity.hasComponent(WelderZombieComponent.class)) {
                SpawnData data = new SpawnData(entity.getPosition());
                FXGL.spawn("explosion", data.put("radius", 50.0));
            }


            entity.removeFromWorld();



        });
    }

    public void startAttacking() {
        zac.setIsAttacking(true);
    }

    public void stopAttacking() {
        zac.setIsAttacking(false);
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
