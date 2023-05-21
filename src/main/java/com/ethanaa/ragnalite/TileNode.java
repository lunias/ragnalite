package com.ethanaa.ragnalite;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;

import java.util.Random;

public class TileNode extends StackPane {

    private static final int HEIGHT = 50;
    private static final int WIDTH = 50;
    private static final int DEPTH = 25;

    private int x;
    private int y;
    private int z;

    private TileType tileType;
    private Biome biome;
    private Box tile;

    private Random random = new Random();

    public TileNode(int x, int y, int z, TileType tileType, Biome biome) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileType = tileType;
        this.biome = biome;

        this.setPickOnBounds(false);

        double translateX = x * HEIGHT;
        double translateY = y * WIDTH;
        double translateZ = z * DEPTH;

        this.tile = new Box(WIDTH, HEIGHT, DEPTH);
        this.tile.setMaterial(new PhongMaterial(this.tileType.getColor()));
        this.tile.getTransforms().add(new Translate(translateX, translateY, translateZ));
        this.tile.setPickOnBounds(true);

        this.tile.setOnMouseClicked(me -> {
            if (me.getButton() == MouseButton.PRIMARY) {
                System.out.println("Clicked tile (" + x + ", " + y + ", " + z +" ) @ Scene (" + this.getSceneX() + ", " + this.getSceneY() + ", " + this.getSceneZ() + ")");
            }
        });

        getChildren().add(tile);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public double getSceneX() {
        return this.tile.getBoundsInParent().getCenterX();
    }

    public double getSceneY() {
        return this.tile.getBoundsInParent().getCenterY();
    }

    public double getSceneZ() {
        return this.tile.getBoundsInParent().getMaxZ();
    }

    @Override
    public String toString() {
        return "TileNode{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", tileType=" + tileType +
                ", biome=" + biome +
                ", sceneX=" + getSceneX() +
                ", sceneY=" + getSceneY() +
                ", sceneZ=" + getSceneZ() +
                '}';
    }
}
