package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class SpitterZombieComponent extends Component {
    private Entity target;
    private boolean canSpit = true;

    public void onUpdate(double tpf) {
        spitAtPlayer(target);
    }

    public void spawnProjectile(Point2D position, Point2D direction) { 
        var data = new SpawnData(position.getX(), position.getY()) 
            .put("direction", direction); 
        Entity projectile = FXGL.spawn("spitProjectile", data); 
        projectile.addComponent(new ProjectileComponent(direction, 300)); 
    }
    
    public void spitAtPlayer(Entity player) { 
        if (canSpit) { 
            canSpit = false; 
            Point2D playerPosition = player.getPosition();
            Point2D zombiePosition = entity.getPosition(); 
            Point2D direction = playerPosition.subtract(zombiePosition).normalize(); 
            spawnProjectile(zombiePosition, direction); 
            FXGL.runOnce(() -> canSpit = true, Duration.seconds(2)); 
        }
    }
}
