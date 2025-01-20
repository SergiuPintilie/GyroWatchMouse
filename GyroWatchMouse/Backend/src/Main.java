import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a Robot instance
            Robot robot = new Robot();

            // Scanner for user input
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter 'x y' to move the cursor to a position, or 'click' to click. Type 'exit' to quit.");
            while (true) {
                System.out.print("Command: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                if (input.equalsIgnoreCase("click")) {
                    // Perform a left mouse click
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    System.out.println("Mouse clicked.");
                }else if (input.equalsIgnoreCase("right click")) {
                    // Perform a left mouse click
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    System.out.println("Mouse right clicked.");
                }
                else {
                    // Parse coordinates
                    String[] parts = input.split(" ");
                    if (parts.length == 2) {
                        try {
                            int x = Integer.parseInt(parts[0]);
                            int y = Integer.parseInt(parts[1]);

                            // Move the cursor to the specified coordinates
                            robot.mouseMove(x, y);
                            System.out.println("Mouse moved to (" + x + ", " + y + ").");
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid coordinates. Please enter numeric values.");
                        }
                    } else {
                        System.out.println("Invalid command. Use 'x y' or 'click'.");
                    }
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
