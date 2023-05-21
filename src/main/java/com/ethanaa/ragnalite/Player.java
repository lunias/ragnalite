package com.ethanaa.ragnalite;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public class Player {

    private static final Image IMAGE = new Image("sprites2.png");
    private static final Image IMAGE_BACK = new Image("sprites2_back.png");

    private final ObjectProperty<TileNode> tile = new SimpleObjectProperty<>();
    private Sprite sprite;
    private Orientation orientation;
    private Action action = Action.IDLE;

    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    public Player(TileNode tile, Orientation orientation, Rotate cameraRz) {
        this.tile.set(tile);
        this.orientation = orientation;

        this.sprite = new SpriteBuilder()
                .setTile(this.tile)
                .setCameraRotate(cameraRz)
                .setIdleAnimation(IMAGE, 0, 0,
                        1600 / 10, 1097 / 4, 39,
                        10, 2_000, -25 - 100)
                .setIdleAnimationBehind(IMAGE_BACK, 0, 0,
                        1600 / 10, 1102 / 4, 40,
                        10, 2_000, -25 - 100)
                .setOrientation(this.orientation)
                .setAction(this.action)
                .build();

        this.centerX.set(tile.getSceneX());
        this.centerY.set(tile.getSceneY());
    }

    public void setTile(TileNode tile) {
        this.centerX.set(tile.getSceneX());
        this.centerY.set(tile.getSceneY());
        this.tile.set(tile);
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

    public void moveNorth(WorldRegion worldRegion) {
        TileNode target = worldRegion.getTileNode(this.tile.get().getX(), this.tile.get().getY() - 1);
        System.out.println("Target tile: " + target);
        if (target == null) {
            return;
        }
        setTile(target);
    }

    public void moveWest(WorldRegion worldRegion) {
        TileNode target = worldRegion.getTileNode(this.tile.get().getX() - 1, this.tile.get().getY());
        System.out.println("Target tile: " + target);
        if (target == null) {
            return;
        }
        setTile(target);
    }

    public void moveSouth(WorldRegion worldRegion) {
        TileNode target = worldRegion.getTileNode(this.tile.get().getX(), this.tile.get().getY() + 1);
        System.out.println("Target tile: " + target);
        if (target == null) {
            return;
        }
        setTile(target);
    }

    public void moveEast(WorldRegion worldRegion) {
        TileNode target = worldRegion.getTileNode(this.tile.get().getX() + 1, this.tile.get().getY());
        System.out.println("Target tile: " + target);
        if (target == null) {
            return;
        }
        setTile(target);
    }
}
