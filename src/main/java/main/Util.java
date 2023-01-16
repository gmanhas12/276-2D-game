package main;

import Helpers.DefaultProperties;

/**
 * For using helper functions that rely on the data from the Panel class
 * @author  Tanzil Sarker
 */
public class Util {
    public Panel panel;

    public Util(Panel panel){
        this.panel = panel;
    }

    /**
     * Checks whether a particular tile in the matrix is grass or not
     * @param x int
     * @param y int
     * @return boolean
     */
    public boolean isGrass(int x, int y) {
        return this.panel.backgroundController.mapTileMatrixReader[x][y] == DefaultProperties.Traversable;
    }
}
