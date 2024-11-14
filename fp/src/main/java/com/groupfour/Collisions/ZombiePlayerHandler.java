package com.groupfour.Collisions;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponents.GuardZombieComponent;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.UI.MainUI;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import com.almasb.fxgl.time.TimerAction;

import java.util.ArrayList;
import java.util.List;

public class ZombiePlayerHandler extends CollisionHandler {
    private boolean canAttack = true;
    private static final Duration ATTACK_DELAY = Duration.seconds(1);
    private static final Duration ATTACK_COOLDOWN = Duration.seconds(1);
    private List<TimerAction> scheduledAttacks = new ArrayList<>();

    public ZombiePlayerHandler() {
        super(EntityType.ZOMBIE, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity zombie, Entity player) {
        ZombieComponent zombieComponent = zombie.getComponent(ZombieComponent.class);
        zombieComponent.startAttacking();
        if (canAttack) {
            handleCollision(zombie, player);
            Sound zombieAttack = FXGL.getAssetLoader().loadSound("ZombieAttack.mp3");
            FXGL.getAudioPlayer().playSound(zombieAttack);
        }
    }

    @Override
    protected void onCollisionEnd(Entity zombie, Entity player) {
        if (zombie.hasComponent(ZombieComponent.class)) {
            zombie.getComponent(ZombieComponent.class).stopAttacking();
        }
        
        for (TimerAction attack : scheduledAttacks) {
            attack.expire();
        }
        scheduledAttacks.clear();
    }

    public void handleCollision(Entity zombie, Entity player) {
        if (canAttack) {
            startAttackSequence(zombie, player);
        }
    }

    private void startAttackSequence(Entity zombie, Entity player) {
        TimerAction attackTask = FXGL.runOnce(() -> {
            if (isInAttackRange(zombie.getPosition(), player.getPosition())) {
                inflictDamage(zombie, player);
            }
        }, ATTACK_DELAY);
        scheduledAttacks.add(attackTask);
    }

    private boolean isInAttackRange(Point2D zombiePosition, Point2D playerPosition) {
        return playerPosition.distance(zombiePosition) < 50;
    }

    private void inflictDamage(Entity zombie, Entity player) {
        double reducedDamage = player.getComponent(PlayerComponent.class).getReducedDamage();
        int baseDamage;
        if (zombie.hasComponent(GuardZombieComponent.class)) { baseDamage = 40; } else baseDamage = 10;
        double finalDamage = (baseDamage * reducedDamage);
        getGameScene().getViewport().shake(15, 5);
        player.getComponent(PlayerComponent.class).takeDamage(finalDamage);

        new MainUI().flashTintRed();

        canAttack = false;
        FXGL.runOnce(() -> canAttack = true, ATTACK_COOLDOWN);
    }

    public boolean isCanAttack() {
        return canAttack;
    }
}
