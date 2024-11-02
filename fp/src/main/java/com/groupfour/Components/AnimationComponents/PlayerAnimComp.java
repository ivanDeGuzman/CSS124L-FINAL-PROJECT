package com.groupfour.Components.AnimationComponents;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class PlayerAnimComp extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animWalk, animIdle;
    private boolean isMoving;
    private AnimatedTexture weaponTexture;
    private AnimationChannel weaponIdle, weaponAttack;
    private boolean isShooting;
    private Image playerSprite;
    private String weaponType = "m16a1";

    public PlayerAnimComp() {
        updatePlayerSprite();
    }   

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        handleWeaponTexture();
    }

    @Override
    public void onUpdate(double tpf) {
        handleWeaponTexture();
        updatePlayerSprite();
        
        if (isMoving) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }

        if (isShooting) {
            if (weaponTexture.getAnimationChannel() != weaponAttack) {
                weaponTexture.loopAnimationChannel(weaponAttack);
            }
        } else {
            if (weaponTexture.getAnimationChannel() != weaponIdle) {
                weaponTexture.loopAnimationChannel(weaponIdle);
            }
        }
    }

    private void handleWeaponTexture() {
        String idlePath, attackPath;
        switch(weaponType.toLowerCase()) {
            case "m16a1": 
                idlePath = "Weapons/Idle/M16_Idle.png";
                attackPath = "Weapons/Shooting/M16_Shooting.png";
                break; 
            case "famas": 
                idlePath = "Weapons/Idle/AK17_Idle.png";
                attackPath = "Weapons/Shooting/AK47_Shooting.png";
                break; 
            case "beretta m9": 
                idlePath = "Weapons/Idle/BerettaM9_Idle.png";
                attackPath = "Weapons/Shooting/BerettaM9_Shooting.png";
                break; 
            default: 
                throw new IllegalArgumentException("Unknown weapon type: " + weaponType);
        }

        updateWeaponTexture(idlePath, attackPath);
    }

    private void updatePlayerSprite() {

        if (weaponType != "m16a1" || weaponType != "famas") {
            playerSprite = FXGL.image("Players/1P_Shoot.png");
        } else {
            playerSprite = FXGL.image("Players/1P_Rifle_Shoot.png");
        }

        animIdle = new AnimationChannel(playerSprite, 3, 50, 50, Duration.seconds(1), 0, 0);
        animWalk = new AnimationChannel(playerSprite, 3, 50, 50, Duration.seconds(0.5), 0, 2);
        texture = new AnimatedTexture(animIdle);
    }

    private void updateWeaponTexture(String idlePath, String attackPath) {
        Image weaponIdleSprite = FXGL.image(idlePath);
        Image weaponAttackSprite = FXGL.image(attackPath);

        weaponIdle = new AnimationChannel(weaponIdleSprite, 1, 25, 25, Duration.seconds(1), 0, 0);
        weaponAttack = new AnimationChannel(weaponAttackSprite, 3, 25, 25, Duration.seconds(0.5), 0, 2);

        if (weaponTexture != null) {
            entity.getViewComponent().removeChild(weaponTexture);
        }

        weaponTexture = new AnimatedTexture(weaponIdle);
        entity.getViewComponent().addChild(weaponTexture);

        weaponTexture.setTranslateX(10);
        weaponTexture.setTranslateY(-7);
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }
}

