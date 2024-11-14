package com.groupfour.Components.ZombieComponents;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import com.groupfour.Components.ZombieComponents.ZombieComponent;
import com.groupfour.mygame.EntityTypes;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import java.util.List;

public class HealingCircleComponent extends Component {
    private final double healRadius;
    private final double healPercent;
    private final Duration healInterval;
    private LocalTimer healTimer;
    private AnimationChannel formation, idle;
    private AnimatedTexture texture;

    public HealingCircleComponent(double healRadius, double healPercent, Duration healInterval) {
        this.healRadius = healRadius;
        this.healPercent = healPercent;
        this.healInterval = healInterval;

        formation = new AnimationChannel(FXGL.image("Zombies/Healer_Zombie_Death.png"), 18, 50, 50, Duration.seconds(1), 6, 18);
        idle = new AnimationChannel(FXGL.image("Zombies/Healer_Zombie_Death.png"), 7, 50, 50, Duration.seconds(1), 0, 6);
        texture = new AnimatedTexture(formation);




    }

    @Override
    public void onAdded() {
        healTimer = FXGL.newLocalTimer();
        healTimer.capture();
        texture.setTranslateX(-25);
        texture.setTranslateY(-25);
        entity.getViewComponent().addChild(texture);
        entity.setZIndex(1);
        texture.playAnimationChannel(formation);

    }
    @Override
    public void onUpdate(double tpf) {
        if (healTimer.elapsed(healInterval)) {
            healNearbyZombies();
            healTimer.capture();
        }

    }

    private void healNearbyZombies() {
        List<Entity> zombies = FXGL.getGameWorld().getEntitiesByType(EntityTypes.EntityType.ZOMBIE);

        for (Entity zombie : zombies) {
            if (zombie.distance(entity) <= healRadius) {
                int healAmount = (int) (zombie.getComponent(ZombieComponent.class).getMaxHealth() * healPercent);
                zombie.getComponent(ZombieComponent.class).setHealth(Math.min((zombie.getComponent(ZombieComponent.class).getHealth() + healAmount), zombie.getComponent(ZombieComponent.class).getMaxHealth()));
                System.out.println(zombie.getComponent(ZombieComponent.class).getHealth());
            }
        }
    }

    public AnimatedTexture getTexture() {
        return texture;
    }

    public void setTexture(AnimatedTexture texture) {
        this.texture = texture;
    }
}
