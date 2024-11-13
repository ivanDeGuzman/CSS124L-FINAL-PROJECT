package com.groupfour.Factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ParticleFactory {

    public static ParticleEmitter createBloodEmitter() {
        ParticleEmitter emitter = ParticleEmitters.newExplosionEmitter(5); 

        emitter.setStartColor(Color.DARKRED); 
        emitter.setEndColor(Color.BLACK);     
        emitter.setSize(3, 5);                    
        emitter.setExpireFunction(i -> Duration.seconds(FXGL.random(0.3, 1.2)));
        emitter.setVelocityFunction(i -> new Point2D(FXGL.random(-15, 15), FXGL.random(-15, 15)));

        return emitter;
    }
}
