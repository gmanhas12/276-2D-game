package Collectables;

import Helpers.DefaultProperties;
import main.CollectablePlacer;
import main.HUD;
import main.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Super class of any collectable object
 * @author Pieter Ruslim
 */
public class Collectable {
    public BufferedImage sprite;
    public boolean visible = true;
    public int mapX, mapY;
    public Rectangle collectableHitbox;

    /**
     * Displays sprite on the panel at its position relative to the player
     * @param graphics2D Graphics2D
     * @param panel Main panel
     */
    public void render(Graphics2D graphics2D, Panel panel){
        // Uses position on map to calculate position on window.
        if (visible) {
            int windowX = mapX - panel.player.mapX + panel.player.getPlayerWindowPositionX();
            int windowY = mapY - panel.player.mapY + panel.player.getPlayerWindowPositionY();

            //draws collectable on the given X and Y position
            graphics2D.drawImage(sprite, windowX, windowY, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize, null);
        }
    }

    /**
     * Defines behaviour of object when collided with.
     * Returns true if instance of collectable should be removed
     * @param hud where UI changes will be made
     * @param collectablePlacer
     * @return
     */
    public boolean collect(HUD hud, CollectablePlacer collectablePlacer){

        return true;
    }

}
