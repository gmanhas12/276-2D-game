package main;

import java.awt.event.MouseEvent;

/**
 * Receives MouseEvent, sends messages to appropriate recipients
 * @author Pieter Ruslim
 */
public class MouseListener implements java.awt.event.MouseListener {
    private StartMenu startMenu;
    public MouseListener(StartMenu startMenu) {
        this.startMenu = startMenu;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Receives mouse pressed events, and sends them to StartMenu for position checking
     * @param e Mouse Event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("Click" );
        int mouseX = e.getX();
        int mouseY = e.getY();
        startMenu.checkClick(mouseX, mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
