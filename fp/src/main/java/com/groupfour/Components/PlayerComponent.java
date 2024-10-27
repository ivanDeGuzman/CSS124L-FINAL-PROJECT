package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Weapons.BerettaM9;
import javafx.geometry.Point2D;
import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.app.scene.Viewport;

public class PlayerComponent extends Component {
    private boolean isDead = false;
    private int health;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private WeaponComponent currentWeapon;
    private double speed =2;
    private boolean initialized=false;
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
                setDeath(true);
            }
        }
    }

    public WeaponComponent getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(WeaponComponent weapon) {
        this.currentWeapon = weapon;
    }

    public void setDeath(boolean dead) {
        isDead = dead;
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

    public boolean isInitialized(){
        return initialized;
    }

    public void setUpPlayer() {

        Viewport viewport = getGameScene().getViewport();
        viewport.setLazy(true); 
        viewport.bindToEntity(entity, getAppWidth() / 2.0, getAppHeight() / 2.0);
        initialized=true;
    }
 
    public void moveX(boolean isLeft) {
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

    public void copy(PlayerComponent placeholder){

    }

    public void moveY(boolean isDown) {
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
    