package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.groupfour.Components.BulletComponent;
import javafx.util.Duration;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.AnimationComponents.ZombieAnimComp;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Point2D;

import java.util.TimerTask;
import java.util.TimerTask;

public class SpitterZombieComponent extends Component {
    private Entity target;
    private double range = 300.0;
    private double acidDamage = 10;
    private double speed = 800;

    @Override
    public void onAdded() {

        acidCooldownTimer = FXGL.newLocalTimer();
        acidCooldownTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        target = findClosestPlayer();

        if (target != null
                && target.getPosition().distance(getEntity().getPosition()) <= range
                && acidCooldownTimer.elapsed(acidCooldownDuration)) {

            spitAcid(target);
            acidCooldownTimer.capture();

        }
    }

    public Entity findClosestPlayer() {
        var players = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);

        if (players.isEmpty()) {
            return null;
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

        return target;
    }

    private LocalTimer acidCooldownTimer;
    private Duration acidCooldownDuration = Duration.seconds(3);

    public void spitAcid(Entity target) {
        Point2D direction = target.getPosition().subtract(getEntity().getPosition()).normalize();
        Point2D position = getEntity().getPosition().add(direction.multiply(25));

        var data = new SpawnData(position)
                .put("direction", direction)
                .put("damage", getAcidDamage());

        Entity bullet = FXGL.getGameWorld().spawn("spitProjectile", data);
        bullet.getComponent(BulletComponent.class).setDamage(acidDamage);
        bullet.getComponent(BulletComponent.class).setDirection(direction);
        bullet.getComponent(BulletComponent.class).setSpeed(speed);


        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        bullet.setRotation(angle);


    }

    private double getAcidDamage() {
        return acidDamage;
    }


}
