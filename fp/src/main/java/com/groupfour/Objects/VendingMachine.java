package com.groupfour.Objects;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.UI.ObjectsUI;
import com.groupfour.mygame.EntityTypes.EntityType;
import javafx.util.Duration;

public class VendingMachine extends Component {
    private int max = 6;
    private int min = 1;
    private int range = max - min + 1;
    private int rand;
    private boolean canInteract = true;
    private ObjectsUI ui = new ObjectsUI();

    
    public void interact() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
   
            if (canInteract) {
                rand = (int) (Math.random() * range) + min;
                buffs(rand);
                canInteract = false;
                FXGL.runOnce(() -> canInteract = true, Duration.minutes(1));
            } else {
                ui.vmNoInteract();
            }
        });
    }

    public void buffs(int rand) {
        switch (1) {
            case 1:
                // Energy Drink - increase speed of player for 1 min
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).increaseSpeed(1.2, Duration.minutes(1));
                });
                ui.ammoCansUI("energy");
                break;
            case 2:
                // Fizzy Drink - increase attack 25% of player for 1 min
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).increaseWeaponDamage(1.25, Duration.minutes(1));
                });
                ui.ammoCansUI("fizzy");
                break;
            case 3:
                // Toxic Sludge - increase attack 100% but decrease health to 1
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).increaseWeaponDamage(2, Duration.minutes(1));
                    player.getComponent(PlayerComponent.class).setHealth(1);
                });
                System.out.println("Sludge");
                break;
            case 4:
                //Ice Cold - increase player def 25% for 1 min
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).setReducedDamage(0.75, Duration.minutes(1));
                });
                System.out.println("Ice Cold");
                break;
            case 5:
                //Atomic Soda - boost all stats by 15% for 2 min
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).increaseWeaponDamage(1.15, Duration.minutes(2));
                    player.getComponent(PlayerComponent.class).setReducedDamage(0.85, Duration.minutes(2));
                    player.getComponent(PlayerComponent.class).increaseSpeed(1.15, Duration.minutes(1));
                });
                System.out.println("Atomic");
                break;
            case 6:
                //Nuke Soda - boost all stats by 40% for 2 min
                FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                    player.getComponent(PlayerComponent.class).increaseWeaponDamage(1.4, Duration.minutes(2));
                    player.getComponent(PlayerComponent.class).setReducedDamage(0.6, Duration.minutes(2));
                    player.getComponent(PlayerComponent.class).increaseSpeed(1.4, Duration.minutes(1));
                });
                System.out.println("Nuke");
                break;
        }
    }
}
