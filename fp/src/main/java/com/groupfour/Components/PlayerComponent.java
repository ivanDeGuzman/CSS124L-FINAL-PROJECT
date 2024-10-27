package com.groupfour.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.groupfour.Weapons.BerettaM9;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.app.scene.Viewport;

public class PlayerComponent extends Component {
    private boolean isDead = false;
    private int health;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private WeaponComponent currentWeapon;
    private double speed =2;
    private Input gameInput = getInput();
    private String name="test";

    public PlayerComponent(int initialHealth) {
        this.health = initialHealth;
        this.currentWeapon = new BerettaM9(false, null);
    }
    
    public int getHealth() {
        return health;
    }

    public double getSpeed(){
        return speed;
    }

    public boolean isShooting(){
        return shooting;
    }

    public void setShooting(boolean shooting){
        this.shooting=shooting;
    }

    public double getTimeSinceLastShot(){
        return timeSinceLastShot;
    }

    public void setTimeSinceLastShot(double  timeSinceLastShot){
        this.timeSinceLastShot = timeSinceLastShot;
    }

    public void takeDamage(int damage) {
        if (!isDead) {
            health -= damage;
            System.out.println("Player health: " + health);
            if (health <= 0) {
                onDeath();
            }
        }
    }

    public WeaponComponent getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(WeaponComponent weapon) {
        this.currentWeapon = weapon;
    }

    private void onDeath() {
        isDead = true;
        getDialogService().showMessageBox("You Died! Back to Main Menu?", () -> {
            getGameController().gotoMainMenu();
        });
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D playerPosition = entity.getCenter();
        Point2D mousePosition = getInput().getMousePositionWorld();
        Point2D vector = playerPosition.subtract(mousePosition);
        double angle = Math.toDegrees(Math.atan2(vector.getY(), vector.getX()));
        entity.setRotation(angle - 90);
    }
    
    public boolean isDead() {
        return isDead;
    }



    public void setUpPlayer() {
        onKeyBuilder(getInput(), KeyCode.W).onAction(() -> moveY(false));
        onKeyBuilder(getInput(), KeyCode.S).onAction(() -> moveY(true));
        onKeyBuilder(getInput(), KeyCode.A).onAction(() -> moveX(true));
        onKeyBuilder(getInput(), KeyCode.D).onAction(() -> moveX(false));
        onKeyBuilder(getInput(), KeyCode.R).onActionBegin(() ->{getCurrentWeapon().reload();});
        getInput().addAction(new UserAction("Start Shooting") {
            @Override
            protected void onActionBegin() {
                getCurrentWeapon().fire(entity);
                setShooting(true);
            }
            protected void onActionEnd() {
                setShooting(false);
            }
        }, MouseButton.PRIMARY);

        Viewport viewport = getGameScene().getViewport();
        viewport.setLazy(true); 
        viewport.bindToEntity(entity, getAppWidth() / 2.0, getAppHeight() / 2.0);
    }
 
    private void moveX(boolean isLeft) {
        double tempSpeed = speed;
        if (!isDead()) {
            if (isShooting()) {
                tempSpeed /= 2;
            }
            if (isLeft) {
                tempSpeed = -tempSpeed;
            }
        entity.translateX(tempSpeed);
        }
    }

    private void moveY(boolean isDown) {
        double tempSpeed = speed;
        if (!isDead()) {
            if (isShooting()) {
                tempSpeed /= 2;
            }
            if (!isDown) {
                tempSpeed = -tempSpeed;
            }
        entity.translateY(tempSpeed);
        }
    }

    public String getName(){
    return name;
    }


}
    