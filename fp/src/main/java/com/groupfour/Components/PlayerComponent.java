package com.groupfour.Components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import com.groupfour.Weapons.BerettaM9;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {
    private boolean isDead = false;
    private int health;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private WeaponComponent currentWeapon;
    private double speed =2;
    private Input gameInput;
    private String name="test";


    public String getName(){
        return name;
    }

    public PlayerComponent(int initialHealth) {
        this.health = initialHealth;
        this.currentWeapon = new BerettaM9(false, null);
    }

    public void setGameInput(Input gameInput){
        this.gameInput = gameInput;
    }
    
    public Input getGameInput(){
        return gameInput;
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
}
    