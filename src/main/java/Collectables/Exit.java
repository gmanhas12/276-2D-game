package Collectables;

import Helpers.DefaultProperties;
import main.CollectablePlacer;
import main.HUD;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @author Tanzil Sarker
 * Exit will appear after all rewards are collected, and will end the level when collided with
 */
public class Exit extends Collectable{
    public Exit(int mapX, int mapY){
        this.mapX = mapX;
        this.mapY = mapY;
        this.visible = false;
        this.collectableHitbox = new Rectangle(0, 23, 25, 5);
        try{
            sprite= ImageIO.read(getClass().getResourceAsStream("/MapTiles/exit.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Sends messages to load new map and display text on screen.
     * @param hud an instance of HUD class
     * @param collectablePlacer an instance of CollectablePlacer class
     * @return false
     */
    @Override
    public boolean collect(HUD hud, CollectablePlacer collectablePlacer) {
        if(collectablePlacer.normalRewardCount == collectablePlacer.totalRewards) {
            // If it's the last map or the overall score of the player is 0 or less, its game over!
            if (hud.mapNumber >= DefaultProperties.NumberOfMaps || hud.getScore() <= 0) {
                System.out.println("Game Over!");
                hud.displayMessage(24, "Game Over");
                hud.gameOver = true;
                hud.mapexit = false;
            } else {
                System.out.println("Exit Reached");
                hud.displayMessage(5, "Exit Reached");
                hud.mapexit = true;
            }

            return true;
        }
        return false;
    }
}
