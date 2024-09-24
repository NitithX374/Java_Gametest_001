import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class StartMenu extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton exitButton;
    protected Music_class backgroundClip; // Keep a reference to the music class

    public StartMenu() {
        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Create buttons
        startButton = new JButton("Start Game");
        exitButton = new JButton("Exit");

        // Add action listeners
        startButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Add buttons to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(startButton, gbc);

        gbc.gridy = 1;
        add(exitButton, gbc);
    }

    // @Override
    // public void actionPerformed(ActionEvent e) {
    //     if (e.getSource() == startButton) {
    //         // Change to game panel and play music in a separate thread
    //         JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    //         GameProject gamePanel = new GameProject(); 
            
    //         // Set focus to the game panel
    //         topFrame.setContentPane(gamePanel);
    //         topFrame.revalidate();
    //         topFrame.repaint();
            
    //         gamePanel.requestFocusInWindow(); // Ensure gamePanel can capture key events

    //         // Play the background music in a separate thread to avoid UI freezing
    //         new Thread(() -> {
    //             backgroundMusic = new Music_class("test_package\\image\\Fontaine.wav");
    //             backgroundMusic.play();
    //         }).start();

    //     } else if (e.getSource() == exitButton) {
    //         // Stop background music if needed here
    //         if (backgroundMusic != null) {
    //             backgroundMusic.stop(); // Stop the music when exiting
    //         }
    //         System.exit(0); // Exit the application
    //     }
    // }
    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == startButton) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GameProject gamePanel = new GameProject();
        topFrame.setContentPane(gamePanel);
        topFrame.revalidate();
        topFrame.repaint();
        gamePanel.requestFocusInWindow();
        // Stop the current music if it's playing
        if (backgroundClip != null) {
            backgroundClip.stop();
        }
        
        // Start the new music for stage_02
        backgroundClip = new Music_class("test_package\\image\\Fontaine.wav");
        FloatControl volumeControl = (FloatControl) backgroundClip.clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-30.0f); // Decrease volume
        new Thread(() -> {
            backgroundClip.play();
        }).start();

        // Set the new game panel
        
    } else if (e.getSource() == exitButton) {
        // Stop background music if needed
        if (backgroundClip != null) {
            backgroundClip.stop();
        }
        System.exit(0);
    }
}


    // Method to show the menu
    public static void showMenu(JFrame frame) {
        StartMenu menu = new StartMenu();
        frame.setContentPane(menu);
        frame.revalidate();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Game Project");
        StartMenu.showMenu(frame); // Show the start menu

        frame.setSize(1700, 800); // Set the size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
        frame.setVisible(true); // Make the window visible
    }
}
