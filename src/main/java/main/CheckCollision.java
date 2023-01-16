package main;
import Collectables.Collectable;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import Helpers.DefaultProperties;
import java.awt.*;

/**
 * @author Gagandeep Manhas
 * checks for collisions
 */
public class CheckCollision {
    public Panel panel;

    public CheckCollision(Panel panel) {
        this.panel = panel;
    }

    /**
     * Checks whether the particular unit is about to move into a tile with a collidable tile
     * @param unit Player or enemy entity
     */
    public int tileCheck(Unit unit) {
        int mapRight = unit.mapX + unit.hitBox.x + unit.hitBox.width;//pass
        int columnRight = mapRight / DefaultProperties.adjustedTileSize;

        int mapLeft = unit.mapX + unit.hitBox.x;//pass
        int columnLeft = mapLeft / DefaultProperties.adjustedTileSize;

        int mapBottom = unit.mapY + unit.hitBox.y + unit.hitBox.height;//pass
        int bottomRow = mapBottom / DefaultProperties.adjustedTileSize;

        int mapTop = unit.mapY + unit.hitBox.y;//pass
        int topRow = mapTop / DefaultProperties.adjustedTileSize;

        switch (unit.unitFacing) {
            case LEFT -> {
                columnLeft = (mapLeft - unit.movementSpeed) / (DefaultProperties.adjustedTileSize);
                this.isCollidableTile(unit, columnLeft, topRow, columnLeft, bottomRow);
                return 1;
            }
            //break;
            case RIGHT -> {
                columnRight = (mapRight + unit.movementSpeed) / (DefaultProperties.adjustedTileSize);
                this.isCollidableTile(unit, columnRight, topRow, columnRight, bottomRow);
                return 2;
            }
            //break;
            case UP -> {
                topRow = (mapTop - unit.movementSpeed) / (DefaultProperties.adjustedTileSize);
                this.isCollidableTile(unit, columnLeft, topRow, columnRight, topRow);
                return 3;
            }
            // break;
            case DOWN -> {
                bottomRow = (mapBottom + unit.movementSpeed) / (DefaultProperties.adjustedTileSize);
                this.isCollidableTile(unit, columnLeft, bottomRow, columnRight, bottomRow);
                return 4;
            }
            // break;
        }

        return -1;
    }

    /**
     * Checks collisions between player and every collectable, sends a message to collectablePlacer when collision is found
     * @param player Player instance that is being checked for collisions with
     */
    public void collectableCheck(Player player){
        //Get player's hitbox location relative to map
        if (panel.collectablePlacer.mapobjects != null) {
            Rectangle playerHitbox = new Rectangle(player.mapX, player.mapY, DefaultProperties.adjustedTileSize, DefaultProperties.adjustedTileSize);
            for (int i = 0; i < panel.collectablePlacer.mapobjects.length; i++) {
                Collectable collectable = panel.collectablePlacer.mapobjects[i];
                if (collectable != null) {
                    collectable.collectableHitbox.setLocation(collectable.mapX, collectable.mapY);
                    if (collectable.collectableHitbox.intersects(playerHitbox)) {
                        panel.collectablePlacer.onCollide(i);
                    }
                }
            }
        }
    }

    /**
     * Checks collision between player and a moving enemy. Causes the game to stop.
     * @param player reference to player
     */
    public void enemyCheck(Player player) {
        if (panel.enemies != null) {
            for (Enemy enemy : panel.enemies) {
                if ((enemy != null) && (enemy.euclideanDistance(enemy.mapX - player.mapX, enemy.mapY - player.mapY) < 20)) {
                        System.out.println("Collision with enemy");
                        panel.scoreBoard.collisionWithEnemy = true;
                        break;
                    }
                }
            }
        }


    /**
     * Checks whether the tiles at the given location in the matrix are hard obstacles
     * @param unit Unit class whose player collision is to be changed
     * @param colA column index of the first tile
     * @param rowA row index of the first tile
     * @param colB column index of the second tile
     * @param rowB row index of the second tile
     */
    private void isCollidableTile(Unit unit, int colA, int rowA, int colB, int rowB) {
        int tileA = panel.backgroundController.mapTileMatrixReader[colA][rowA];
        int tileB = panel.backgroundController.mapTileMatrixReader[colB][rowB];
        if (panel.backgroundController.backgroundTiles[tileA].collide || panel.backgroundController.backgroundTiles[tileB].collide) {
            unit.unitCollision = true;
        }
    }
}
