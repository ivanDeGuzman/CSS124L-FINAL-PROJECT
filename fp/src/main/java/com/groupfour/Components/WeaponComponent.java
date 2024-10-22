package com.groupfour.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class WeaponComponent extends Component {
    private boolean isDead = false;
    private int health;
    private boolean isShooting = false;
    private double timeSinceLastShotP1 = 0;
    
    public WeaponComponent(int initialHealth) {
        this.health = initialHealth;
    }

    public int getHealth() {
        return health;
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
    