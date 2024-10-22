package com.groupfour.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;

public class PlayerComponent extends Component {
    private boolean isDead = false;
    private int health;
    private boolean shooting = false;
    private double timeSinceLastShot = 0;
    private WeaponComponent weapon;
    private double speed =2;
    private Input gameInput;
    private String name="test";


    public String getName(){
        return name;
    }

    public PlayerComponent(int initialHealth) {
        this.health = initialHealth;
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

    private void onDeath() {
        isDead = true;
        FXGL.getDialogService().showMessageBox("You Died! Back to Main Menu?", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }

    public boolean isDead() {
        return isDead;
    }
}
    