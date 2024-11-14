package com.groupfour.Components.ZombieComponents;

import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponents.ZombieAnimComp;
import com.groupfour.UI.MainUI;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Point2D;

public class ZombieComponent extends Component {
    private Entity target;
    private int health;
    private int speed;
    private String type;
    private ZombieAnimComp zac;
    private final double minRotate = 10.0;
    private final int maxHealth;
    private MainUI ui = new MainUI();

    public ZombieComponent(int initialHealth, int speed, String type) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        this.speed = speed;
        this.type = type;
    }

    @Override
    public void onAdded() {
        zac = new ZombieAnimComp();
        entity.addComponent(zac);
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

        entity.translate(direction.multiply(speed * tpf));
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
            Random rand = new Random();
            if (rand.nextInt(100) < 10) {
                ui.addAmmo();
                player.getComponent(PlayerComponent.class).setAmmoFromZombie(10);
            }
        });

        entity.getComponentOptional(DoctorZombieComponent.class)
                .ifPresent(component -> {
                    SpawnData data = new SpawnData(entity.getPosition());
                    FXGL.spawn("healingCircle", data.put("radius", 100.0));
                });


        entity.getComponentOptional(WelderZombieComponent.class)
                .ifPresent(component -> {
                    SpawnData data = new SpawnData(entity.getPosition());
                    FXGL.spawn("explosion", data.put("radius", 50.0));
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
