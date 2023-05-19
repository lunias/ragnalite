package com.ethanaa.ragnalite;

import javafx.scene.paint.Color;

public enum TileType {
    LAND(true, Color.GREEN),
    WALL(false, Color.DARKSLATEGRAY),
    HOLE(false, Color.BLACK),
    WATER(false, Color.LIGHTSKYBLUE);

    private boolean passable;
    private Color color;

    TileType(boolean passable, Color color) {
        this.passable = passable;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean isPassable() {
        return passable;
    }
}
