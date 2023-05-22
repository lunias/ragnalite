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
    }

    public void init(Rotate cameraRz) {
        this.sprite = new SpriteBuilder()
                .setTile(getTileProperty())
                .setCameraRotate(cameraRz)
                .setIdleAnimation(IMAGE, 0, 0,
                        480 / 4, 124, 4,
                        4, 1_000, 0)
                .setIdleAnimationBehind(IMAGE, 0, 0,
                        480 / 4, 124, 4,
                        4, 1_000, 0)
                .setOrientation(this.orientation)
                .setAction(this.action)
                .isPlayer(false)
                .build();

        this.centerX.set(getTileProperty().get().getSceneX());
        this.centerY.set(getTileProperty().get().getSceneY());
    }

    public void setTile(TileNode tile) {
        this.tile.set(tile);
        this.centerX.set(tile.getSceneX());
        this.centerY.set(tile.getSceneY());
    }

    public ObjectProperty<TileNode> getTileProperty() {
        return tile;
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
