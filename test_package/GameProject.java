import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import Characters.CharacterAttributes;
import Characters.Furina;
import Characters.Opponent_01;
import Characters.Opponent_02;
import Characters.Skill;

public class GameProject extends JPanel implements ActionListener, KeyListener {

    protected Timer timer;
    protected int charX = -75; // Initial position of the character
    protected int charY = 220;
    protected int opponentX = 1100, opponentY = 220; // Position of the opponent
    protected Image backgroundImage;
    protected Image characterImage;
    protected Image opponentImage; // Opponent image
    protected int charWidth = 575; // Size of the character
    protected int charHeight = 350;
    protected static Music_class backgroundClip;

    // Character stats
    protected static CharacterAttributes player;
    protected CharacterAttributes opponent;
    public boolean opponentDefeated = false; // Flag to track if the opponent is defeated
    protected String defeatMessage = ""; // Message to display when opponent is defeated
    protected String evadeMessage = ""; // Message for evaded skill // Flag to show/hide skill list
    public int turnCount = 0; // Turn counter
    protected String enemySkillMessage = ""; // Message for the enemy's skill cast
    protected Timer messageTimer;
    protected boolean shadowUsed = false; // Track if the opponent used "One with the shadow"
    protected boolean showStats = false; // Flag to show/hide character stats
    protected int currentStage = 1;
    protected String ImPath = null;
protected void getImage_(String Imagename){
    this.ImPath = Imagename;
}
    public GameProject() {
        
        player = new Furina("Furina", 100, Furina.initializeSkills());
        opponent = new Opponent_01("Enemy_01", 1500, Opponent_01.initializeSkills());
        // Timer to control the game loop, triggers every 15ms
        timer = new Timer(15, this);
        timer.start();

        // Load images
        backgroundImage = new ImageIcon("test_package\\image\\genshin_impact_4k_videos-21859.jpg").getImage(); // Background image path
        characterImage = new ImageIcon("test_package\\image\\furina-character-avatar-profile-genshin-1.jpg").getImage(); // Character image path
        opponentImage = new ImageIcon("test_package\\image\\Enemy_Fatui_Pyro_Agent.jpg").getImage(); // Opponent image path
        
        // Add KeyListener to capture user input
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        // Play background music
        // Background music path
        // playBackgroundMusic("test_package\\image\\Fontaine.wav"); 
        // FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
        // volumeControl.setValue(-30.0f); // Decrease volume
        
    }
    // public void playMusic(boolean isPlaying){
    //     playBackgroundMusic("test_package\\image\\Fontaine.wav"); 
    //     FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
    //     volumeControl.setValue(-30.0f); // Decrease volume
    // }
    // protected void stopBackgroundMusic() {
    //     if (backgroundClip != null && backgroundClip.isRunning()) {
    //         backgroundClip.stop(); // Stop the clip
    //         backgroundClip.close(); // Close the clip to release resources
    //         backgroundClip = null; // Reset the clip reference
    //     }
    // }
    // protected void stopBackgroundMusic_() {
    //     if (backgroundClip.isRunning()) {
    //         backgroundClip.stop(); // Stop the clip
    //         backgroundClip.close(); // Close the clip to release resources
    //         backgroundClip = null; // Reset the clip reference
    //     }
    // }
    
    
    
public void focusGained(FocusEvent e) {
    System.out.println("Focus gained by the component");
}


public void focusLost(FocusEvent e) {
    System.out.println("Focus lost from the component");
}

    
    private void showCharacterStats() {
        // Create a dialog to display character stats
        JDialog statsDialog = new JDialog();
        statsDialog.setTitle("Character Stats");
        statsDialog.setSize(300, 400);
        statsDialog.setLayout(new GridLayout(0, 1)); // Vertical layout

        // Add stats to the dialog
        statsDialog.add(new JLabel("HP: " + player.HP));
        statsDialog.add(new JLabel("Element: " + player.getElement())); // Assuming getElement() method exists
        statsDialog.add(new JLabel("Current Status: " + player.getStatus())); // Assuming getStatus() method exists
        statsDialog.add(new JLabel("Passive: " + player.getPassive())); // Assuming getPassive() method exists

        // Skill list
        StringBuilder skillList = new StringBuilder("Skills:\n");
        for (Skill skill : player.skills) {
            skillList.append(skill.getName()).append(" (").append(skill.getType()).append(")\n");
        }
        statsDialog.add(new JLabel(skillList.toString()));

        // Make dialog modal and visible
        statsDialog.setModal(true);
        statsDialog.setVisible(true);
    }

