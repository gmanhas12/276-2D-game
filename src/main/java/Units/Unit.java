package Units;

import Helpers.DIRECTION;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Units are objects on the game that are defined by the ability to move.
 */
public class Unit {
    public int mapX;
    public int mapY;
    public int matrixRow;
    public int matrixCol;
    public int movementSpeed; //pixels per frame
    public int animationCounter;
    public int spriteNum;
    public final int framesPerAnimation = 5;

    //Images, sprites alternate between 1 and 2
    // player solid area
    public Rectangle hitBox;
    public BufferedImage imageUp1, imageUp2;
    public BufferedImage imageDown1, imageDown2;
    public BufferedImage imageLeft1, imageLeft2;
    public BufferedImage imageRight1, imageRight2;
    public boolean unitCollision = false;

    public DIRECTION unitFacing;

    /**
     * Gets the sprite that corresponds to a Units direction and time
     * @param direction where unit is facing
     * @return BufferedImage sprite of unit facing the specified direction.
     */
    public BufferedImage getSprite(DIRECTION direction){
        BufferedImage sprite = switch (direction) {
            case UP -> (spriteNum == 1) ? imageUp1 : imageUp2;
            case DOWN -> (spriteNum == 1) ? imageDown1 : imageDown2;
            case LEFT -> (spriteNum == 1) ? imageLeft1 : imageLeft2;
            case RIGHT -> (spriteNum == 1) ? imageRight1 : imageRight2;
        };
        return sprite;

    }

    /**
     * Gets called everytime a unit is rendered, alternates the sprites so that the unit looks animated.
     */
    public void countAnimations(){
        animationCounter++;
        if (animationCounter > framesPerAnimation) {
            if (spriteNum == 1)
                spriteNum = 2;
            else if (spriteNum == 2)
                spriteNum = 1;
            animationCounter = 0;
        }
    }
}
