package Units;
import Helpers.DIRECTION;
import Helpers.DefaultProperties;
import main.KeyListen;
import main.Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Implements player object
 * Handles player position and movement, as well as rendering
*/
public class Player extends Unit {
    private final int playerWindowPositionX;
    private final int playerWindowPositionY;

    private Panel panel;
    public KeyListen keystrokes;

    /**
     * Returns Player object
     *
     * @param panel      Panel on which Player will be rendered on
     * @param keystrokes KeyboardListener for inputs
     */
    public Player(Panel panel, KeyListen keystrokes) {
        this.panel = panel;
        this.keystrokes = keystrokes;

        // Returns halfway point index on the screen for drawing the player.
        playerWindowPositionX = (DefaultProperties.WindowWidth - DefaultProperties.adjustedTileSize) / 2;
        playerWindowPositionY = (DefaultProperties.WindowHeight - DefaultProperties.adjustedTileSize) / 2;
        //hitbox of player
        hitBox = new Rectangle(DefaultProperties.UnitHitBox_x, DefaultProperties.UnitHitbox_y, DefaultProperties.UnitHitbox_width, DefaultProperties.UnitHitbox_height);

        resetValues();
        loadPlayerImages();
    }

    /**
     * Getter for playerWindowPositionX
     *
     * @return int
     */
    public int getPlayerWindowPositionX() {
        return playerWindowPositionX;
    }

    /**
     * Getter for playerWindowPositionX=Y
     *
     * @return int
     */
    public int getPlayerWindowPositionY() {
        return playerWindowPositionY;
    }

    /**
     * Set Player values to defaults
     * Sets player position, speed, direction, and animations to defaults.
     */
    public void resetValues() {
        // Set player position at the top edge of the board.
        // position = adjustedTileSize * desired X/Y in Map file
        mapX = DefaultProperties.adjustedTileSize * DefaultProperties.MapStartIndex;
        mapY = DefaultProperties.adjustedTileSize * DefaultProperties.MapStartIndex;
        matrixCol = DefaultProperties.MapStartIndex;
        matrixRow = DefaultProperties.MapStartIndex;
        movementSpeed = DefaultProperties.MovementSpeed;
        unitFacing = DIRECTION.UP;

        animationCounter = 0;
        spriteNum = 1;
    }

    /**
     * Read and save sprites to player object.
     */
    public void loadPlayerImages() {
        try {
            imageUp1 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/up1.png"));
            imageUp2 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/up2.png"));
            imageDown1 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/down1.png"));
            imageDown2 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/down2.png"));
            imageLeft1 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/left1.png"));
            imageLeft2 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/left2.png"));
            imageRight1 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/right1.png"));
            imageRight2 = ImageIO.read(getClass().getResourceAsStream("/PlayerImages/right2.png"));
        } catch (IOException e) {
            //System.out.println("TEST");
            e.printStackTrace();
        }
    }

    /**
     * Moves player in the direction held down on the keyboard
     */
    public void update() {
        // Collision with enemies should be checked regardless of whether the player is moving or not
        panel.collisionCheck.enemyCheck(this);

        if((keystrokes.pushedUp || keystrokes.pushedDown || keystrokes.pushedLeft || keystrokes.pushedRight )==true) {
            if (keystrokes.pushedUp)
                unitFacing = DIRECTION.UP;
            else if (keystrokes.pushedDown)
                unitFacing = DIRECTION.DOWN;
            else if (keystrokes.pushedLeft)
                unitFacing = DIRECTION.LEFT;
            else if (keystrokes.pushedRight)
                unitFacing = DIRECTION.RIGHT;

            //check for collision with each reward
            panel.collisionCheck.collectableCheck(this);
            //checking collision with tiles
            unitCollision = false;
            panel.collisionCheck.tileCheck(this);
            // no collision, so player can move
            if (!unitCollision) {
                switch (unitFacing) {
                    case UP:
                        mapY -= movementSpeed;
                        break;
                    case DOWN:
                        mapY += movementSpeed;
                        break;
                    case LEFT:
                        mapX -= movementSpeed;
                        break;
                    case RIGHT:
                        mapX += movementSpeed;
                        break;
                }
            }

            matrixRow = Math.round(mapY / DefaultProperties.adjustedTileSize);
            matrixCol = Math.round(mapX / DefaultProperties.adjustedTileSize);
        }
        //swap between sprites every few frames to make the player look like it is walking
        countAnimations();
    }


    /**
     * Draw Player onto screen
     * Changes depending on the direction the player is moving
     *
     * @param graphics2D Graphics 2D
     */
    public void render(Graphics2D graphics2D, Panel panel) {
        BufferedImage playerSprites = getSprite(unitFacing);
        graphics2D.drawImage(playerSprites, playerWindowPositionX, playerWindowPositionY, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize, null);
    }
}

