package Units;
import Helpers.DIRECTION;
import Helpers.DefaultProperties;
import main.Panel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements Enemies
 * Handles enemy position and path finding, as well as rendering.
 * @author Mudassir and Tanzil
 */
public class Enemy extends Unit {
    private Panel panel;
    static int control= 0;
    int framesyncher=0;
    Random rand=new Random();
    int direction=rand.nextInt(4)+1;
    int directioncounter=0;

    /**
     * Returns Enemy object
     * @param panel Panel on which Player will be rendered on
     */
    public Enemy(Panel panel) {
        this.panel = panel;
        hitBox = new Rectangle(DefaultProperties.UnitHitBox_x, DefaultProperties.UnitHitbox_y, DefaultProperties.UnitHitbox_width, DefaultProperties.UnitHitbox_height);
        resetValues();
        loadEnemyImages();
    }

    /**
     * Set Enemy values to defaults
     * Sets enemy position, speed, direction, and animations to defaults.
     */
    public void resetValues() {
        // Set player position at the top edge of the board.
        // position = adjustedTileSize * desired X/Y in Map file
        while(true) {
            Random rand=new Random();
            int tempx = ThreadLocalRandom.current().nextInt(DefaultProperties.MapStartIndex, DefaultProperties.MaxMapCol );
            int tempy = ThreadLocalRandom.current().nextInt(DefaultProperties.MapStartIndex, DefaultProperties.MaxMapRow );
            if(this.panel.util.isGrass(tempx, tempy)
                    && this.euclideanDistance(DefaultProperties.MapStartIndex - tempx, DefaultProperties.MapStartIndex - tempy) > DefaultProperties.SpawnRange) {
                this.mapX = DefaultProperties.adjustedTileSize * tempx;
                this.mapY = DefaultProperties.adjustedTileSize * tempy;
                break;
            }
        }

        movementSpeed = DefaultProperties.MovementSpeed;
        unitFacing = DIRECTION.UP;

        animationCounter = 0;
        spriteNum = 1;
    }

    /**
     * Read and save sprites to enemy object.
     */
    public void loadEnemyImages() {
        try {
            imageUp1 = ImageIO.read(getClass().getResourceAsStream("/Enemies/up1.png"));
            imageUp2 = ImageIO.read(getClass().getResourceAsStream("/Enemies/up2.png"));
            imageDown1 = ImageIO.read(getClass().getResourceAsStream("/Enemies/down1.png"));
            imageDown2 = ImageIO.read(getClass().getResourceAsStream("/Enemies/down2.png"));
            imageLeft1 = ImageIO.read(getClass().getResourceAsStream("/Enemies/left1.png"));
            imageLeft2 = ImageIO.read(getClass().getResourceAsStream("/Enemies/left2.png"));
            imageRight1 = ImageIO.read(getClass().getResourceAsStream("/Enemies/right1.png"));
            imageRight2 = ImageIO.read(getClass().getResourceAsStream("/Enemies/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the value of the euclidean distance
     * @param a int |x0-x1|
     * @param b int |y0-y1|
     * @return double Euclidean distance
     */
    public double euclideanDistance(int a, int b) {
        double value = Math.pow(a,2) + Math.pow(b, 2);
        return Math.sqrt(value);
    }

    /**
     * Updates enemy position.
     * Implements chasing and wandering behaviour
     */
    public void update() {
        //checking collision with tiles
        this.unitCollision = false;
        int colside=panel.collisionCheck.tileCheck(this);
        // no collision, so enemy can move
        int v = panel.player.mapX - this.mapX;
        int z = panel.player.mapY - this.mapY;
        double range = euclideanDistance(v, z);

        //System.out.println(range);
        if(range<DefaultProperties.DetectionRange) {
            //If player is close to enemy, enemy will try to move directly towards the player.
            if (!this.unitCollision) {
                if (v >= 0 && control < 24) {
                    this.mapX += this.movementSpeed;
                    this.unitFacing = DIRECTION.RIGHT;
                } else if (v <= 0 && control < 24) {
                    this.mapX -= this.movementSpeed;
                    this.unitFacing = DIRECTION.LEFT;
                } else if (z >= 0 && control >= 24) {
                    this.mapY += this.movementSpeed;
                    this.unitFacing = DIRECTION.DOWN;
                } else if (z <= 0 && control >= 24) {
                    this.mapY -= this.movementSpeed;
                    this.unitFacing = DIRECTION.UP;
                }
                //System.out.println(v + "-----" + z + ":::" + switcher);
                control++;
                if (control == 48)
                    control = 0;
            } else {
                //if player is between enemy and an obstacle, enemy will be forced back
                collision_Response(colside);
                //System.out.println(colside);

            }
        }
        else{
            //if the player is too far, the enemy wander around randomly
            if(directioncounter>30) {
                direction = rand.nextInt(4) + 1;
                directioncounter=0;
            }
            if (!this.unitCollision) {
                //System.out.println(direction);
                if (direction==1 ) {
                    this.mapX += this.movementSpeed-3;
                    this.unitFacing = DIRECTION.RIGHT;
                } else if (direction==2) {
                    this.mapX -= this.movementSpeed-3;
                    this.unitFacing = DIRECTION.LEFT;
                } else if (direction==3) {
                    this.mapY += this.movementSpeed-3;
                    this.unitFacing = DIRECTION.DOWN;
                } else if (direction==4) {
                    this.mapY -= this.movementSpeed-3;
                    this.unitFacing = DIRECTION.UP;
                }
            } else {
                //System.out.println("HERE2");
                collision_Response(colside);
                direction=rand.nextInt(4)+1;
                //System.out.println(colside);
            }
        }
        if(framesyncher%24==0) {
            directioncounter++;
        }
        framesyncher++;
        countAnimations();
    }

    private void collision_Response(int colside) {
        if (colside == 1)
            this.mapX += this.movementSpeed + 2;
        else if (colside == 2)
            this.mapX -= this.movementSpeed + 2;
        else if (colside == 3)
            this.mapY += this.movementSpeed + 2;
        else if (colside == 4)
            this.mapY -= this.movementSpeed + 2;
    }


    /**
     * Draw Enemy onto screen
     * Changes depending on the direction the Enemy is moving
     *
     * @param graphics2D Graphics 2D
     */
    public void render(Graphics2D graphics2D) {
        BufferedImage enemySprite = getSprite(unitFacing);
        int windowX = mapX - panel.player.mapX + panel.player.getPlayerWindowPositionX();
        int windowY = mapY - panel.player.mapY + panel.player.getPlayerWindowPositionY();

        graphics2D.drawImage(enemySprite, windowX, windowY, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize, null);
    }
}

