import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import Characters.CharacterAttributes;
import Characters.Furina;
import Characters.Opponent_02;
import Characters.Skill;
public class Stage_02 extends GameProject{
    private int opponentX = 1100, opponentY = 220; // Position of the opponent
    public Stage_02(CharacterAttributes player) {
        super();
        terminate();
        this.player = player; // Set the player character
    
        // Initialize opponent
        opponent = new Opponent_02("New Enemy", 2000, Opponent_02.initializeSkills());
    
        // Load images
        backgroundImage = new ImageIcon("test_package\\image\\touhou-psekai-03272024.jpg").getImage();
        if (backgroundImage == null) {
            System.err.println("Error loading background image.");
        }
        opponentImage = new ImageIcon("test_package\\image\\Enemy_Fatui_Pyro_Agent.jpg").getImage();
        if (opponentImage == null) {
            System.err.println("Error loading opponent image.");
        }
    
        // Set up timer for repaint
        timer = new Timer(15, e -> repaint());
        timer.start();
    
        // Step 1: Adding KeyListener and setting focus (Place these lines here in the constructor)
        setFocusable(true); // Make sure the JPanel can receive focus
        addKeyListener(this); // Add KeyListener to the JPanel
        requestFocusInWindow(); // Request focus for key events
    }
    
    @Override
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow();
    }
    @Override
    protected void playBackgroundMusic(String filePath) {
        try {
            // Stop and close the existing clip if already playing
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
                backgroundClip.close();
            }
            
            // Initialize new clip
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
        if (showSkills) {
            System.out.println("Drawing Skill List"); // Confirm skills should be drawn
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



    
    // Override other methods as needed for gameplay mechanics
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            charX -= 100; // Move left by 5 pixels
        }
        // Move character right
        if (key == KeyEvent.VK_RIGHT) {
            charX += 100; // Move right by 5 pixels
        }
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
        if (key == KeyEvent.VK_I) {
            showStats = !showStats; // Toggle character stats
            revalidate();
            repaint();
        }
        if (key == KeyEvent.VK_H) {
            showSkills = !showSkills; // Toggle skill list
            revalidate();
            repaint();
        }
    }
    @Override
    public void playMusic(boolean isPlaying){
        playBackgroundMusic("test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav"); 
        FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
        volumeControl.setValue(-30.0f); // Decrease volume
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Project");
        Stage_02 game_02 = new Stage_02(player);
        frame.add(game_02);
        frame.setSize(800, 600); // Adjust the size as needed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game_02.requestFocusInWindow(); // Ensure focus is on the panel
        game_02.playMusic(true);
        
    }
    
}
