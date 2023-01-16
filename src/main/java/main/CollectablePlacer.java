package main;

import Collectables.*;
import Helpers.DefaultProperties;
import Helpers.STATE;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Places and Manages all collectables on the board
 * @author Pieter Ruslim
 */
public class CollectablePlacer {
    public Panel panel;
    public HUD scoreBoard;
    public Collectable[] mapobjects;//the last index in the array is the exit
    public int normalRewardCount;
    public int bonusRewards;
    public int totalRewards;
    public int trapCount;

    /**
     * Constructs the collectable placer class with the given number of rewards and traps. By default, there is 1 exit.
     * @param panel Panel class
     * @param totalRewards int The total number of regular rewards on the map
     * @param bonusRewards int The total number of bonus rewards on the map
     * @param trapCount int The totoal number of traps on the map
     */
    public CollectablePlacer(Panel panel, int totalRewards, int bonusRewards, int trapCount){
        this.panel = panel;
        this.scoreBoard = panel.scoreBoard;
        this.bonusRewards = bonusRewards;
        this.normalRewardCount = 0;
        this.totalRewards = totalRewards; //total rewards temporarily set to 12, may be decided by map file later.
        this.trapCount = trapCount; //adding additional traps such that the score could be <= 0
        this.scoreBoard.setRewardIndicator("" + normalRewardCount + " / " + totalRewards);

        // The total number of objects in a map is determined by the number of rewards, trap and the exit gate.
        this.mapobjects = new Collectable[this.totalRewards + this.trapCount + this.bonusRewards + 1];
    }

    /**
     * Checks whether the generated tile is empty and can be used
     * @param usedPositions LinkedList(int[]) used positions on the map
     * @param x int
     * @param y int
     * @return boolean
     */
    private boolean isValidPosition(LinkedList<int[]> usedPositions, int x, int y) {
        return !isUsedPosition(usedPositions, new int[] {x, y}) && panel.util.isGrass(x, y);
    }

    /**
     * Checks whether a current index in the matrix is being used by another object
     * @param usedPosition LinkedList(int[]) List of array containing index values {x, y}
     * @param position int[] Given array containing index value {x, p}
     * @return boolean True if the same array exist within the list
     */
    private boolean isUsedPosition(LinkedList<int[]> usedPosition, int[] position) {
        if (usedPosition == null || usedPosition.isEmpty()) return false;

        for (int[] item : usedPosition) {
            if (Arrays.equals(item, position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Places collectables such as rewards, bonus rewards, traps and exit door in the map.
     */
    public void setCollectables(){
        LinkedList<int[]> usedPositions = new LinkedList<>();
        int bonusRewardRange = totalRewards + bonusRewards;
        int trapRange = bonusRewardRange + trapCount;
        int finalIndex = trapRange + 1;
        int tempx;
        int tempy;

        for(int i=0;i<finalIndex;i++) {
            tempx = ThreadLocalRandom.current().nextInt(DefaultProperties.MapStartIndex, DefaultProperties.MaxMapCol);

            // The final index is for the map exit and so tempy should pick from the final traversable row on the map
            if (i == finalIndex - 1) {
                tempy = DefaultProperties.MaxMapRow - DefaultProperties.MapStartIndex - 1;
            }
            else {
                tempy = ThreadLocalRandom.current().nextInt(DefaultProperties.MapStartIndex, DefaultProperties.MaxMapRow);
            }

            int[] position = new int[]{tempx, tempy};

            if (isValidPosition(usedPositions, tempx, tempy)) {
                usedPositions.add(position);

                if (i < totalRewards) {
                    mapobjects[i] = new NormalReward(tempx * DefaultProperties.adjustedTileSize, tempy * DefaultProperties.adjustedTileSize, panel);

                } else if (i >= totalRewards && i < bonusRewardRange) {
                    mapobjects[i] = new BonusReward(tempx * DefaultProperties.adjustedTileSize, tempy * DefaultProperties.adjustedTileSize, panel);

                } else if (i >= bonusRewardRange && i < trapRange) {
                    mapobjects[i] = new Trap(tempx * DefaultProperties.adjustedTileSize, tempy * DefaultProperties.adjustedTileSize);
                } else {
                    mapobjects[i] = new Exit(tempx * DefaultProperties.adjustedTileSize, tempy * DefaultProperties.adjustedTileSize);
                }
            }
            else
                i--;
        }

        usedPositions.clear();
    }

    /**
     * Implements behaviour of rewards when they have been collided with
     * @param index index of reward colliding with player in the rewards array
     */
    public void onCollide(int index) {
        if (mapobjects[index].collect(scoreBoard, this) == true) {
            mapobjects[index] = null;

            if (this.scoreBoard.mapexit || this.scoreBoard.gameOver) {
                this.panel.state = STATE.PAUSE;
            }

            if (this.normalRewardCount == this.totalRewards && mapobjects[mapobjects.length - 1] != null) {
                // The last item in the objects list is the Exit
                mapobjects[mapobjects.length - 1].visible = true;
            }
        }
    }
}
