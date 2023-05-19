package com.ethanaa;

import org.codetome.hexameter.core.api.contract.SatelliteData;


public class GridData implements SatelliteData {

    private int height = 0;

    public GridData(int height) {

        this.height = height;
    }

    public GridData() {
        //
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public void setPassable(boolean passable) {

    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public void setOpaque(boolean opaque) {

    }

    @Override
    public double getMovementCost() {
        return 0;
    }

    @Override
    public void setMovementCost(double movementCost) {

    }
}
