package Helpers;

/**
 * Class listing all default hardcoded setting values for the game
 * @author Mudassir Noor
 */
public class DefaultProperties {
    // Screen Settings
    public static final int defaultTileSize =16;                   //16*16 tile, most common tile size used in most 2d games
    public static final int scale=3;

    public static final int adjustedTileSize = defaultTileSize *scale;    //16*3= scaled up to 48x48
    public static final int screenColumn =15;
    public static final int screenRow =10;

    // tileSize*maxScreenCol = 720 pixels , width of window
    // tileSize*maxScreenRow = 480 pixels , height of window
    public static final int WindowWidth = adjustedTileSize * screenColumn;
    public static final int WindowHeight = adjustedTileSize * screenRow;

    // Map Settings
    public static final int MaxMapCol = 42;
    public static final int MaxMapRow = 42;
    public static final int MapStartIndex = 7;
    public static final int NumberOfMaps = 3;

    // System Properties
    public static final int framesPerSecond = 24;

    // Score Settings
    public static final int RegularReward = 100;
    public static final int BonusReward = 200;
    public static final int ScoreDeduction = 100;

    // Enemy and Player Settings
    public static final int enemyMultiplier = 2;
    public static final int DetectionRange = 200; // in pixel units
    public static final int SpawnRange = 8; // distance in the map matrix
    public static final int MovementSpeed = 4;
    public static final int UnitHitBox_x = 15;
    public static final int UnitHitbox_y = 35;
    public static final int UnitHitbox_width = 10;
    public static final int UnitHitbox_height = UnitHitbox_width;

    // Path object values
    public static final int Traversable = 10;
}
