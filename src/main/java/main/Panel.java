package main;
import Background.BackgroundController;
import Helpers.DefaultProperties;
import Helpers.STATE;
import Units.Enemy;
import Units.Player;
import javax.swing.*;
import java.awt.*;

/**
 * The base window that every other game element will be drawn on top of.
 * Handles the game loop, and holds a reference to every object in the main and Units packages.
 * @author Tanzil Sarker
 */
public class Panel extends JPanel implements Runnable{
    //Screen settings
    KeyListen keystroke = new KeyListen();                   //calling class to listen for keystrokes
    Thread looper;                                          //used to control game time
    Audio audio = new Audio();

    StartMenu startMenu = new StartMenu(this);
    MouseListener mouseListener = new MouseListener(this.startMenu);

    // Game variable definitions
    public STATE state = STATE.MENU;
    public CheckCollision collisionCheck;
    public HUD scoreBoard;
    public Player player;
    public CollectablePlacer collectablePlacer;
    public Enemy[] enemies;
    public BackgroundController backgroundController;
    public Util util;

    public Panel(){
        this.addKeyListener(keystroke);               //from listeners class
        this.addMouseListener(mouseListener);         //for menus
        this.setBackground(Color.darkGray);           //setting color of background, Options : black, white, yellow, etc
        this.setDoubleBuffered(true);                 //improves game performance
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(DefaultProperties.WindowWidth, DefaultProperties.WindowHeight));
        this.scoreBoard = new HUD(this);
    }

    /**
     * Sets up a given map with all collectables, player and other necessary utilities
     */
    public void setupMap(String mapFileName) {
        backgroundController = new BackgroundController(mapFileName);
        collisionCheck = new CheckCollision(this);
        collectablePlacer = new CollectablePlacer(this, 8, 4, 5);
        player = new Player(this, keystroke);
        util = new Util(this);
        collectablePlacer.setCollectables();
        enemies = new Enemy[this.scoreBoard.mapNumber * DefaultProperties.enemyMultiplier];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(this);
        }
    }

    /**
     * Renders the maps
     * @param graphics Graphics
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);                         //subclass of panel
        Graphics2D graphics2D = (Graphics2D) graphics;
        backgroundController.render(graphics2D, this);
        if (state == STATE.GAME || state == STATE.PAUSE) { //Render objects that are part of the Game
            //render each collectable
            for (int i = 0; i < collectablePlacer.mapobjects.length; i++) {
                if (collectablePlacer.mapobjects[i] != null) {
                    collectablePlacer.mapobjects[i].render(graphics2D, this);
                }
            }
            // render enemies and player
            player.render(graphics2D, this);
            for (Enemy enemy : enemies) {
                enemy.render(graphics2D);
            }
            // render scoreboard
            scoreBoard.render(graphics2D);
        }
        else if(state == STATE.MENU){ //Render objects that are part of the Menu
            startMenu.render(graphics2D);
        }
        graphics2D.dispose();                                   //saves memory
    }

    /**
     * Updates the positions of Units
     */
    public void Refresh(){
        if( state == STATE.GAME){
            player.update();
            for (Enemy enemy: enemies) {
                enemy.update();
            }
        }
}

    /**
     * Starts the game loop
     */
    public void gameLoopStarter(){
        looper = new Thread(this);                //passing game panel class
        looper.start();                                 //calls the run method
    }

    /**
     * Plays the given soundfile in a loop
     * @param index index number of the file
     */
    public void backgroundmusic(int index){
        audio.soundfile(index);
        audio.playsound();
        audio.soundloop();
    }

    /**
     * Stops the musice when the game is over
     */
    public void stopbackgroundmsuic(){
        audio.stopsound();
    }

    /**
     * Controls the pace of the game, making sure that the game updates and renders at a consistent rate
     * When game thread started, it calls this method. Game loop implemented in delta time method
     * 1 update game information
     * 1 if player moves on the map the players x/y coordinate changes on the screen we update that information here
     * * and draw the screen of player with new position
     * 2 draw our screen inside this loop with the information
     * 2 how many times we decide to update will set our games FPS
     */
    @Override
    public void run() {
        double refresh_rate= 1000.0/ DefaultProperties.framesPerSecond; // at 24 FPS the screen is refreshed every 41.66 milliseconds
        long prevTime=System.currentTimeMillis();
        double accumulator=0;
        int fps=0;
        long stopwatch=0;
        long timeNow;
        while (looper != null) {
            //check current time
            timeNow= System.currentTimeMillis();

            // Subtract current time with last time and get the change in time
            // then divide by refresh rate, then refreshes screen at every 41.66 system milliseconds
            accumulator= accumulator+((timeNow-prevTime)/refresh_rate);
            stopwatch=stopwatch+(timeNow-prevTime);

            // makes current time into past time
            prevTime=timeNow;

            if (accumulator>=1) {
                Refresh();
                repaint();
                accumulator--;
                fps++;
            }

            if(stopwatch>= 1000){
                //System.out.println(fps);
                fps=0;
                stopwatch=0;
            }
        }
    }
}
