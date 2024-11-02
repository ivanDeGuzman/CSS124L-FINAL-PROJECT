package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.input.Input;
import com.groupfour.Components.AnimationComponents.PlayerAnimComp;
import com.groupfour.UI.MainUI;
import com.groupfour.Weapons.BerettaM9;
import com.groupfour.Weapons.FAMAS;
import com.groupfour.Weapons.M16A1;

import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import java.util.List;
import java.util.ArrayList;

public class PlayerComponent extends Component {
    private PlayerComponent pc;
    private boolean isDead = false;
    private int health = 100;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private List<WeaponComponent> weapons = new ArrayList<>();
    private double originalSpeed = 2;
    private double speed = originalSpeed;
    private int currentWeaponIndex = 0;
    private double reducedDamage = 1.0;
    private int currency = 0;
    private PlayerAnimComp ac;

    private String name="Player 1";

    public PlayerComponent() {
        
        weapons.add(new BerettaM9(false, null));
        weapons.add(new FAMAS(false, null));
        weapons.add(new M16A1(false, null));
    }

    @Override
    public void onAdded() {
        ac = new PlayerAnimComp();
        entity.addComponent(ac);
    }

    public void setCurrency(int amount) { 
        this.currency += amount; 
    }

    public int getCurrency() {
        return currency;
    }

    public int getHealth() {
        return health;
    }

    // BUFFS
    public void heal() {

    }

    public void increaseSpeed(double amount, Duration duration) { 
        speed *= amount; 
        FXGL.runOnce(() -> resetSpeed(), duration); 
    }

    public void increaseWeaponDamage(double amount, Duration duration) {
        weapons.forEach(weapon -> {
            weapon.increaseDamage(amount);
            FXGL.runOnce(() -> weapon.resetDamage(), duration);
        });
    }

    private void resetSpeed() { 
        speed = originalSpeed; 
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

    public void setReducedDamage(double multiplier, Duration duration) { 
        this.reducedDamage = multiplier; FXGL.runOnce(() -> resetReducedDamage(), duration); 
    }
    
    public double getReducedDamage() {
        return reducedDamage;
    }

    private void resetReducedDamage() { 
        this.reducedDamage = 1.0; 
    }

    public void takeDamage(double damage) {
        if (!isDead) {
            damage *= reducedDamage;
            health -= damage;
            System.out.println("Player health: " + health);
            if (health <= 0) {
                setDeath(true);
            }
        }
    }

    public WeaponComponent getCurrentWeapon() {
        return weapons.get(currentWeaponIndex);
    }

    // implement later when you can pickup weapons now
    // public void setCurrentWeapon(WeaponComponent weapon) {
    //     this.currentWeapon = weapon;
    // }

    public void setDeath(boolean dead) {
        isDead = dead;
    }

    public void switchWeapon() {
        runOnce(() -> {
            currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
            System.out.println("Switched to: " + getCurrentWeapon().getName());
        }, Duration.seconds(0.5));
        animWeaponSet();
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D playerPosition = entity.getCenter();
        Point2D mousePosition = getInput().getMousePositionWorld();
        Point2D vector = mousePosition.subtract(playerPosition);
        double angle = Math.toDegrees(Math.atan2(vector.getY(), vector.getX()));
        entity.setRotation(angle + 90);

    }
    
    public boolean isDead() {
        return isDead;
    }

    public void setUpPlayer() {
        Viewport viewport = getGameScene().getViewport();
        viewport.setLazy(true);
        viewport.bindToEntity(entity, getAppWidth() / 2.0, getAppHeight() / 2.0);
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
            ac.setIsMoving(true);
        }
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
            ac.setIsMoving(true);
        }
    }

    public String getName(){
    return name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void stopMoving() {
        ac.setIsMoving(false);
    }

    public void animWeaponSet() {
        ac.setWeaponType(getCurrentWeapon().getName());
    }

}
    