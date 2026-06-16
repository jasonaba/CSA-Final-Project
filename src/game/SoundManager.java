package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    public static void playSound(String soundFilePath) {
        try {
            File audioFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    	public static void loopMusic(String soundFilePath) {
    	    try {
    	        File audioFile = new File(soundFilePath);
    	        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
    	        
    	        Clip clip = AudioSystem.getClip();
    	        clip.open(audioStream);
    	        
    	        // THIS IS THE MAGIC LINE: It tells Java to loop it forever!
    	        clip.loop(Clip.LOOP_CONTINUOUSLY); 
    	        
    	    } catch (Exception e) {
    	        System.out.println("Error looping music: " + e.getMessage());
    	    }
    	}


}