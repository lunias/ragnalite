package com.ethanaa.ragnalite;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class SpriteBuilder {

    private TileNode tile;
    private Rotate cameraRotate;
    private Orientation orientation;
    private Action action;

    final private Map<Action, Map<Orientation, SpriteAnimation>> animationMap = new HashMap<>();

    public Sprite build() {
        return new Sprite(this);
    }

    public SpriteBuilder setTile(TileNode tile) {
        this.tile = tile;
        return this;
    }

    public SpriteBuilder setCameraRotate(Rotate cameraRotate) {
        this.cameraRotate = cameraRotate;
        return this;
    }

    private SpriteAnimation createAnimation(Image image, int offsetX, int offsetY, int width, int height, int count, int columns, int duration, int translateZ) {

        final Rotate spriteStandRotate = new Rotate(90 - 63.44, tile.getSceneX(), tile.getSceneY(), tile.getSceneZ(), Rotate.X_AXIS);
        final Rotate spriteCameraRotate = new Rotate(0, tile.getSceneX(), tile.getSceneY(), tile.getSceneZ(), Rotate.Y_AXIS);

        spriteCameraRotate.angleProperty().bind(cameraRotate.angleProperty().multiply(-1.0));

        final ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);
        imageView.setX(tile.getSceneX());
        imageView.setY(tile.getSceneY() - 20.0); // TODO I think -20 is required because of how we rotate the sprite away from the camera, the sprite is floating above the ground
        imageView.getTransforms().addAll(spriteStandRotate, spriteCameraRotate);
        imageView.setTranslateZ(tile.getSceneZ() + translateZ);
        imageView.setPickOnBounds(false);

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

    public TileNode getTile() {
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
}
