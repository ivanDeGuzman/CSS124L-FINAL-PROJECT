package com.groupfour.Components.AnimationComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.groupfour.Components.PlayerComponent;

import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class PlayerAnimComp extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animWalk, animIdle;
    private boolean isMoving;
    private AnimatedTexture weaponTexture;
    private AnimationChannel weaponIdle, weaponAttack;
    private String weaponType = "beretta m9";
    private int frames, frameEnd, width, height, setX, setY;
    private int playerFrames, pfEnd;

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        handleWeaponTexture();
        handlePlayerTexture();
    }

    @Override
    public void onUpdate(double tpf) {
        handleWeaponTexture();
        handlePlayerTexture();

        if (isMoving) {
            // entity.getComponent(PlayerComponent.class).getCurrentWeapon().isFiring();
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }

        if (entity.getComponent(PlayerComponent.class).getCurrentWeapon().getIsFiring()) {
            if (weaponTexture.getAnimationChannel() != weaponAttack) {
                weaponTexture.loopAnimationChannel(weaponAttack);
            }
        } else {
            if (weaponTexture.getAnimationChannel() != weaponIdle) {
                weaponTexture.loopAnimationChannel(weaponIdle);
            }
        }
    }

    private void handlePlayerTexture() {
        String idlePath, walkPath;
        if (weaponType.equalsIgnoreCase("M16A1") || weaponType.equalsIgnoreCase("FAMAS")||weaponType.equalsIgnoreCase("AK47")||weaponType.equalsIgnoreCase("Sawed Off")) {
            idlePath = "Players/1P_Rifle_Shoot.png";
            walkPath = "Players/1P_Rifle_Shoot.png";
            playerFrames = 2;
            pfEnd = 1;
        } else {
            idlePath = "Players/1P_Shoot.png";
            walkPath = "Players/1P_Shoot.png";
            playerFrames = 3;
            pfEnd = 2;
        }
        updatePlayerSprite(idlePath, walkPath, playerFrames, pfEnd);
    }

    private void handleWeaponTexture() {
        String idlePath, attackPath;

        switch(weaponType.toLowerCase()) {
            case "m16a1":
                idlePath = "Weapons/Idle/M16_Idle.png";
                attackPath = "Weapons/Shooting/M16_Shooting.png";
                width = 50;
                height = 50;
                frames = 2;
                frameEnd = 1;
                setX = 10;
                setY = -7;
                break;
            case "famas":
                idlePath = "Weapons/Idle/FAMAS_Idle.png";
                attackPath = "Weapons/Shooting/FAMAS_Shooting.png";
                width = 50;
                height = 50;
                frames = 2;
                frameEnd = 1;
                setX = 10;
                setY = -7;
                break;
            case "beretta m9":
                idlePath = "Weapons/Idle/BerettaM9_Idle.png";
                attackPath = "Weapons/Shooting/BerettaM9_Shooting.png";
                width = 20;
                height = 20;
                frames = 3;
                frameEnd = 2;
                setX = 12;
                setY = 0;
                break;
            case "ak47":
                idlePath = "Weapons/Idle/AK47_Idle.png";
                attackPath = "Weapons/Shooting/AK47_Shooting.png";
                width = 50;
                height = 50;
                frames =2;
                frameEnd = 1;
                setX = 10;
                setY = -7;
                break;
            case "sawed off":
                idlePath = "Weapons/Idle/Sawed_Off_DoubleBarrel.png";
                attackPath = "Weapons/Shooting/Sawed_Off_DoubleBarrel_Shooting.png";
                width = 50;
                height = 50;
                frames =2;
                frameEnd = 1;
                setX = 10;
                setY = -7;
                break;
            default:
                throw new IllegalArgumentException("Unknown weapon type: " + weaponType);
        }

        updateWeaponTexture(idlePath, attackPath, frames, frameEnd, width, height);
    }

    private void updatePlayerSprite(String idlePath, String walkPath, int playerFrames, int playerEnd) {
        Image idleSprite = FXGL.image(idlePath);
        Image walkSprite = FXGL.image(walkPath);

        animIdle = new AnimationChannel(idleSprite, playerFrames, 50, 50, Duration.seconds(0.5), 0, 0);
        animWalk = new AnimationChannel(walkSprite, playerFrames, 50, 50, Duration.seconds(0.5), 0, playerEnd);

        if (texture != null) {
            entity.getViewComponent().removeChild(texture);
        }

        texture = new AnimatedTexture(animIdle);
        entity.getViewComponent().addChild(texture);
    }

    private void updateWeaponTexture(String idlePath, String attackPath, int frames, int frameEnd, int width, int height) {
        Image weaponIdleSprite = FXGL.image(idlePath);
        Image weaponAttackSprite = FXGL.image(attackPath);

        weaponIdle = new AnimationChannel(weaponIdleSprite, 1, width, height, Duration.seconds(1), 0, 0);
        weaponAttack = new AnimationChannel(weaponAttackSprite, frames, width, height, Duration.seconds(0.5), 0, frameEnd);

        if (weaponTexture != null) {
            entity.getViewComponent().removeChild(weaponTexture);
        }

        weaponTexture = new AnimatedTexture(weaponIdle);
        entity.getViewComponent().addChild(weaponTexture);

        weaponTexture.setTranslateX(setX);
        weaponTexture.setTranslateY(setY);
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }
}
