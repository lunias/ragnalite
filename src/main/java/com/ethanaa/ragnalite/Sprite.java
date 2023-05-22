package com.ethanaa.ragnalite;


import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

import java.util.Map;

public class Sprite extends ImageView {
    private Rotate cameraRotate;

    private final Map<Action, Map<Orientation, SpriteAnimation>> animationMap;

    private SpriteAnimation currentAnimation;

    private final ObjectProperty<Action> currentAction = new SimpleObjectProperty<>();
    private final ObjectProperty<Orientation> currentOrientation = new SimpleObjectProperty<>();
    private final ObjectProperty<TileNode> tile;

    public Sprite(SpriteBuilder spriteBuilder) {

        this.cameraRotate = spriteBuilder.getCameraRotate();
        this.animationMap = spriteBuilder.getAnimationMap();

        this.currentAction.addListener((observable, oldAction, newAction) -> {

            Map<Orientation, SpriteAnimation> orientationSpriteAnimationMap = animationMap.get(newAction);
            if (orientationSpriteAnimationMap == null) {
                return;
            }

            SpriteAnimation animation = orientationSpriteAnimationMap.get(currentOrientation.get());
            if (animation == null) {
                animation = orientationSpriteAnimationMap.get(Orientation.FORWARD);
                if (animation == null) {
                    return;
                }
            }

            startAnimation(animation);
        });

        this.currentOrientation.addListener((observableValue, oldOrientation, newOrientation) ->  {

            Map<Orientation, SpriteAnimation> orientationSpriteAnimationMap = animationMap.get(currentAction.get());
            if (orientationSpriteAnimationMap == null) {
                return;
            }

            SpriteAnimation animation = orientationSpriteAnimationMap.get(newOrientation);
            if (animation == null) {
                return;
            }

            startAnimation(animation);
        });

        this.currentAction.set(spriteBuilder.getAction());
        this.currentOrientation.set(spriteBuilder.getOrientation());
        this.tile = spriteBuilder.getTile();

        this.tile.addListener(((observable, oldTile, newTile) -> {
            if (oldTile.getY() < newTile.getY()) {
                setCurrentOrientation(Orientation.FORWARD);
            } else if (oldTile.getY() > newTile.getY()){
                setCurrentOrientation(Orientation.BACKWARD);
            }
            moveTo(newTile);
        }));

        this.cameraRotate.angleProperty().addListener((observableValue, oldAngle, newAngle) -> {

            double absoluteAngle = Math.abs(newAngle.doubleValue());

            /*
            if (absoluteAngle == 90 || absoluteAngle == 270) {
                this.currentOrientation.set(this.currentOrientation.get() == Orientation.FORWARD
                        ? Orientation.BACKWARD : Orientation.FORWARD);
            }
             */

        });
    }

    private void stopCurrentAnimation() {

        if (this.currentAnimation == null) return;

        this.currentAnimation.stop();
    }

    private void startAnimation(SpriteAnimation animation) {

        stopCurrentAnimation();

        ImageView animationImageView = animation.getImageView();

        setImage(animationImageView.getImage());
        setViewport(animationImageView.getViewport());
        setSmooth(animationImageView.isSmooth());
        setPreserveRatio(animationImageView.isPreserveRatio());
        setFitWidth(animationImageView.getFitWidth());
        setX(animationImageView.getX());
        setY(animationImageView.getY());
        getTransforms().clear();
        getTransforms().addAll(animationImageView.getTransforms());
        setTranslateZ(animationImageView.getTranslateZ());

        this.currentAnimation = new SpriteAnimation(this, animation);
        this.currentAnimation.setCycleCount(Animation.INDEFINITE);
        this.currentAnimation.play();
    }

    public void moveTo(TileNode tile) {

        double realWidth = this.getBoundsInParent().getWidth();
        double realHeight = this.getBoundsInParent().getHeight();

        final Rotate spriteCameraRotate = new Rotate(
                0,
                tile.getSceneX(),
                tile.getSceneY(),
                0,
                Rotate.Z_AXIS);
        spriteCameraRotate.angleProperty().bind(cameraRotate.angleProperty().multiply(-1.0));

        final Rotate spriteStandRotate = new Rotate(
                Main.CAMERA_ANGLE,
                tile.getSceneX(),
                tile.getSceneY(),
                tile.getSceneZ(),
                Rotate.X_AXIS);

        double xOffset = -(realWidth / 2.0);
        double yOffset = -(realHeight / 2.0 + (realHeight - 50.0));

        for (Map<Orientation, SpriteAnimation> orientationSpriteAnimationMap : animationMap.values()) {
            for (SpriteAnimation spriteAnimation : orientationSpriteAnimationMap.values()) {

                ImageView imageView = spriteAnimation.getImageView();
                imageView.setX(tile.getSceneX() + xOffset);
                imageView.setY(tile.getSceneY() + yOffset);
                imageView.getTransforms().clear();
                imageView.setTranslateZ(tile.getSceneZ() * -2);
                imageView.getTransforms().addAll(spriteCameraRotate, spriteStandRotate);
            }
        }

        setX(tile.getSceneX() + xOffset);
        setY(tile.getSceneY() + yOffset);
        getTransforms().clear();
        setTranslateZ(tile.getSceneZ() * -2);
        getTransforms().addAll(spriteCameraRotate, spriteStandRotate);
    }

    public TileNode getTile() {
        return tile.get();
    }

    public Rotate getCameraRotate() {
        return cameraRotate;
    }

    public void setCameraRotate(Rotate cameraRotate) {
        this.cameraRotate = cameraRotate;
    }

    public Action getCurrentAction() {
        return currentAction.get();
    }

    public ObjectProperty<Action> currentActionProperty() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction.set(currentAction);
    }

    public Orientation getCurrentOrientation() {
        return currentOrientation.get();
    }

    public ObjectProperty<Orientation> currentOrientationProperty() {
        return currentOrientation;
    }

    public void setCurrentOrientation(Orientation currentOrientation) {
        this.currentOrientation.set(currentOrientation);
    }

}
