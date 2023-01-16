package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * when implementing KeyListener KeyTyped,KeyPressed,keyReleased must be implemented
 * @author Gagandeep Manhas
 */

public class KeyListen implements KeyListener {
    public boolean pushedUp, pushedLeft, pushedRight, pushedDown; //possible directions play can traverse
    @Override
    public void keyTyped(KeyEvent e) {
    //no need to be implemented for our project
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int value = e.getKeyCode(); //contains information about which key player pressed

        //program cares about only the following key below, everything else discarded
        //add KeyEvent.VK_(button value) to add more keystroke reading functionality
        if(value == KeyEvent.VK_UP)
            pushedUp=true;

        if(value==KeyEvent.VK_DOWN)
            pushedDown=true;

        if(value==KeyEvent.VK_LEFT)
            pushedLeft=true;

        if(value==KeyEvent.VK_RIGHT)
            pushedRight=true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int value= e.getKeyCode();

        //program cares about only the following key below, everything else discarded
        //add KeyEvent.VK_(button value) to add more keystroke reading functionality
        if(value == KeyEvent.VK_UP)
            pushedUp=false;

        if(value==KeyEvent.VK_DOWN)
            pushedDown=false;

        if(value==KeyEvent.VK_LEFT)
            pushedLeft=false;

        if(value==KeyEvent.VK_RIGHT)
            pushedRight=false;
    }
}
