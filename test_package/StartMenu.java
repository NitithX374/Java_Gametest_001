import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel implements ActionListener {
    private JButton startButton;
    private JButton exitButton;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Switch to the game panel
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            GameProject gamePanel = new GameProject(); // Create a new game panel instance
            topFrame.setContentPane(gamePanel); // Switch to your game panel
            topFrame.revalidate(); // Refresh the frame
            topFrame.repaint(); // Optional: repaint the frame to ensure the game displays properly
            gamePanel.requestFocusInWindow(); // Request focus for key events
        } else if (e.getSource() == exitButton) {
            System.exit(0); // Exit the game
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
