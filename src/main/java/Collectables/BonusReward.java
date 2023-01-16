package Collectables;

import main.CollectablePlacer;
import main.HUD;
import Helpers.DefaultProperties;
import main.Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Bonus Reward Spawns randomly and disappears after a set amount of time
 * Upon being collected by the player, gives points.
 * @author Pieter Ruslim
 */
public class BonusReward extends Collectable{
    /**
     * Returns instance of BonusReward
     * @param mapX Horizontal Position of BonusReward relative to map
     * @param mapY Vertical Position of BonusReward relative to map
     */
    public BonusReward(int mapX, int mapY, Panel panel){
        this.mapX = mapX;
        this.mapY = mapY;
        this.collectableHitbox = new Rectangle(0, 0, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize);
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/CollectablesImages/BonusReward.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increases score inside hud by 100, and displays a message on screen.
     * Returns true to indicate that this instance should be removed
     * @param hud where Bonus Reward collected message will be displayed
     * @param collectablePlacer
     * @return
     */
    @Override
    public boolean collect(HUD hud, CollectablePlacer collectablePlacer) {
        hud.setScore(hud.getScore() + DefaultProperties.BonusReward);
        hud.displayMessage(24, "Bonus Reward collected");
        System.out.println("Bonus Reward collected");
        return true;
    }
}