    // Method to play background music
    

    // Override paintComponent to draw custom graphics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
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

        // Draw messages if applicable
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
        if (Skill.showSkills) {
            System.out.println("skill pressed");
            int y = 200; // Starting Y position for skill list
            g.setColor(Color.WHITE);
            g.fillRect(680, 550, 300, 200); // Background for skill list
            g.setColor(Color.BLACK);
            g.drawRect(680, 550, 300, 200); // Border for skill list
            g.drawString("Skills:", 683, 565);
            for (int i = 0; i < player.skills.size(); i++) {
                Skill skill = player.skills.get(i);
                g.drawString((i + 1) + ". " + skill.getName() + " (" + skill.getType() + " Damage: " + skill.getDamage() + ")", 685, 550 + 40 + (i * 20));
            }
        }

        // Draw enemy skill message if applicable
        if (!enemySkillMessage.isEmpty()) {
            g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black background
            g.fillRect(getWidth() / 2 - 200, 250, 400, 50); // Background for enemy message
            g.setColor(Color.WHITE);
            g.drawString(enemySkillMessage, getWidth() / 2 - 190, 280);
        }

        // Draw character stats if visible
        if (showStats) {
            g.setColor(Color.WHITE);
            g.fillRect(50, 100, 300, 250); // Background for stats
            g.setColor(Color.BLACK);
            g.drawRect(50, 100, 300, 250); // Border for stats
            g.drawString("Character Stats:", 60, 120);
            g.drawString("HP: " + player.HP, 60, 160);
            g.drawString("Element: " + player.getElement(), 60, 180);
            g.drawString("Current Status: " + player.getStatus(), 60, 200);
            g.drawString("Passive Ability: " + player.getPassive(), 60, 220);
            g.drawString("Strength: 10", 60, 240);
            g.drawString("Dexterity: 10", 60, 260);
            g.drawString("Intelligence: 10", 60, 280);
            g.drawString("Luck: 10", 60, 300);
            g.drawString("Agility: 10", 60, 320);
            g.drawString("Cute: 999999++", 60, 340);
        }
    }

    // This method is called automatically every time the timer triggers
//     @Override
//     public void actionPerformed(ActionEvent e) {
//         if (charX + charWidth > getWidth()) {
//             // Switch to the game panel
//             JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
//             Stage_02 stage_02 = new Stage_02(player); // Create a new game panel instance
//             topFrame.setContentPane(stage_02); // Switch to your game panel
//             topFrame.revalidate(); // Refresh the frame
//             topFrame.repaint(); // Optional: repaint the frame to ensure the game displays properly
//             stage_02.requestFocusInWindow(); // Request focus for key events
        
