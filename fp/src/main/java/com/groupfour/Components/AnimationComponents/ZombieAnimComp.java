package com.groupfour.Components.AnimationComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class ZombieAnimComp extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animWalk, animAttack;
    private boolean isAttacking;

    public ZombieAnimComp() {
        
        Image walkImage = FXGL.image("Zombies/Basic_Zombie_Walking.png");
        Image attackImage = FXGL.image("Zombies/Basic_Zombie_Attack.png");

        animWalk = new AnimationChannel(walkImage, 7, 50, 50, Duration.seconds(1), 1, 4);
        animAttack = new AnimationChannel(attackImage, 12, 50, 50, Duration.seconds(1), 0, 11);
        texture = new AnimatedTexture(animWalk);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animWalk);
    }

    @Override
    public void onUpdate(double tpf) {
        handleAnimations();
    }

    private void handleAnimations() {
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
}
