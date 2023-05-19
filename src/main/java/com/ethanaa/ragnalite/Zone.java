package com.ethanaa.ragnalite;

import java.util.Random;

public class Zone {

    private int width;
    private int height;
    private Biome biome;
    private TileType[][] tiles;

    public Zone(int width, int height, Biome biome) {
        this.width = width;
        this.height = height;
        this.biome = biome;
        this.tiles = new TileType[width][height];
        initTiles();
    }

    private void initTiles() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.tiles[i][j] = TileType.values()[new Random().nextInt(TileType.values().length)];
            }
        }
    }

    public TileType[][] getTiles() {
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Biome getBiome() {
        return biome;
    }
}
