package com.groupfour.Collisions;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponent;
import com.groupfour.UI.MainUI;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class ZombiePlayerHandler extends CollisionHandler {
    private boolean canAttack = true;
    private static final Duration ATTACK_DELAY = Duration.seconds(1);
    private static final Duration ATTACK_COOLDOWN = Duration.seconds(1);

    public ZombiePlayerHandler() {
        super(EntityType.ZOMBIE, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity zombie, Entity player) {
        if (canAttack) {
            handleCollision(zombie, player);
        }
    }

    public void handleCollision(Entity zombie, Entity player) {
        if (canAttack) {
            startAttackSequence(zombie, player);
        }
    }

    private void startAttackSequence(Entity zombie, Entity player) {
        FXGL.runOnce(() -> {
            if (isInAttackRange(zombie.getPosition(), player.getPosition())) {
                inflictDamage(zombie, player);
            }
        }, ATTACK_DELAY);
    }

    private boolean isInAttackRange(Point2D zombiePosition, Point2D playerPosition) {
        return playerPosition.distance(zombiePosition) < 50;
    }

    private void inflictDamage(Entity zombie, Entity player) {
        double reducedDamage = player.getComponent(PlayerComponent.class).getReducedDamage();
        int baseDamage = 10;
        double finalDamage = (baseDamage * reducedDamage);
        player.getComponent(PlayerComponent.class).takeDamage(finalDamage);

        System.out.println("Inflicted Damage: " + finalDamage + " (Reduced Damage Multiplier: " + reducedDamage + ")");

        new MainUI().flashTintRed();
        getGameScene().getViewport().shake(15, 5);

        canAttack = false;
        FXGL.runOnce(() -> canAttack = true, ATTACK_COOLDOWN);
    }
}
