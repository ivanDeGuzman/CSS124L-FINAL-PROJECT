package com.groupfour.Components.AnimationComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.groupfour.Components.ZombieComponents.DoctorZombieComponent;
import com.groupfour.Components.ZombieComponents.SpitterZombieComponent;
import com.groupfour.Components.ZombieComponents.WelderZombieComponent;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class ZombieAnimComp extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animWalk, animAttack, animDeath;
    private boolean isAttacking;
    private boolean added = true;
    private boolean death = false;

    public ZombieAnimComp() {

    }

    @Override
    public void onAdded() {


    }

    @Override
    public void onUpdate(double tpf) {
        while (added) {

            if (entity.hasComponent(SpitterZombieComponent.class)) {
                animWalk = new AnimationChannel(FXGL.image("Zombies/Spitter_Walking.png"), 11, 50, 50, Duration.seconds(1), 0, 10);
                animAttack = new AnimationChannel(FXGL.image("Zombies/Spitter_Attack.png"), 5, 50, 50, Duration.seconds(1), 0, 4);
                texture = new AnimatedTexture(animWalk);
            } else if (entity.hasComponent(DoctorZombieComponent.class)) {
                animWalk = new AnimationChannel(FXGL.image("Zombies/Healer_Zombie_Walking.png"), 11, 50, 50, Duration.seconds(1),  0,10);
                animAttack = new AnimationChannel(FXGL.image("Zombies/Healer_Zombie_Idle.png"), 1, 50, 50, Duration.seconds(1), 0, 0);
                animDeath = new AnimationChannel(FXGL.image("Zombies/Healer_Zombie_Death.png"), 18, 50, 50, Duration.seconds(2), 1, 17);

                texture = new AnimatedTexture(animWalk);
            } else if (entity.hasComponent(WelderZombieComponent.class)) {
                animWalk = new AnimationChannel(FXGL.image("Zombies/Welder_Walking.png"), 10, 50, 50, Duration.seconds(1),  0,9);
                animAttack = new AnimationChannel(FXGL.image("Zombies/Welder_Torching.png"), 12, 50, 50, Duration.seconds(1), 0, 11);

                texture = new AnimatedTexture(animWalk);
            } else {
                animWalk = new AnimationChannel(FXGL.image("Zombies/Basic_Zombie_Walking.png"), 7, 50, 50, Duration.seconds(1), 1, 4);
                animAttack = new AnimationChannel(FXGL.image("Zombies/Basic_Zombie_Attack.png"), 12, 50, 50, Duration.seconds(1), 0, 11);
                texture = new AnimatedTexture(animWalk);
                texture.setRotate(-180);
            }
                entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
                entity.getViewComponent().addChild(texture);
                texture.loopAnimationChannel(animWalk);
            texture.setRotate(180);
            setAdded(false);
        }
        handleAnimations();
    }

    private void handleAnimations() {
        if (death) {
            texture.playAnimationChannel(animDeath);
        }
        if (isAttacking) {
            if (texture.getAnimationChannel() != animAttack) {
                texture.loopAnimationChannel(animAttack);
            }
        } else {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        }
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }
}
