package com.groupfour.Objects;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.groupfour.Components.PlayerComponent;
import com.groupfour.UI.ObjectsUI;
import com.groupfour.mygame.EntityTypes.EntityType;

import javafx.util.Duration;

public class Microwave extends Component {
    private ObjectsUI objectsUI;
    private boolean canInteract = true; 
    private int healAmount = 20; 
    private Duration cooldownDuration = Duration.minutes(1);

    public Microwave(ObjectsUI objectsUI) {
        this.objectsUI = objectsUI;
    }

    public void interact() {
        if (canInteract) { 
            FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
                objectsUI.mcInteract(); 
                player.getComponent(PlayerComponent.class).setHealth(player.getComponent(PlayerComponent.class).getHealth() + healAmount);
                if (player.getComponent(PlayerComponent.class).getHealth() > 100)
                    player.getComponent(PlayerComponent.class).setHealth(100);  
            }); 
            System.out.println("Microwave: Healed player by " + healAmount); 
            canInteract = false; 
            FXGL.runOnce(() -> canInteract = true, cooldownDuration); 
        } else { 
            objectsUI.mcNoInteract();
        }
    }

}
