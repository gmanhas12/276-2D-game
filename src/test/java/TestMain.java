import Background.BackgroundController;
import Collectables.Collectable;
import Collectables.NormalReward;
import Helpers.DIRECTION;
import Helpers.DefaultProperties;
import Helpers.STATE;
import Units.Enemy;
import Units.Player;
import Units.Unit;
import main.*;
import main.Panel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.awt.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests class for all tests for our project
 * @author Mudassir Noor
 */
public class TestMain {

    private static Panel _testPanel = new Panel();
    private CollectablePlacer _testCollectablePlacer;

    /**
     * Parameters for TestCollectablePlacer_OnCollide()
     * @return Stream(Arguments) (int index, int expectedScore, int expectedRewardCount)
     */
    private static Stream<Arguments> collectables_collect_arguments() {
        return Stream.of(
                Arguments.of(0, DefaultProperties.RegularReward, 1), // NormalReward increases score and reward count
                Arguments.of(1, DefaultProperties.BonusReward, 0), // BonusReward increases score but no effect on count
                Arguments.of(2, -DefaultProperties.ScoreDeduction, 0) // Trap deducts score but no effect on count
        );
    }

    /**
     * Parameters for TestCheckCollision_tileCheck()
     * @return Stream(Arguments) (String playerFacing, int position, int expectedResult)
     */
    private static Stream<Arguments> checkCollision_tileCheck_arguments() {
        return Stream.of(
                Arguments.of(DIRECTION.UP, DefaultProperties.MapStartIndex, 3), // Top left corner
                Arguments.of(DIRECTION.LEFT, DefaultProperties.MapStartIndex, 1), // Top left corner
                Arguments.of(DIRECTION.DOWN, DefaultProperties.MaxMapCol - 1 - DefaultProperties.MapStartIndex, 4), // Bottom right corner
                Arguments.of(DIRECTION.RIGHT, DefaultProperties.MaxMapCol - 1 - DefaultProperties.MapStartIndex, 2)  // Botttom right corner
        );
    }

    /**
     * Parameters for TestPlayer_update()
     * @return Stream(Arguments) (String playerFacing, int changeInY, int changeInX)
     */
    private static Stream<Arguments> player_update_arguments() {
        return Stream.of(
                Arguments.of(DIRECTION.UP, -DefaultProperties.MovementSpeed, 0),
                Arguments.of(DIRECTION.LEFT, 0, -DefaultProperties.MovementSpeed),
                Arguments.of(DIRECTION.DOWN, DefaultProperties.MovementSpeed, 0),
                Arguments.of(DIRECTION.RIGHT, 0, DefaultProperties.MovementSpeed)
        );
    }

    /**
     * Setup the panel and other required classes before each test
     */
    @BeforeEach
    public void Setup() {
        _testPanel = new Panel();
        _testPanel.util = new Util(_testPanel);
        _testPanel.backgroundController = new BackgroundController("map1");
        _testPanel.collisionCheck = new CheckCollision(_testPanel);
    }

    /**
     * Tests the Panel.setupMap() method
     */
    @Test
    public void TestPanel_setupMap() {
        Panel testPanel = new Panel();
        testPanel.setupMap("map1");

        assertNotNull(testPanel);
        assertNotNull(testPanel.scoreBoard);
        assertNotNull(testPanel.player);
        assertNotNull(testPanel.util);
        assertNotNull(testPanel.collectablePlacer);
        assertNotNull(testPanel.backgroundController);
        assertEquals(testPanel.state, STATE.MENU); // Initial State
        assertNotNull(testPanel.enemies);
        assertNotNull(testPanel.collisionCheck);
    }

    /**
     * Test for BackgroundController.mapGen() method
     */
    @Test
    public void TestBackgroundController_mapGen(){
        _testPanel.backgroundController.mapGen("/MapData/TestMap.txt", 4, 4);

        int[][] expectedMatrix = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        for (int i = 0; i < 4; i++) {
            assertArrayEquals(expectedMatrix[i], _testPanel.backgroundController.mapTileMatrixReader[i]);
        }
    }

    /**
     * Test for BackgroundController.tiletool() method
     */
    @Test
    public void TestBackgroundController_tiletool() {
        // Tests a valid scenario with correct file path
        _testPanel.backgroundController.tiletool(0, true, "water");
        assertNotNull(_testPanel.backgroundController.backgroundTiles[0]);
        assertTrue(_testPanel.backgroundController.backgroundTiles[0].collide);
        assertNotNull(_testPanel.backgroundController.backgroundTiles[0].image);

        // Tests invalid scenario with incorrect file path. Exception is caught with stack trace.
        assertDoesNotThrow(() -> _testPanel.backgroundController.tiletool(0, true, "random"));
    }

