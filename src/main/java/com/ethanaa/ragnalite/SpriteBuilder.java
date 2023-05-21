package com.ethanaa.ragnalite;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class SpriteBuilder {

    private ObjectProperty<TileNode> tile = new SimpleObjectProperty<>();
    private Rotate cameraRotate;
    private Orientation orientation;
    private Action action;

    private boolean isPlayer = true;

    final private Map<Action, Map<Orientation, SpriteAnimation>> animationMap = new HashMap<>();

    public Sprite build() {
        return new Sprite(this);
    }

    public SpriteBuilder setTile(ObjectProperty<TileNode> tile) {
        this.tile = tile;
        return this;
    }

    public SpriteBuilder setCameraRotate(Rotate cameraRotate) {
        this.cameraRotate = cameraRotate;
        return this;
    }

    private SpriteAnimation createAnimation(Image image, int offsetX, int offsetY, int width, int height, int count, int columns, int duration, int translateZ) {

        final ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);

        double realWidth = imageView.getBoundsInParent().getWidth();
        double realHeight = imageView.getBoundsInParent().getHeight();

        System.out.println("Real height: " + realHeight);
        System.out.println("Real width: " + realWidth);

        imageView.setX(tile.get().getSceneX());
        imageView.setY(tile.get().getSceneY() - (realHeight - 50.0));
        System.out.println("Sprite Builder: putting sprite @ " + tile.get().getSceneX() + ", " + tile.get().getSceneY());

        imageView.setPickOnBounds(false);

        final Rotate spriteCameraRotate = new Rotate(
                0,
                imageView.getX() + realWidth / 2.0,
                imageView.getY() + realHeight / 2.0,
                0,
                Rotate.Z_AXIS);
        spriteCameraRotate.angleProperty().bind(cameraRotate.angleProperty().multiply(-1.0));

        final Rotate spriteStandRotate = new Rotate(
                Main.CAMERA_ANGLE,
                imageView.getX() + realWidth / 2.0,
                imageView.getY() + realHeight / 2.0,
                0,
                Rotate.X_AXIS);

        System.out.println("Tile Z: " + tile.get().getSceneZ());

        imageView.getTransforms().addAll(spriteCameraRotate, spriteStandRotate);

        imageView.setTranslateZ(tile.get().getSceneZ() * -2 - (realHeight - 50.0));

        return new SpriteAnimation(
                imageView,
                Duration.millis(duration),
                count, columns,
                offsetX, offsetY,
                width, height);
    }

    public SpriteBuilder setIdleAnimation(Image image, int offsetX, int offsetY, int width, int height,
                                          int count, int columns, int duration, int translateZ) {

        SpriteAnimation animation = createAnimation(image, offsetX, offsetY,
                width, height, count, columns, duration, translateZ);

        this.animationMap.computeIfAbsent(Action.IDLE, k -> new HashMap<>())
                .put(Orientation.FORWARD, animation);

        return this;
    }

    public SpriteBuilder setIdleAnimationBehind(Image image, int offsetX, int offsetY, int width, int height,
                                                int count, int columns, int duration, int translateZ) {

        SpriteAnimation animation = createAnimation(image, offsetX, offsetY,
                width, height, count, columns, duration, translateZ);

        this.animationMap.computeIfAbsent(Action.IDLE, k -> new HashMap<>())
                .put(Orientation.BACKWARD, animation);

        return this;
    }

    public SpriteBuilder setOrientation(Orientation orientation) {

        this.orientation = orientation;
        return this;
    }

    public SpriteBuilder setAction(Action action) {

        this.action = action;
        return this;
    }

    public SpriteBuilder isPlayer(boolean isPlayer) {

        this.isPlayer = isPlayer;
        return this;
    }

    public ObjectProperty<TileNode> getTile() {
        return tile;
    }

    public Rotate getCameraRotate() {
        return cameraRotate;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Action getAction() {
        return action;
    }

    public Map<Action, Map<Orientation, SpriteAnimation>> getAnimationMap() {
        return animationMap;
    }

    public boolean isPlayer() {
        return isPlayer;
    }
}
