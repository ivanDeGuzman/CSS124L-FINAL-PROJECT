package com.groupfour.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.mygame.EntityTypes;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;

public class ExplosionComponent extends Component {
    private final double explosionRadius;
    private final int damage;
    private AnimationChannel formation, idle;
    private AnimatedTexture texture;
    public ExplosionComponent(double explosionRadius, int damage) {
        this.explosionRadius = explosionRadius;
        this.damage = damage;
        formation = new AnimationChannel(FXGL.image("Zombies/Welder_Explosion.png"), 18, 50, 50, Duration.seconds(1), 3, 18);
        idle = new AnimationChannel(FXGL.image("Zombies/Welder_Explosion.png"), 7, 50, 50, Duration.seconds(1), 0, 6);
        texture = new AnimatedTexture(formation);

    }

    @Override
    public void onAdded() {
        damageNearbyEntities();
        texture.setTranslateX(-25);
        texture.setTranslateY(-25);
        texture.setScaleX(2);
        texture.setScaleX(2);
        entity.getViewComponent().addChild(texture);
        entity.setZIndex(1);
        texture.playAnimationChannel(formation);
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
