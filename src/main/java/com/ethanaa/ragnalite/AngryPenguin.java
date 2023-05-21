package com.ethanaa.ragnalite;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class AngryPenguin {
    private static final Image IMAGE = new Image("angry_penguin_idle_front.png");
    private final ObjectProperty<TileNode> tile = new SimpleObjectProperty<>();
    private Sprite sprite;
    private Orientation orientation;
    private Action action = Action.IDLE;
    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    public AngryPenguin(TileNode tile, Orientation orientation, Rotate cameraRz) {
        this.setTile(tile);
        this.orientation = orientation;

        this.sprite = new SpriteBuilder()
                .setTile(this.tile)
                .setCameraRotate(cameraRz)
                .setIdleAnimation(IMAGE, 0, 0,
                        480 / 4, 124, 4,
                        4, 1_000, -25)
                .setIdleAnimationBehind(IMAGE, 0, 0,
                        1247 / 11, 866 / 7, 4,
                        12, 2_000, -100)
                .setOrientation(this.orientation)
                .setAction(this.action)
                .isPlayer(false)
                .build();

        this.centerX.set(tile.getSceneX());
        this.centerY.set(tile.getSceneY());
    }

    public void setTile(TileNode tile) {
        this.tile.set(tile);
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