    /**
     * Test for CollectablePlacer.setCollectables()
     */
    @Test
    public void TestCollectablePlacer_setCollectables() {
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 8, 4, 5);
        _testCollectablePlacer.setCollectables();

        for (Collectable collectable: _testCollectablePlacer.mapobjects) {
            assertNotNull(collectable);
            assertNotNull(collectable.sprite);
            assertNotNull(collectable.mapX);
            assertNotNull(collectable.mapY);
        }
    }

    /**
     * Tests the CollectablePlacer.onCollide() method
     */
    @Test
    public void TestCollectablePlacer_onCollide() {
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testCollectablePlacer.setCollectables();
        // For Exit, the total rewards count must match the normal rewards count
        assertFalse(_testCollectablePlacer.mapobjects[_testCollectablePlacer.mapobjects.length - 1].visible);
        for (int i = 0; i < _testCollectablePlacer.mapobjects.length; i++) {
            _testCollectablePlacer.onCollide(i);
        }

        int expectedScore = DefaultProperties.RegularReward + DefaultProperties.BonusReward - DefaultProperties.ScoreDeduction;

        // Assert expected final score
        assertEquals(expectedScore, _testCollectablePlacer.scoreBoard.getScore());

        // Assert all regular rewards collected
        assertEquals(_testCollectablePlacer.normalRewardCount, _testCollectablePlacer.totalRewards);

        // Assert map exit is visible after collection of all rewards
        assertTrue(_testCollectablePlacer.scoreBoard.mapexit);
    }

    /**
     * Tests collect() method for NormalReward, BonusReward and Trap
     * @param index int index position of collectable on the collectable placer objects list
     * @param expectedScore int expected score after collectable is collected
     * @param expectedRewardCount int expected normal reward count after collectable is collected
     */
    @ParameterizedTest
    @MethodSource("collectables_collect_arguments")
    public void TestCollectables_collect(int index, int expectedScore, int expectedRewardCount) {
        _testPanel.player = new Player(_testPanel, new KeyListen());
        _testPanel.scoreBoard = new HUD(_testPanel);
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testCollectablePlacer.setCollectables();

        _testCollectablePlacer.mapobjects[index].collect(_testPanel.scoreBoard, _testCollectablePlacer);

        assertEquals(expectedScore, _testPanel.scoreBoard.getScore());
        assertEquals(expectedRewardCount, _testCollectablePlacer.normalRewardCount);

        if (_testCollectablePlacer.mapobjects[index] instanceof NormalReward) {
            assertEquals("1 / 1", _testPanel.scoreBoard.rewardIndicator);
        }
    }

    /**
     * Tests the Exit.collect() method
     */
    @Test
    public void TestExit_collect() {
        _testPanel.player = new Player(_testPanel, new KeyListen());
        _testPanel.scoreBoard = new HUD(_testPanel);
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testCollectablePlacer.setCollectables();

        // First collect all required rewards to be able to execute the Exit.collect() method
        _testCollectablePlacer.mapobjects[0].collect(_testPanel.scoreBoard, _testCollectablePlacer);

        // All regular reward collected and able to proceed to next map
        _testCollectablePlacer.mapobjects[3].collect(_testPanel.scoreBoard, _testCollectablePlacer);
        assertTrue(_testPanel.scoreBoard.mapexit);

        // If the player is on the last map, the exit should trigger game over
        _testPanel.scoreBoard.mapNumber = DefaultProperties.NumberOfMaps;
        _testCollectablePlacer.mapobjects[3].collect(_testPanel.scoreBoard, _testCollectablePlacer);
        assertTrue(_testPanel.scoreBoard.gameOver);
        assertFalse(_testPanel.scoreBoard.mapexit);

        // If the score is 0 or less, the exit should trigger game over
        _testPanel.scoreBoard.mapNumber = DefaultProperties.NumberOfMaps - 1;
        _testPanel.scoreBoard.setScore(0);
        assertTrue(_testPanel.scoreBoard.gameOver);
        assertFalse(_testPanel.scoreBoard.mapexit);
    }

    /**
     * Tests CheckCollision.tileCheck() method
     * @param playerFacing DIRECTION direction of unit
     * @param position int defines both row and column position of unit
     * @param expectedResult int expectedResult from tileCheck() method
     */
    @ParameterizedTest
    @MethodSource("checkCollision_tileCheck_arguments")
    public void TestCheckCollision_tileCheck(DIRECTION playerFacing, int position, int expectedResult) {
        Unit unit = new Unit();
        unit.movementSpeed = DefaultProperties.MovementSpeed;
        unit.matrixRow = position;
        unit.matrixCol = position;
        unit.hitBox = new Rectangle(DefaultProperties.UnitHitBox_x, DefaultProperties.UnitHitbox_y, DefaultProperties.UnitHitbox_width, DefaultProperties.UnitHitbox_height);

        unit.unitFacing = playerFacing;
        unit.mapX = unit.mapY = DefaultProperties.adjustedTileSize * position;

        assertEquals(expectedResult, _testPanel.collisionCheck.tileCheck(unit));
    }

    /**
     * Tests CheckCollision.collectableCheck()
     */
    @Test
    public void TestCheckCollision_collectableCheck() {
        _testPanel.scoreBoard = new HUD(_testPanel);
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testCollectablePlacer.setCollectables();
        _testPanel.collectablePlacer = _testCollectablePlacer;

        _testPanel.player = new Player(_testPanel, new KeyListen());

        _testPanel.collisionCheck.collectableCheck(_testPanel.player);
        assertEquals(0, _testPanel.scoreBoard.getScore());
        assertNotNull(_testCollectablePlacer.mapobjects[0]);

        // Check collision with first index as it is the normal reward
        _testCollectablePlacer.mapobjects[0].mapX = _testPanel.player.mapX;
        _testCollectablePlacer.mapobjects[0].mapY = _testPanel.player.mapY;
        _testPanel.collisionCheck.collectableCheck(_testPanel.player);
        assertEquals(DefaultProperties.RegularReward, _testPanel.scoreBoard.getScore());
        assertNull(_testCollectablePlacer.mapobjects[0]);
    }

    /**
     * Tests CheckCollision.enemyCheck()
     */
    @Test
    public void TestCheckCollision_enemyCheck() {
        _testPanel.scoreBoard = new HUD(_testPanel);
        _testCollectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testCollectablePlacer.setCollectables();
        _testPanel.collectablePlacer = _testCollectablePlacer;
        _testPanel.player = new Player(_testPanel, new KeyListen());

        _testPanel.enemies = new Enemy[] {new Enemy(_testPanel)};

        _testPanel.collisionCheck.enemyCheck(_testPanel.player);
        assertFalse(_testPanel.scoreBoard.collisionWithEnemy);

        _testPanel.enemies[0].mapX = _testPanel.player.mapX + 3;
        _testPanel.enemies[0].mapY = _testPanel.player.mapY;

        _testPanel.collisionCheck.enemyCheck(_testPanel.player);
        assertTrue(_testPanel.scoreBoard.collisionWithEnemy);
    }

    /**
     * Tests Enemy.euclideanDistance() method
     */
    @Test
    public void TestEnemy_euclideanDistance() {
        Enemy testEnemy = new Enemy(_testPanel);

        assertEquals(5, testEnemy.euclideanDistance(4, 3));
    }

    /**
     * Tests Enemy.resetValues() method
     */
    @Test
    public void TestEnemy_resetValues() {
        Player testPlayer = new Player(_testPanel, new KeyListen());
        Enemy testEnemy = new Enemy(_testPanel);
        testEnemy.resetValues();

        assertTrue(testEnemy.euclideanDistance(testEnemy.mapX - testPlayer.mapX, testEnemy.mapY - testPlayer.mapY) > DefaultProperties.SpawnRange);
        assertEquals(testEnemy.movementSpeed, DefaultProperties.MovementSpeed);
        assertEquals(testEnemy.unitFacing, DIRECTION.UP);
    }

    /**
     * Tests Player.resetValues() method
     */
    @Test
    public void TestPlayer_resetValues() {
        Player testPlayer = new Player(_testPanel, new KeyListen());
        testPlayer.resetValues();

        assertEquals(testPlayer.movementSpeed, DefaultProperties.MovementSpeed);
        assertEquals(testPlayer.matrixCol, DefaultProperties.MapStartIndex);
        assertEquals(testPlayer.matrixRow, DefaultProperties.MapStartIndex);
        assertEquals(testPlayer.mapX, DefaultProperties.MapStartIndex * DefaultProperties.adjustedTileSize);
        assertEquals(testPlayer.mapY, DefaultProperties.MapStartIndex * DefaultProperties.adjustedTileSize);
        assertEquals(testPlayer.unitFacing, DIRECTION.UP);
    }

    /**
     * Test for Player.update() method
     * @param playerFacing String direction of player -> up, down, left, right
     * @param changeInY int Change in mapY value
     * @param changeInX int Change in mapX value
     */
    @ParameterizedTest
    @MethodSource("player_update_arguments")
    public void TestPlayer_update(DIRECTION playerFacing, int changeInY, int changeInX) {
        _testPanel.player = new Player(_testPanel, new KeyListen());
        _testPanel.collectablePlacer = new CollectablePlacer(_testPanel, 0, 0, 0);
        _testPanel.player.matrixCol = 12;
        _testPanel.player.matrixRow = 13;
        _testPanel.player.mapX = DefaultProperties.adjustedTileSize * _testPanel.player.matrixCol;
        _testPanel.player.mapY = DefaultProperties.adjustedTileSize * _testPanel.player.matrixRow;

        switch (playerFacing) {
            case UP:
                _testPanel.player.keystrokes.pushedUp = true;
                break;
            case DOWN:
                _testPanel.player.keystrokes.pushedDown = true;
                break;
            case LEFT:
                _testPanel.player.keystrokes.pushedLeft = true;
                break;
            case RIGHT:
                _testPanel.player.keystrokes.pushedRight = true;
                break;
        }

        int initialX = _testPanel.player.mapX;
        int initialY = _testPanel.player.mapY;
        _testPanel.player.update();

        int expectedMapY = initialY + changeInY;
        int expectedMapX = initialX + changeInX;

        assertEquals(_testPanel.player.unitFacing, playerFacing);
        assertEquals(expectedMapY, _testPanel.player.mapY);
        assertEquals(expectedMapX, _testPanel.player.mapX);
        assertEquals(expectedMapY / DefaultProperties.adjustedTileSize, _testPanel.player.matrixRow);
        assertEquals(expectedMapX / DefaultProperties.adjustedTileSize, _testPanel.player.matrixCol);
    }

    /**
     * Test for Player.getWindowPositionX() getter
     */
    @Test
    public void TestPlayer_getWindowPositionX() {
        Player testPlayer = new Player(_testPanel, new KeyListen());
        int expectedWindowX = (DefaultProperties.WindowWidth - DefaultProperties.adjustedTileSize) / 2;
        assertEquals(expectedWindowX, testPlayer.getPlayerWindowPositionX());
    }

    /**
     * Test for Player.getWindowPositionY() getter
     */
    @Test
    public void TestPlayer_getWindowPositionY() {
        Player testPlayer = new Player(_testPanel, new KeyListen());
        int expectedWindowY = (DefaultProperties.WindowHeight - DefaultProperties.adjustedTileSize) / 2;
        assertEquals(expectedWindowY, testPlayer.getPlayerWindowPositionY());
    }

    /**
     * Check click on Start Button
     */
    @Test
    public void TestStartMenu_checkClick() {
        _testPanel.state = STATE.MENU.MENU;
        int x = DefaultProperties.WindowWidth / 2  + 20;
        int y = DefaultProperties.WindowHeight / 2 + 20;

        StartMenu testMenu = new StartMenu(_testPanel);
        testMenu.checkClick(x, y);
        assertEquals(STATE.GAME, _testPanel.state);
    }

    /**
     * Test for HUD.resetHUD()
     */
    @Test
    public void TestHUD_resetHUD() {
        _testPanel.scoreBoard = new HUD(_testPanel);

        int completionTime = 20;
        _testPanel.scoreBoard.clockdown = completionTime;
        _testPanel.scoreBoard.setScore(DefaultProperties.RegularReward);
        _testPanel.collectablePlacer = new CollectablePlacer(_testPanel, 1, 1, 1);
        _testPanel.collectablePlacer.normalRewardCount = 1;

        _testPanel.scoreBoard.resetHUD();
        assertEquals(DefaultProperties.RegularReward, _testPanel.scoreBoard.getScore());
        assertEquals(0, _testPanel.scoreBoard.clockdown);
        assertEquals(completionTime, _testPanel.scoreBoard.totalCompletionTime);
        assertEquals(0, _testPanel.collectablePlacer.normalRewardCount);
        assertFalse(_testPanel.scoreBoard.mapexit);
        assertFalse(_testPanel.scoreBoard.gameOver);
        assertFalse(_testPanel.scoreBoard.collisionWithEnemy);
    }
}
