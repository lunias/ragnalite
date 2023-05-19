package com.ethanaa.ragnalite;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class WorldRegion extends Group {

    private static final int REGION_WIDTH = 50;
    private static final int REGION_HEIGHT = 50;
    private Zone zone;
    private TileNode[][] tileNodes;

    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    private Transform rotation = new Rotate();

    public WorldRegion() {
        this.zone = new Zone(
                REGION_WIDTH,
                REGION_HEIGHT,
                Biome.DESERT);

        getTransforms().addAll(rotation);

        initTileNodes(this.zone);
    }

    private void initTileNodes(Zone zone) {
        TileNode[][] tileNodes = new TileNode[REGION_WIDTH][REGION_HEIGHT];
        for (int i = 0; i < tileNodes.length; i++) {
            for (int j = 0; j < tileNodes[0].length; j++) {
                tileNodes[i][j] = new TileNode(i, j, 0, zone.getTiles()[i][j], zone.getBiome());
                getChildren().add(tileNodes[i][j]);
            }
        }
        this.tileNodes = tileNodes;
        this.centerX.set(this.tileNodes[REGION_WIDTH / 2][REGION_HEIGHT / 2].getSceneX());
        this.centerY.set(this.tileNodes[REGION_WIDTH / 2][REGION_HEIGHT / 2].getSceneY());
    }

    public TileNode getTileNode(int x, int y) {
        return tileNodes[x][y];
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

    public void reset() {
        rotation = new Rotate();
        getTransforms().set(0, rotation);
    }

    public void rx(double angle) {
        Point3D axis = new Point3D(rotation.getMxx(), rotation.getMxy(), rotation.getMxz());
        rotation = rotation.createConcatenation(new Rotate(angle, axis));
        getTransforms().set(0, rotation);
    }

    public void ry(double angle) {
        Point3D axis = new Point3D(rotation.getMyx(), rotation.getMyy(), rotation.getMyz());
        rotation = rotation.createConcatenation(new Rotate(angle, axis));
        getTransforms().set(0, rotation);
    }

    public void rz(double angle) {
        Point3D axis = new Point3D(rotation.getMzx(), rotation.getMzy(), rotation.getMzz());
        System.out.println("Axis: " + axis.getX() + ", " + axis.getY() + ", " + axis.getZ());
        rotation = rotation.createConcatenation(new Rotate(angle, axis));
        getTransforms().set(0, rotation);
    }
}
