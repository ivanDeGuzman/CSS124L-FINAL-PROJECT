package com.groupfour.Objects;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Connection;
import com.groupfour.Components.ObjectsComponent;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.util.Duration;

public class Microwave extends ObjectsComponent {
    private boolean canInteract = true; 
    private int healAmount = 20; 
    private Duration cooldownDuration = Duration.minutes(1);

    
    public Microwave(boolean isServer, Connection<Bundle> connection) {
        super("Microwave", isServer, connection);
    }

    
    public void interact() {
        if (canInteract) { 
            FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> { 
                player.getComponent(PlayerComponent.class).setHealth(player.getComponent(PlayerComponent.class).getHealth() + healAmount);
                if (player.getComponent(PlayerComponent.class).getHealth() > 100)
                    player.getComponent(PlayerComponent.class).setHealth(100);  
            }); 
            System.out.println("Microwave: Healed player by " + healAmount); 
            canInteract = false; 
            FXGL.runOnce(() -> canInteract = true, cooldownDuration); 
        } else { 
            System.out.println("Microwave is cooling down. Please wait."); 
        }
    }

}
