import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import Characters.CharacterAttributes;
import Characters.Opponent_02;

public class Stage_02 extends GameProject {
    private int opponentX = 1100, opponentY = 220; // Position of the opponent
    private Image backgroundImage;
    private Image opponentImage;
    private Clip backgroundClip;
    private CharacterAttributes opponent;

    public Stage_02(CharacterAttributes player) {
        super(); // Call the constructor of GameProject
        this.player = player; // Set the player character

        // Initialize opponent
        opponent = new Opponent_02("New Enemy", 2000, Opponent_02.initializeSkills());

        // Load images
        backgroundImage = new ImageIcon("test_package\\image\\130544231.jpg").getImage();
        opponentImage = new ImageIcon("test_package\\image\\Enemy_Fatui_Pyro_Agent.jpg").getImage(); // Change as needed

        // Play background music
        playBackgroundMusic("test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav");
        FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-30.0f);

        // Set up timer for repaint
        timer = new Timer(15, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(characterImage, charX, charY, charWidth, charHeight, this); // Draw player character
        g.drawImage(opponentImage, opponentX, opponentY, charWidth, charHeight, this); // Draw opponent

        // Additional rendering logic (HP, turn count, etc.) goes here
        // Example:
        g.drawString("Your HP: " + player.HP, 50, 50);
        g.drawString("Opponent HP: " + opponent.HP, 50, 70);
    }

    // Override other methods as needed for gameplay mechanics
}
