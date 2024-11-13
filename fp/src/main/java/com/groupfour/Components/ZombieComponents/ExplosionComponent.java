package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.FXGL;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes;

import java.util.List;
import java.util.Optional;

public class ExplosionComponent extends Component {
    private final double explosionRadius;
    private final int damage;

    public ExplosionComponent(double explosionRadius, int damage) {
        this.explosionRadius = explosionRadius;
        this.damage = damage;
    }

    @Override
    public void onAdded() {
        damageNearbyEntities();
    }

    private void damageNearbyEntities () {

            List<Entity> entities = FXGL.getGameWorld().getEntitiesByType(EntityTypes.EntityType.ZOMBIE, EntityTypes.EntityType.PLAYER);

            for (Entity entity : entities) {
                if (entity.distance(getEntity()) <= explosionRadius) {

                    Optional<ZombieComponent> zombieComponent = entity.getComponentOptional(ZombieComponent.class);
                    zombieComponent.ifPresent(component -> component.takeDamage(damage));

                    Optional<PlayerComponent> playerComponent = entity.getComponentOptional(PlayerComponent.class);
                    playerComponent.ifPresent(component -> component.takeDamage(damage));

                }
            }


    }

}
