import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
The {@code JPanel} that will contain the game itself. It will be responsible for
the logic and whatnot for running the game.
*/
public class GamePanel extends JPanel {

	/** The width of the panel. */
	private final int WIDTH;
	/** The height of the panel. */
	private final int HEIGHT;

	/**
	Constructs the panel itself.
	@param w The width of the panel
	@param h The height of the panel
	*/
	public GamePanel(int w, int h){
		WIDTH = w;
		HEIGHT = h;
	}

	/** {@inheritdoc} */
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;

		// Draw the background
		g2d.setColor(Color.WHITE);
		g2d.drawRect(0, 0, WIDTH, HEIGHT);

		try {
			// Draw one card
			g2d.drawImage(ImageIO.read(new File("./img/2C.png")), 10, 10, 100, 153, null);
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}