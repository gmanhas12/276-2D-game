package Collectables;

import main.CollectablePlacer;
import main.HUD;
import Helpers.DefaultProperties;
import main.Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Normal Rewards give points and disappears upon colliding with player
 * @author Pieter Ruslim
 */
public class NormalReward extends Collectable{
    /**
     * Returns instance of NormalReward
     * @param mapX Horizontal Position of NormalReward relative to map
     * @param mapY Vertical Position of NormalReward relative to map
     */
    public NormalReward(int mapX, int mapY, Panel panel ){
        this.mapX = mapX;
        this.mapY = mapY;
        this.collectableHitbox = new Rectangle(0, 0, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize);
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/CollectablesImages/NormalReward.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Increases score when instance is collided with.
     * Returns true to indicate that instance should be removed.
     * @param hud Where reward count will be updated
     * @param collectablePlacer where normalRewardCount is stored
     * @return boolean
     */
    @Override
    public boolean collect(HUD hud, CollectablePlacer collectablePlacer) {
        collectablePlacer.normalRewardCount += 1;
        hud.setScore(hud.getScore() + DefaultProperties.RegularReward);
        hud.setRewardIndicator("" + collectablePlacer.normalRewardCount + " / " + collectablePlacer.totalRewards);

        System.out.println("Normal Rewards collected: " + collectablePlacer.normalRewardCount);
        return true;
    }
}
