import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;

import java.io.File;
import java.io.IOException;

public class Music_class {
    String currentFilePath;
    static Clip clip;
    public static boolean isPlayingRightnow = false;
    public String musicID_01 = "test_package\\image\\Fontaine.wav";
    public String musicID_02 = "test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav";
    public Music_class(String filePath) {
        try {
            // Load the audio file
            File audioFile = new File(filePath);

            // Get the audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Load the clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file format.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Line unavailable for audio.");
            e.printStackTrace();
        }
        
    }
    public void loadAudioFile(String filePath) {
        try {
            // Load the audio file
            File audioFile = new File(filePath);

            // Get the audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Load the clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            currentFilePath = filePath; // Store the file path
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file format.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Line unavailable for audio.");
            e.printStackTrace();
        }
    }
    // Method to start playing the music
    public static void Playsong(){
        isPlayingRightnow = !isPlayingRightnow;
    }
    public void play() {
        if(!isPlayingRightnow){
            
        if (clip != null) {
            clip.start();
            System.out.println("Music started.");

            try {
                // Sleep the thread while music is playing
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Clip is not initialized.");
            }
        }
    }

    // Method to loop the music continuously
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Music is looping.");
        } else {
            System.out.println("Clip is not initialized.");
        }
    }

    // Method to stop the music
    public void stop() {
        if(isPlayingRightnow){
            
        try {
            if (clip != null) {
                clip.stop(); // Stop the clip
                clip.close(); // Close the clip resources
                System.out.println("Music stopped.");
            } else {
                System.out.println("Clip is not initialized.");
            }
        } catch (Exception e) {
            // Catching any general exception that might occur
            System.out.println("An error occurred while stopping the music: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
    public void stopByID(String filePath) {
        if (clip != null && filePath.equals(currentFilePath)) {
            stop(); // Stop if the file path matches
        } else {
            System.out.println("No matching audio to stop for the given file path.");
        }
    }

    

    // Play background music continuously
    public void playBackgroundMusic(String filePath) {
        if (clip == null || !clip.isOpen()) {
            // If the clip is not already playing, initialize and start looping the background music
            new Music_class(filePath).loop();
        } else {
            System.out.println("Background music is already playing.");
        }
    }

    public static void main(String[] args) {
        // Replace with the correct path to your .wav file
        Music_class musc = new Music_class("test_package/image/Fontaine.wav");

        if (musc.clip != null) {
            // Set volume control
            FloatControl volumeControl = (FloatControl) musc.clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-30.0f); // Decrease volume

            // Start playing the music
            musc.play();

            // Stop the music after some time (optional)
            musc.stop();
        } else {
            System.out.println("Could not load the audio file.");
        }
    }

    public FloatControl getControl(Type masterGain) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getControl'");
    }
}
