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
    private static boolean EnableSkill_01 = Skill.showSkills;
    private static boolean EnableStat_01 = CharacterAttributes.showStats;
    public Stage_02(CharacterAttributes player) {
        super();
        // this.player = player; // Set the player character
        
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
        // backgroundClip.stopByID(backgroundClip.musicID_01);
        backgroundClip = new Music_class("test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav");
        FloatControl volumeControl = (FloatControl) backgroundClip.clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(-30.0f); // Decrease volume
        new Thread(() -> {
            backgroundClip.play();
        }).start();
        // Step 1: Adding KeyListener and setting focus (Place these lines here in the constructor)
        
        // new Thread(() -> {
        //     backgroundClip = new Music_class("test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav");
        //     backgroundClip.play();
        // }).start();
    }
    
    @Override
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow();
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
        if (Skill.showSkills) {
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
        if (EnableStat_01) {
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
        boolCheck();
        super.keyPressed(e); //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        System.out.println("Key Pressed: " + e.getKeyChar());
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
            System.out.println("Hehehhehehehe");
            EnableStat_01 = !EnableStat_01; // Toggle character stats
        }
        if (key == KeyEvent.VK_H) {
            EnableSkill_01 = !EnableSkill_01; // Toggle skill list
            repaint();
        }
    }
    protected void boolCheck(){
        if(Skill.showSkills==true){
            System.out.println("Trueeeeeeeeeeeeeeeeeeeeee");
            }
        if(Skill.showSkills==false){
            System.out.println("Falseeeeeeeeeeeeeeeeeeeeeee");
            }
    }
    @Override
    public void actionPerformed(ActionEvent e){

    }
    // @Override
    // public void playMusic(boolean isPlaying){
    //     playBackgroundMusic("test_package\\image\\Rapid as Wildfires — Liyue Battle Theme I _ Genshin Impact Original Soundtrack_ Liyue Chapter.wav"); 
    //     FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
    //     volumeControl.setValue(-30.0f); // Decrease volume
    // }
    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Game Project");
    //     GameProject game_02 = new Stage_02(player);
    //     System.out.println("Skill state : " + Skill.showSkills);
    //     frame.add(game_02);
    //     frame.setSize(800, 600); // Adjust the size as needed
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setVisible(true);
    //     game_02.requestFocusInWindow(); // Ensure focus is on the panel
    // }
}
