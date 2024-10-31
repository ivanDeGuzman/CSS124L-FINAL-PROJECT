package com.groupfour.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.groupfour.mygame.EntityTypes.EntityType;

import static com.almasb.fxgl.dsl.FXGL.texture;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;

public class ZombieComponent extends Component {
    private Entity target;
    private int health;
    private List<Texture> sprites = new ArrayList<>();
    private SpriteState currentState = SpriteState.WALK;

    private enum SpriteState { WALK, ATTACK }

    public ZombieComponent(int initialHealth) {
        this.health = initialHealth;
        
        sprites.add(texture("Zombies/Basic_Zombie_Walking.gif"));
        sprites.add(texture("Zombies/Basic_Zombie_Attack.gif"));

        sprites.forEach(sprite -> sprite.setScaleX(1.5));
        sprites.forEach(sprite -> sprite.setScaleY(1.5));

    }

    public void onAdded() {
        entity.getViewComponent().addChild(sprites.get(SpriteState.WALK.ordinal()));
        entity.getViewComponent().addChild(sprites.get(SpriteState.ATTACK.ordinal()));
        updateSpriteVisibility();
    }

    @Override
    public void onUpdate(double tpf) {
        findClosestPlayer();

        if (target != null) {
            Point2D targetPosition = target.getPosition();
            moveTowardsTarget(targetPosition, tpf);
            updateSpriteState(targetPosition);
        }
    }
    
    private void updateSpriteState(Point2D targetPosition) {
        Point2D zombiePosition = entity.getCenter();
        double distanceToPlayer = targetPosition.distance(zombiePosition);
        SpriteState newState = (distanceToPlayer < 50) ? SpriteState.ATTACK : SpriteState.WALK;

        if (newState != currentState) {
            currentState = newState;
            updateSpriteVisibility();
        }

        Point2D direction = targetPosition.subtract(zombiePosition);
        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        entity.setRotation(angle + 75);
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

    private void updateSpriteVisibility() {
        for (Texture sprite : sprites) {
            sprite.setVisible(false);
        }
        switch (currentState) {
            case ATTACK:
                sprites.get(SpriteState.ATTACK.ordinal()).setVisible(true);
                break;
            case WALK:
                sprites.get(SpriteState.WALK.ordinal()).setVisible(true);
                break;
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
