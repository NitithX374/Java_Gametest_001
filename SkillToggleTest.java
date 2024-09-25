import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SkillToggleTest extends JPanel implements KeyListener {
    public static boolean showSkills = false; // Simplified toggle flag

    public SkillToggleTest() {
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow(); // Ensure focus for key events
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showSkills) {
            g.drawString("Skill List Shown", 100, 100);
        } else {
            g.drawString("Skill List Hidden", 100, 100);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_H) {
            showSkills = !showSkills; // Toggle the flag
            System.out.println("Show skills: " + showSkills); // Debug statement
            repaint(); // Trigger a repaint
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Skill Toggle Test");
        SkillToggleTest panel = new SkillToggleTest();
        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
