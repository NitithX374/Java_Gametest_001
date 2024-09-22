import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameProject extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int charX = -75, charY = 220; // Initial position of the character
    private int opponentX = 1100, opponentY = 220; // Position of the opponent
    private Image backgroundImage;
    private Image characterImage;
    private Image opponentImage; // Opponent image
    private int charWidth = 575, charHeight = 350; // Size of the character
    private Clip backgroundClip;

    // Character stats
    private CharacterAttributes player;
    private CharacterAttributes opponent;
    private boolean opponentDefeated = false; // Flag to track if the opponent is defeated
    private String defeatMessage = ""; // Message to display when opponent is defeated
    private String evadeMessage = ""; // Message for evaded skill
    private boolean showSkills = false; // Flag to show/hide skill list
    private int turnCount = 0; // Turn counter
    private String enemySkillMessage = ""; // Message for the enemy's skill cast
    private Timer messageTimer;
    private boolean shadowUsed = false; // Track if the opponent used "One with the shadow"

    public GameProject() {
        // Timer to control the game loop, triggers every 15ms
        timer = new Timer(15, this);
        timer.start();

        // Load images
        backgroundImage = new ImageIcon("test_package\\image\\161b80f45fd83e31559a2b057dc1f58e.jpg").getImage(); // Replace with your background image path
        characterImage = new ImageIcon("test_package\\image\\furina-character-avatar-profile-genshin-1.jpg").getImage(); // Replace with your character image path
        opponentImage = new ImageIcon("test_package\\image\\Enemy_Fatui_Pyro_Agent.jpg").getImage(); // Replace with your opponent image path

        // Add KeyListener to capture user input
        setFocusable(true);
        addKeyListener(this);

        // Initialize player and opponent attributes
        List<Skill> playerSkills = new ArrayList<>();
        playerSkills.add(new Skill("Fireball", "Pyro", 200));
        playerSkills.add(new Skill("Ice Blast", "Cryo", 150));
        playerSkills.add(new Skill("Thunder Strike", "Electro", 350));
        player = new CharacterAttributes("Hero", 100, playerSkills);

        List<Skill> opponentSkills = new ArrayList<>();
        opponentSkills.add(new Skill("Normal Attack", "Element One", 0)); // Damage will be handled separately
        opponentSkills.add(new Skill("One with the Shadow", "Element Two", 0)); // No direct damage
        opponentSkills.add(new Skill("Debt Collector", "Pyro", 270)); // Damage is handled after shadow skill
        opponent = new CharacterAttributes("Enemy", 1500, opponentSkills);

        // Play background music
        playBackgroundMusic("test_package\\image\\Fontaine.wav"); // Replace with your audio file path
        FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-30.0f); // Decrease volume (range: -80.0f to 6.0f)
    }

    // Method to play background music
    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing audio file: " + e.getMessage());
        }
    }

    // Override paintComponent to draw custom graphics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the character image at updated position and size
        g.drawImage(characterImage, charX, charY, charWidth, charHeight, this);

        // Draw the opponent image if not defeated
        if (!opponentDefeated) {
            g.drawImage(opponentImage, opponentX, opponentY, charWidth, charHeight, this);
        }

        // Draw turn count
        g.setColor(Color.BLACK);
        g.drawString("Turn: " + turnCount, getWidth() / 2 - 25, 30); // Centered at the top

        // Draw messages if applicable, using slightly transparent black
        g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background
        if (!defeatMessage.isEmpty()) {
            g.fillRect(getWidth() / 2 - 200, 50, 400, 50); // Background for message
            g.setColor(Color.WHITE);
            g.drawString(defeatMessage, getWidth() / 2 - 190, 80);
        }

        if (!evadeMessage.isEmpty()) {
            g.fillRect(getWidth() / 2 - 200, 110, 400, 50); // Background for evade message
            g.setColor(Color.WHITE);
            g.drawString(evadeMessage, getWidth() / 2 - 190, 140);
        }

        // Draw HP for character and opponent
        g.setColor(Color.BLACK);
        g.drawString("Your HP: " + player.HP, 50, 50);
        g.drawString("Opponent HP: " + opponent.HP, 50, 70);

        // Draw skill list if visible
        if (showSkills) {
            int y = 200; // Starting Y position for skill list
            g.setColor(Color.WHITE);
            g.fillRect(680, 550, 300, 200); // Background for skill list
            g.setColor(Color.BLACK);
            g.drawRect(680, 550, 300, 200); // Border for skill list
            g.drawString("Skills:", 683, 565);
            for (int i = 0; i < player.skills.size(); i++) {
                Skill skill = player.skills.get(i);
                g.drawString((i + 1) + ". " + skill.name + " (" + skill.element + " Damage: " + skill.damage + ")", 685, 550 + 40 + (i * 20));
            }
        }

        // Draw enemy skill message if applicable
        if (!enemySkillMessage.isEmpty()) {
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background
            g.fillRect(getWidth() / 2 - 200, 250, 400, 50); // Background for enemy message
            g.setColor(Color.WHITE);
            g.drawString(enemySkillMessage, getWidth() / 2 - 190, 280);
        }
    }

    // This method is called automatically every time the timer triggers
    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if character has moved to the right edge
        if (charX + charWidth > getWidth()) {
            // Switch to the start menu
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StartMenu.showMenu(topFrame); // Go back to the start menu
            return; // Exit the actionPerformed method
        }

        // Repaint the screen
        repaint();
    }

    // Handle key press events
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Show skills if 'H' is pressed
        if (key == KeyEvent.VK_H) {
            showSkills = !showSkills; // Toggle skill list visibility
        }

        // Use Ice Blast skill if '2' is pressed
        if (key == KeyEvent.VK_2 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(1)); // Ice Blast
            opponentTurn();
            turnCount++; // Increment turn count
        }

        // Use Fireball skill if '1' is pressed
        if (key == KeyEvent.VK_1 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(0)); // Fireball
            opponentTurn();
            turnCount++; // Increment turn count
        }

        // Use Thunder Strike skill if '3' is pressed
        if (key == KeyEvent.VK_3 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(2)); // Thunder Strike
            opponentTurn();
            turnCount++; // Increment turn count
        }

        // Modify position
        if (key == KeyEvent.VK_LEFT) {
            charX -= 10; // Move left
        }
        if (key == KeyEvent.VK_RIGHT) {
            charX += 10; // Move right
        }
        if (key == KeyEvent.VK_UP) {
            charY -= 10; // Move up
        }
        if (key == KeyEvent.VK_DOWN) {
            charY += 10; // Move down
        }
    }

    private void startMessageTimer() {
        if (messageTimer != null) {
            messageTimer.stop(); // Stop previous timer if running
        }
        messageTimer = new Timer(1000, e -> {
            enemySkillMessage = ""; // Clear message after 1 second
            messageTimer.stop(); // Stop the timer
        });
        messageTimer.setRepeats(false); // Only run once
        messageTimer.start(); // Start the timer
    }

    private void opponentTurn() {
        // Simple opponent logic
        if (!opponentDefeated) {
            // Randomly choose a skill (for simplicity)
            int skillIndex = (int) (Math.random() * opponent.skills.size());
            Skill opponentSkill = opponent.skills.get(skillIndex);

            // Set the message for the enemy's skill
            enemySkillMessage = opponentSkill.name + " was cast!";

            if (opponentSkill.name.equals("Normal Attack")) {
                player.HP -= calculateDamage(0); // No damage from Normal Attack, can add logic if needed
            } else if (opponentSkill.name.equals("One with the Shadow")) {
                shadowUsed = true; // Mark that shadow skill was used
            } else if (opponentSkill.name.equals("Debt Collector") && shadowUsed) {
                player.HP -= calculateDamage(opponentSkill.damage);
            }

            // Check if character HP drops to 0 or below
            if (player.isDefeated()) {
                player.HP = 100; // Revive character
                defeatMessage = "The cutie cannot be defeated!";
            }

            startMessageTimer(); // Start timer to clear enemy message
        }
    }

    private int calculateDamage(int baseDamage) {
        // Apply passive ability to reduce damage
        return (int) (baseDamage * 0.2); // 99% damage reduction
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    // Create the game window
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Game Project");
        GameProject game = new GameProject();

        frame.add(game);
        frame.setSize(1700, 800); // Set the size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
