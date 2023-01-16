package Collectables;

import Helpers.DefaultProperties;
import main.CollectablePlacer;
import main.HUD;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * Traps decrease score when stepped on
 * @author Mudassir Noor
 */
public class Trap extends Collectable {

    public Trap(int mapX, int mapY){
        this.mapX = mapX;
        this.mapY = mapY;
        this.collectableHitbox = new Rectangle(0, 0, 15, 15);
        try {
            sprite = ImageIO.read(getClass().getResourceAsStream("/CollectablesImages/trap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reduces score in hud by 100
     * returns True to indicate that this instance should be removed
     * @param hud
     * @param collectablePlacer
     * @return boolean
     */
    @Override
    public boolean collect(HUD hud, CollectablePlacer collectablePlacer) {
        hud.setScore(hud.getScore() - DefaultProperties.ScoreDeduction);
        hud.displayMessage(30, "Stepped on a trap!");
        return true;
    }
}
