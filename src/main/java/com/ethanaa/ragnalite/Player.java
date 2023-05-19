package com.ethanaa.ragnalite;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class Player {

    private static final Image IMAGE = new Image("sprites2.png");
    private static final Image IMAGE_BACK = new Image("sprites2_back.png");

    private TileNode tile;
    private Sprite sprite;
    private Orientation orientation;
    private Action action = Action.IDLE;

    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    public Player(TileNode tile, Orientation orientation, Rotate cameraRz) {
        this.tile = tile;
        this.orientation = orientation;

        this.sprite = new SpriteBuilder()
                .setTile(this.tile)
                .setCameraRotate(cameraRz)
                .setIdleAnimation(IMAGE, 0, 0,
                        1600 / 10, 1097 / 4, 39,
                        10, 2_000, -100)
                .setIdleAnimationBehind(IMAGE_BACK, 0, 0,
                        1600 / 10, 1102 / 4, 40,
                        10, 2_000, -100)
                .setOrientation(this.orientation)
                .setAction(this.action)
                .build();

        this.centerX.set(tile.getSceneX());
        this.centerY.set(tile.getSceneY());
    }

    public Sprite getSprite() {
        return sprite;
    }

    public double getCenterX() {
        return centerX.get();
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public double getCenterY() {
        return centerY.get();
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }
}
