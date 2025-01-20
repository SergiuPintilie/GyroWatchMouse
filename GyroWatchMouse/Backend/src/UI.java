import javax.swing.*;
import java.awt.*;

public class UI {

    public static void createUI() {
        // Create the frame
        JFrame frame = new JFrame("Mouse Control Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1));

        // Panel for Threshold
        JPanel thresholdPanel = new JPanel(new FlowLayout());
        JLabel thresholdLabel = new JLabel("Threshold: ");
        JSlider thresholdSlider = new JSlider(1, 20, (int) TestBtMouse.THRESHOLD);
        JTextField thresholdField = new JTextField(String.valueOf(TestBtMouse.THRESHOLD), 5);
        thresholdPanel.add(thresholdLabel);
        thresholdPanel.add(thresholdSlider);
        thresholdPanel.add(thresholdField);

        // Panel for Click Threshold
        JPanel clickThresholdPanel = new JPanel(new FlowLayout());
        JLabel clickThresholdLabel = new JLabel("Click Threshold: ");
        JSlider clickThresholdSlider = new JSlider(10, 250, (int) TestBtMouse.CLICK_THRESHOLD);
        JTextField clickThresholdField = new JTextField(String.valueOf(TestBtMouse.CLICK_THRESHOLD), 5);
        clickThresholdPanel.add(clickThresholdLabel);
        clickThresholdPanel.add(clickThresholdSlider);
        clickThresholdPanel.add(clickThresholdField);

        // Panel for Sensitivity
        JPanel sensitivityPanel = new JPanel(new FlowLayout());
        JLabel sensitivityLabel = new JLabel("Sensitivity: ");
        JSlider sensitivitySlider = new JSlider(1, 50, TestBtMouse.SENSITIVITY);
        JTextField sensitivityField = new JTextField(String.valueOf(TestBtMouse.SENSITIVITY), 5);
        sensitivityPanel.add(sensitivityLabel);
        sensitivityPanel.add(sensitivitySlider);
        sensitivityPanel.add(sensitivityField);

        // Add listeners for sliders
        thresholdSlider.addChangeListener(e -> {
            TestBtMouse.THRESHOLD = thresholdSlider.getValue();
            thresholdField.setText(String.valueOf(TestBtMouse.THRESHOLD));
        });

        clickThresholdSlider.addChangeListener(e -> {
            TestBtMouse.CLICK_THRESHOLD = clickThresholdSlider.getValue();
            clickThresholdField.setText(String.valueOf(TestBtMouse.CLICK_THRESHOLD));
        });

        sensitivitySlider.addChangeListener(e -> {
            TestBtMouse.SENSITIVITY = sensitivitySlider.getValue();
            sensitivityField.setText(String.valueOf(TestBtMouse.SENSITIVITY));
        });

        // Add panels to the frame
        frame.add(thresholdPanel);
        frame.add(clickThresholdPanel);
        frame.add(sensitivityPanel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
