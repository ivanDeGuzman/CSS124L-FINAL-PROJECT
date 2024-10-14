package com.groupfour.mygame;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class App extends GameApplication {
    private Entity player;
    private Entity follower, follower2, follower3;
    private AStarGrid grid;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Zombie Game");
        settings.setVersion("1.0");
    }

//a* pathfinding test
    // @Override
    // protected void initGame() {
    //     grid = new AStarGrid(1280 / 40, 720 / 40);

    //     player = FXGL.entityBuilder()
    //             .at(100, 100)
    //             .viewWithBBox(new Polygon(0, 0, 40, 0, 20, 40))
    //             .buildAndAttach();

    //     follower = FXGL.entityBuilder()
    //             .at(200, 200)
    //             .viewWithBBox(new Rectangle(40, 40, Color.BLUE))
    //             .with(new CellMoveComponent(40, 40, 150))
    //             .with(new AStarMoveComponent(grid))
    //             .buildAndAttach();
        
    //      follower2 = FXGL.entityBuilder()
    //             .at(400, 300)
    //             .viewWithBBox(new Rectangle(40, 40, Color.RED))
    //             .with(new CellMoveComponent(40, 40, 150))
    //             .with(new AStarMoveComponent(grid))
    //             .buildAndAttach();
        
    //     follower3 = FXGL.entityBuilder()
    //             .at(300, 200)
    //             .viewWithBBox(new Rectangle(40, 40, Color.GREEN))
    //             .with(new CellMoveComponent(40, 40, 150))
    //             .with(new AStarMoveComponent(grid))
    //             .buildAndAttach();

    //     FXGL.onKey(KeyCode.W, () -> player.getComponent(TransformComponent.class).translateY(-3));
    //     FXGL.onKey(KeyCode.S, () -> player.getComponent(TransformComponent.class).translateY(3));
    //     FXGL.onKey(KeyCode.A, () -> player.getComponent(TransformComponent.class).translateX(-3));
    //     FXGL.onKey(KeyCode.D, () -> player.getComponent(TransformComponent.class).translateX(3));
    //     FXGL.run(() -> updateFollower(), Duration.seconds(1));
    // }

    // private void updateFollower() {

    //     int playerX = (int) player.getX() / 40;
    //     int playerY = (int) player.getY() / 40;

    //     follower.getComponent(AStarMoveComponent.class).moveToCell(playerX, playerY);
    //     follower2.getComponent(AStarMoveComponent.class).moveToCell(playerX, playerY);
    //     follower3.getComponent(AStarMoveComponent.class).moveToCell(playerX, playerY);
    // }

    

    public static void main(String[] args) {
        launch(args);
    }
    
}
