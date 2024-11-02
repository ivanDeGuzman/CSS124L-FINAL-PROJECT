package com.groupfour.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class AnimationComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animWalk, animIdle;
    private boolean isMoving;

    public AnimationComponent() {
        Image image = FXGL.image("Players/1P_Shoot.png");
        animIdle = new AnimationChannel(image, 3, 50, 50, Duration.seconds(1), 0, 0);
        animWalk = new AnimationChannel(image, 3, 50, 50, Duration.seconds(0.5), 0, 2);
        texture = new AnimatedTexture(animIdle);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animIdle);
    }

    @Override
    public void onUpdate(double tpf) {
        // System.out.println(isMoving);
        if (isMoving) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
