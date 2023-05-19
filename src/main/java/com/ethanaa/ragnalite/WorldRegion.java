package com.ethanaa.ragnalite;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

public class WorldRegion extends Group {

    private static final int REGION_WIDTH = 100;
    private static final int REGION_HEIGHT = 100;
    private Zone zone;
    private TileNode[][] tileNodes;

    private final DoubleProperty centerX = new SimpleDoubleProperty();
    private final DoubleProperty centerY = new SimpleDoubleProperty();

    public WorldRegion() {
        this.zone = new Zone(
                REGION_WIDTH,
                REGION_HEIGHT,
                Biome.DESERT);

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
}