//     }
// }

    // Handle key presses for skill activation and stats display
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            charX -= 100; // Move left by 100 pixels
            charX = Math.max(charX, 0); // Prevent going off the left edge
        }
        // Move character right
        if (key == KeyEvent.VK_RIGHT) {
            charX += 100; // Move right by 100 pixels
            charX = Math.min(charX, getWidth() - 20); // Prevent going off the right edge
        }
        if(Skill.showSkills==true){
        if (key == KeyEvent.VK_1 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(0)); // Fireball
            opponentTurn();
            turnCount++; // Increment turn count after opponent's turn
        }
        if (key == KeyEvent.VK_2 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(1)); // Skill 2
            opponentTurn();
            turnCount++;
        }
        if (key == KeyEvent.VK_3 && !opponentDefeated) {
            player.castSkill(opponent, player.skills.get(2)); // Skill 3
            opponentTurn();
            turnCount++;
        }
    }
        if (key == KeyEvent.VK_I) {
            showStats = !showStats; // Toggle character stats
        }
        if (key == KeyEvent.VK_H) {
            Skill.showSkills = !Skill.showSkills; // Toggle skill list
            if (Skill.showSkills){
                repaint();
            }
        }
    }

    private void startMessageTimer() {
        messageTimer = new Timer(3000, e -> enemySkillMessage = ""); // Clear message after 3 seconds
        messageTimer.setRepeats(false); // Only execute once
        messageTimer.start();
    }

    protected void opponentTurn() {
        if (!opponentDefeated) {
            int skillIndex = (int) (Math.random() * opponent.getSkills().size());
            Skill opponentSkill = opponent.getSkills().get(skillIndex);
            enemySkillMessage = opponentSkill.getName() + " was cast!";
    
            // Logic for opponent skill effects
            switch (opponentSkill.getName()) {
                case "Normal Attack":
                    player.HP -= 10; // Reduce player's HP for Normal Attack
                    break;
                case "One with the Shadow":
                    shadowUsed = true; // Mark that shadow skill was used
                    break;
                case "Debt Collector":
                    if (shadowUsed) {
                        player.HP -= opponentSkill.getDamage(); // Reduce player's HP if shadow skill was used
                    }
                    break;
                default:
                    // Handle other skills if needed
                    break;
            }
    
            // Check if player's HP drops to 0 or below
            if (player.HP <= 0) {
                player.HP=100;
                defeatMessage = "The cutie cannot be defeated!";
            }
    
            // Check if opponent's HP drops to 0 or below
            if (opponent.HP <= 0) {
                opponentDefeated = true; // Set the opponent as defeated
                Skill.showSkills = false;
                defeatMessage = "You have defeated the enemy!";
            }
    
            // Start message timer to clear the enemy skill message
            startMessageTimer();
        }
    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }

    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Game Project");
    //     GameProject game = new GameProject();
    //     frame.add(game);
    //     Music_class backgroundMusic = new Music_class("test_package\\image\\Fontaine.wav");
    //     backgroundMusic.play();
    //     try {
    //         Thread.sleep(1000000); // Sleep for 10 seconds
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }

    //     frame.setSize(800, 600); // Adjust the size as needed
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setVisible(true);
    // }   
    // protected void terminate() {
    //     this.backgroundClip=null;
    //     this.backgroundImage=null;
    //     if (backgroundClip != null && backgroundClip.isRunning()) {
    //         backgroundClip.stop(); // Stop the previous background music
    //         backgroundClip.close(); // Close the clip to release resources
    //     }
        
    // }
    
    // @Override
    // public void actionPerformed(ActionEvent e) {
    //     // Check if character has moved to the right edge of the stage
    //     if (charX + charWidth > getWidth()) {
            
    //         // stopBackgroundMusic();
    //         // Stop the current background music
    //         // Create a new instance of Stage_02, passing any necessary parameters           
    //         Stage_02 stage_02 = new Stage_02(player);
            
    //         // Switch to the new stage panel
    //         JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    //         topFrame.setContentPane(stage_02); // Set Stage_02 as the content pane
    //         topFrame.revalidate(); // Refresh the frame to reflect changed
    //         stage_02.requestFocusInWindow();
    //         timer.stop(); // Stop the timer if needed
    //     }
    //     // Repaint the screen to reflect any changes in the current stage
    //     repaint();
    // }
    @Override
public void actionPerformed(ActionEvent e) {
    if (charX + charWidth > getWidth()) {
        // Stop the current background music
        // if (backgroundClip != null) {
        //     backgroundClip.stop(); // Ensure you stop any playing music
        // }

        // Switch to Stage_02
        Stage_02 stage_02 = new Stage_02(player);
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setContentPane(stage_02);
        topFrame.revalidate();
        stage_02.requestFocusInWindow();
        timer.stop();
    }
    repaint();
}

}



