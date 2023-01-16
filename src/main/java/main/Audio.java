package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/**
 * @author Tanzil Sarker
 * Audio class that contains audio clipsa nd functions to import, play, loop, and stop clips
 */
public class Audio {
    Clip clip;
    URL gamesounds[]=new URL[1];

    /**
     * Constructor for audio class
     */
    public Audio(){
        gamesounds[0]=getClass().getResource("/Sound/backgroundsound.wav");
    }

    /**
     * plays sound clip
     */
    public void playsound(){
        clip.start();
    }
    /**
     * loops audio
     */
    public void soundloop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    /**
     * stops audio
     */
    public void stopsound(){
        clip.stop();
    }
    /**
     * imports audio file
     */
    public void soundfile(int index) {
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(gamesounds[index]);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
