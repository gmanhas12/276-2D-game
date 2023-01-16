package main;

import Helpers.DefaultProperties;
import Helpers.STATE;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Displays score and related UI elements to the screen, and manages score and game states.
 * @author Pieter Ruslim
 */
public class HUD {
    Panel panel;
    Font font;
    Font endfont;
    int score;
    public String rewardIndicator;
    public boolean mapexit= false;
    public boolean gameOver= false;
    public boolean collisionWithEnemy= false;
    BufferedImage normalRewardSprite;
    int spriteSize;
    public double clockdown;
    public double totalCompletionTime;
    String message;
    int messageDuration;
    public String mapName;
    public int mapNumber;

    /**
     * Creates instance of HUD
     * @param panel panel for access to Game settings
     */
    public HUD(Panel panel){
        this.panel = panel;
        this.font = new Font("Arial", Font.PLAIN, 18);
        this.endfont = new Font("Arial", Font.BOLD, 30);
        this.score = 0;
        this.rewardIndicator = "0 / ?";
        this.mapName = "map";
        this.mapNumber = 1;
        this.totalCompletionTime = 0;
        this.clockdown = 0;
        this.spriteSize = DefaultProperties.adjustedTileSize;
        try {
            this.normalRewardSprite = ImageIO.read(getClass().getResourceAsStream("/CollectablesImages/NormalReward.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display how many rewards have been collected out of how many total rewards
     * @param rewardIndicator String that is rendered beside the Sprite of normal reward.
     */
    public void setRewardIndicator(String rewardIndicator) {
        this.rewardIndicator = rewardIndicator;
    }

    /**
     * Displays message for a duration
     * @param duration Amount of time in frames that message will be on screen
     * @param message Message to be Displayed
     */
    public void displayMessage(int duration, String message){
       this.message = message;
       messageDuration = duration;
    }

    /**
     * Render the HUD layer on screen, displays different messages based on different game states.
     * @param graphics2D graphics object of which HUD will be rendered on
     */
    public void render(Graphics2D graphics2D){
        if(mapexit){
            String mapExitMessage="Exit Reached: "+ String.format("%.02f", clockdown);
            graphics2D.setFont(endfont);
            graphics2D.setColor(Color.ORANGE);
            int x=(DefaultProperties.WindowWidth/2)-100;
            int y=(DefaultProperties.WindowHeight/2)+50;
            if( messageDuration > 0){
                graphics2D.drawString(mapExitMessage, x, y);
                messageDuration -= 1;
            }
            if (messageDuration <= 0) {
                this.mapNumber++;
                if (mapNumber <= DefaultProperties.NumberOfMaps) {
                    this.resetHUD();
                }
                else {
                    this.setMapName(null);
                    this.gameOver = true;
                    this.message = null;
                }
            }
        }
        else if (gameOver) {
            this.totalCompletionTime += clockdown;
            String gameOverMessage="Game Over!";
            String totalTime = "Total Time: " + String.format("%.02f", totalCompletionTime);
            String totalScore = "Score: " + score;
            graphics2D.setFont(endfont);
            graphics2D.setColor(Color.ORANGE);
            int x=(DefaultProperties.WindowWidth/2)-100;
            int y=(DefaultProperties.WindowHeight/2) - 50;
            graphics2D.drawString(gameOverMessage, x, y);
            y += 50;
            graphics2D.drawString(totalTime, x, y);
            y+= 50;
            graphics2D.drawString(totalScore, x, y );
            panel.looper=null;
            panel.stopbackgroundmsuic();
        }
        else if (collisionWithEnemy) {
            String message="Collided with Enemy. Game Over! "+ String.format("%.02f", clockdown);
            font_color_hud_settings(graphics2D, message);
        }
        else if(this.score<0){
            String message="Collided with trap. Game Over! "+ String.format("%.02f", clockdown);
            font_color_hud_settings(graphics2D, message);
        }
        else{
            graphics2D.setFont(font);
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawImage(normalRewardSprite, 610, 34, 18, 18, null);
            graphics2D.drawString("Score: " + score, 610, 30);
            graphics2D.drawString(rewardIndicator, 643,50);

            clockdown+=(double)1/24;
            graphics2D.drawString(String.format("%.02f", clockdown),20, 30);
            if( messageDuration > 0){
                graphics2D.drawString(message, 30, DefaultProperties.WindowHeight - 40);
                messageDuration -= 1;
            }
        }
    }

    private void font_color_hud_settings(Graphics2D graphics2D, String message) {
        graphics2D.setFont(endfont);
        graphics2D.setColor(Color.ORANGE);
        int x=(DefaultProperties.WindowWidth/2)-300;
        int y=(DefaultProperties.WindowHeight/2)+50;
        graphics2D.drawString(message, x, y);
        graphics2D.drawString("Score: " + score, x, y + 50);
        panel.looper=null;
        panel.stopbackgroundmsuic();
    }

    /**
     * Gets score
     * @return int score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets score
     * @param score int score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets map name
     * @param value String map name
     */
    public void setMapName(String value) {
        mapName = value;
    }

    /**
     * Resets the panel class with new map
     */
    public void resetHUD() {
        this.panel.setupMap(this.mapName + this.mapNumber);
        this.panel.state = STATE.GAME;
        this.mapexit = false;
        this.gameOver = false;
        this.message = null;
        this.messageDuration = 0;
        this.totalCompletionTime += clockdown;
        this.clockdown = 0;
    }
}
