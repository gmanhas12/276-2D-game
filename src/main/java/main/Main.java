package main;

import javax.swing.*;

public class Main {
    public static void main( String[] Args){
        System.out.println(System.getProperty("user.dir"));
        JFrame frame= new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Escape to the Wild"); //Title shows up on window bar
        Panel panel= new Panel();
        panel.setupMap("map1"); // Setup game state before game loop
        frame.setVisible(true); // setting to false makes a headerless program
        frame.add(panel); //adds properties of panel class to window
        frame.pack(); //displays our screen tiles, without it we only get a window bar
        frame.setLocationRelativeTo(null);// settings window to center of screen, does not work if called before .pack()


        panel.gameLoopStarter(); // calls the class that starts the loop
    }
}
