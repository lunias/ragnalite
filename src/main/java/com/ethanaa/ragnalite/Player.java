package com.ethanaa.ragnalite;

import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Player {

    private TileNode tile;
    private Sprite sprite;
    private Orientation orientation;
    private Action action = Action.IDLE;
    private PerspectiveCamera camera;

    private Rotate cameraRx;
    private Rotate cameraRy;
    private Rotate cameraRz;

    private Translate cameraX;
    private Translate cameraY;
    private Translate cameraZ;

    private static final Image IMAGE = new Image("sprites2.png");
    private static final Image IMAGE_BACK = new Image("sprites2_back.png");

    public Player(TileNode tile, Orientation orientation) {
        this.tile = tile;
        this.orientation = orientation;
        this.camera = new PerspectiveCamera(false);

        cameraRx = new Rotate(0, Rotate.X_AXIS);
        cameraRx.setPivotX(tile.getSceneX());
        cameraRx.setPivotY(tile.getSceneY());
        cameraRx.setPivotZ(tile.getSceneZ());

        cameraRy = new Rotate(0, Rotate.Y_AXIS);
        cameraRy.setPivotX(tile.getSceneX());
        cameraRy.setPivotY(tile.getSceneY());
        cameraRy.setPivotZ(tile.getSceneZ());

        cameraRz = new Rotate(0, Rotate.Z_AXIS);
        cameraRz.setPivotX(tile.getSceneX());
        cameraRz.setPivotY(tile.getSceneY());
        cameraRz.setPivotZ(tile.getSceneZ());

        cameraX = new Translate(0, 0, 0);
        cameraY = new Translate(0, 0, 0);
        cameraZ = new Translate(0, 0, -500);

        this.camera.getTransforms().addAll(cameraRx, cameraRy, cameraRz, cameraX, cameraY, cameraZ);

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
    }

    public Sprite getSprite() {
        return sprite;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public Rotate getCameraRx() {
        return cameraRx;
    }

    public void setCameraRx(Rotate cameraRx) {
        this.cameraRx = cameraRx;
    }

    public Rotate getCameraRy() {
        return cameraRy;
    }

    public void setCameraRy(Rotate cameraRy) {
        this.cameraRy = cameraRy;
    }

    public Rotate getCameraRz() {
        return cameraRz;
    }

    public void setCameraRz(Rotate cameraRz) {
        this.cameraRz = cameraRz;
    }

    public Translate getCameraX() {
        return cameraX;
    }

    public void setCameraX(Translate cameraX) {
        this.cameraX = cameraX;
    }

    public Translate getCameraY() {
        return cameraY;
    }

    public void setCameraY(Translate cameraY) {
        this.cameraY = cameraY;
    }

    public Translate getCameraZ() {
        return cameraZ;
    }

    public void setCameraZ(Translate cameraZ) {
        this.cameraZ = cameraZ;
    }
}
