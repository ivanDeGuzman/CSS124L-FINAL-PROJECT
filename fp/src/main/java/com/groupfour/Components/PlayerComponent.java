package com.groupfour.Components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.groupfour.Components.AnimationComponents.PlayerAnimComp;
import com.groupfour.UI.MainUI;
import com.groupfour.Weapons.AK47;
import com.groupfour.Weapons.BerettaM9;
import com.groupfour.Weapons.FAMAS;
import com.groupfour.Weapons.M16A1;
import com.groupfour.Weapons.SawedOff;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;

import java.util.List;
import java.util.ArrayList;

public class PlayerComponent extends Component {
    private boolean isDead = false;
    private int health = 100;
    private int stamina = 100;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private List<WeaponComponent> weapons = new ArrayList<>();
    private double originalSpeed = 3;
    private double speed = originalSpeed;
    private int currentWeaponIndex = 0;
    private double reducedDamage = 1.5;
    private int currency = 100;
    private PlayerAnimComp ac;
    private Input clientInputs = new Input();
    private double angle;
    private Point2D lastMousePosition;
    private boolean isSprinting = false;
    private double STAMINA_DECAY_RATE = 0.05;
    private double STAMINA_RECHARGE_RATE = 10;
    private double STAMINA_RECHARGE_DELAY = .5;
    private double staminaRechargeTimer = 0;


    private String name="Player 1";

    public PlayerComponent() {
        weapons.add(new BerettaM9());
        weapons.add(new M16A1());
        weapons.add(new AK47());
        weapons.add(new SawedOff());


        
    }

    @Override
    public void onAdded() {
        ac = new PlayerAnimComp();
        entity.addComponent(ac);
        lastMousePosition = getInput().getMousePositionWorld();
    }

    public void setCurrencyFromZombie(int amount) { 
        this.currency += amount; 
    }

    public void setCurrencyFromArmory(int currency) {
        this.currency = currency;
    }

    public void setSprinting(boolean sprinting) {
        this.isSprinting = sprinting;
    }

    public int getCurrency() {
        return currency;
    }

    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public void setInput(Input input){
        this.clientInputs = input;
    }

    public Input getClientInput(){
        return clientInputs;
    }

    // BUFFS

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
            if (health <= 0) {
                setDeath(true);
            }
        }
    }

    public WeaponComponent getCurrentWeapon() {
        return weapons.get(currentWeaponIndex);
    }

    public void setDeath(boolean dead) {
        isDead = dead;
    }

    public void switchWeapon() {
        runOnce(() -> {
            getCurrentWeapon().stopFiring();
            currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
            System.out.println("Switched to: " + getCurrentWeapon().getName());
        }, Duration.seconds(0.5));
        animWeaponSet();
    }

    @Override
    public void onUpdate(double tpf) {

        int MAX_STAMINA = 100;
        if (clientInputs != null) {
            mouseUpdate();
        }
        
        if (isSprinting) {
            if (stamina > 0) {
                stamina -= STAMINA_DECAY_RATE;
            } else {
                isSprinting = false;
            }
        } else {
            if (stamina < MAX_STAMINA || stamina == 0) {
                staminaRechargeTimer += tpf;
                if (staminaRechargeTimer >= STAMINA_RECHARGE_DELAY) {
                    stamina += STAMINA_RECHARGE_RATE;
                    staminaRechargeTimer = 0;
                }
            }
        }
    }
    
    //cringe ass mouse
    private void mouseUpdate() {
        Point2D playerPosition = entity.getCenter();
        Point2D mousePosition = getInput().getMousePositionWorld();

        if (!mousePosition.equals(lastMousePosition)) {
            Point2D vector = mousePosition.subtract(playerPosition);
            angle = Math.toDegrees(Math.atan2(vector.getY(), vector.getX()));
            entity.setRotation(angle + 90);

            lastMousePosition = mousePosition;
            
        }
    }
    
    public boolean isDead() {
        return isDead;
    }
 
    public void moveX(boolean isLeft) {

        double tempSpeed = speed;
        
        if (isSprinting) {
            tempSpeed *= 2;
        }

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

        if (isSprinting) {
            tempSpeed *= 2;
        }

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

    public void initClientInput(){

        clientInputs.addAction(new UserAction("Move Upwards"){
            protected void onAction(){
                moveY(false);
            }
            protected void onActionEnd() {
                stopMoving();
            }
        },KeyCode.W);

        clientInputs.addAction(new UserAction("Move Down"){
            protected void onAction(){
                moveY(true);
            }
            protected void onActionEnd() {
                stopMoving();
            }
        },KeyCode.S);

        clientInputs.addAction(new UserAction("Move Left"){
            protected void onAction(){
                moveX(true);   
            }
            protected void onActionEnd() {
                stopMoving();
            }
        },KeyCode.A);

        clientInputs.addAction(new UserAction("Move Right"){
            protected void onAction(){
                moveX(false);
            }
            protected void onActionEnd() {
                stopMoving();
            }
        },KeyCode.D);

        clientInputs.addAction(new UserAction("Reload"){
            protected void onActionBegin(){
                getCurrentWeapon().reload();
            }
        },KeyCode.R);

        clientInputs.addAction(new UserAction("Shoot") {
            protected void onActionBegin() {
               getCurrentWeapon().fire(entity);
               setShooting(true);
            }
            protected void onActionEnd() {
               setShooting(false);
               getCurrentWeapon().stopFiring();
            }
        }, MouseButton.PRIMARY);
        clientInputs.addAction(new UserAction("Switch Weapons") {
            protected void onActionBegin() {
                switchWeapon();
            }
        }, KeyCode.Q);
        clientInputs.addAction(new UserAction("Sprint") {
            @Override
            protected void onAction() {
                isSprinting = true;
            }
        
            @Override
            protected void onActionEnd() {
                isSprinting = false;
            }
        }, KeyCode.SPACE);
        // clientInputs.addAction(new UserAction("Interact") {
        //     protected void onActionBegin() {
        //         interactWithObject();
        //     }
        // }, KeyCode.F);
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

    public double getAngle() {
        return angle;
    }

    public void setSpeed(int originalSpeed) {
        this.originalSpeed = originalSpeed;
    }

    public void addWeapon(WeaponComponent weapon) {
        weapons.add(weapon);
    }
}
