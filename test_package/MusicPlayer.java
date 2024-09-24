import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer extends GameProject {

    private Clip clip;

    // Constructor to load the music file
    public MusicPlayer(String filePath) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("The audio file does not exist.");
                return;
            }

            // Get the audio stream from the file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get and open the clip for playback
            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error occurred while loading the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line for playing the clip is unavailable.");
            e.printStackTrace();
        }
    }

    // Method to start playing the music
    public void play() {
        if (clip != null) {
            clip.start();
            System.out.println("Music started.");
        }
    }

    // Method to loop the music continuously
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Music is looping.");
        }
    }

    // Method to stop the music
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            System.out.println("Music stopped.");
        }
    }

    // Main method to test the class
    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer("test_package\\image\\Fontaine.wav");
        player.play();  // Play the music

        // To loop the music continuously
        // player.loop();
        
        // To stop the music after a delay, e.g., 10 seconds (for demonstration)
        try {
            Thread.sleep(1000000); // Sleep for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

