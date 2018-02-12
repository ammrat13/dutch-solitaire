import javax.swing.*;

/**
The main class of the project. Its only purpose is to launch the game itself.
*/
public class Main {

	/** The width of the frame. */
	private static final int WIDTH = 1162;
	/** The height of the frame. */
	private static final int HEIGHT = 648;

	public static void main(String[] args){
		JFrame frame = new JFrame();

		GamePanel gp = new GamePanel(WIDTH, HEIGHT);
		gp.addKeyListener(gp);
		gp.setFocusable(true);
		frame.add(gp);

		// Start
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
	}
}