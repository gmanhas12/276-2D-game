package main;

import Helpers.DefaultProperties;
import Helpers.STATE;

import java.awt.*;

/**
 * Defines Behaviour of program before game starts
 * @author Pieter Ruslim
 */
public class StartMenu {
    Panel panel;
    Font font;
    Rectangle playButton;
    public StartMenu(Panel panel){
        this.panel = panel;
        font = new Font("arial",Font.BOLD,30);
        playButton = new Rectangle(DefaultProperties.WindowWidth/2 - 50, DefaultProperties.WindowHeight/2, 100, 50);
    }

    /**
     * Starts the game when play button is clicked
     * @param x mouse X position
     * @param y mouse Y position
     */
    public void checkClick(int x, int y){
        if( panel.state == STATE.MENU) {
            //System.out.println("Click" + x + " " + y);
            if (playButton.contains(x, y)) {
                panel.state = STATE.GAME;
                panel.backgroundmusic(0);
            }
        }
    }

    /**
     * Displays start Menu on screen
     * @param graphics2D screen
     */
    public void render(Graphics2D graphics2D){
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString("Play", playButton.x + 15, playButton.y + 30);
        graphics2D.drawString("Escape From The Wild", DefaultProperties.WindowWidth/2 - 150, 100);

        graphics2D.draw(playButton);
    }
}
