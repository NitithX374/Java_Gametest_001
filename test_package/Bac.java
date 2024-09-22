import javax.swing.*;
import java.awt.*;
public class Bac{

}
public static void main(String[] args) {
    // Create a new JFrame (window)
    JFrame frame = new JFrame("Image Display Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1800, 900); // Set the size of the window

    // Load the image
    ImageIcon imageIcon = new ImageIcon("test_package\\image\\1347431.jpeg"); // Replace with the path to your image file
    JLabel label = new JLabel(imageIcon);

    // Add the label (with the image) to the frame
    frame.add(label);

    // Make the frame visible
    frame.setVisible(true);
}