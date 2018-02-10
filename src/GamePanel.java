import java.awt.*;
import java.io.*;
import java.util.*;
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

	/** The margin on each side in pixels. */
	private final int MAR = 50;
	/** The ratio of the cards' length to their width. */
	private final double AR = 1056.0/691.0;
	/** The margin around each card in pixels. */
	private final int CMAR = 5;

	/** Stores the cards we have on the table. */
	Card[][] cs = new Card[4][14];

	/**
	Constructs the panel itself.
	@param w The width of the panel
	@param h The height of the panel
	*/
	public GamePanel(int w, int h){
		WIDTH = w;
		HEIGHT = h;
		reset();
	}

	/**
	Resets the game. Resuffles the deck and redeals the cards.
	*/
	private void reset(){
		// Empty the current array. We only empty the last column as the rest 
		//	will be overwritten
		for(int r=0; r<4; r++)
			cs[r][13] = null;


		// Fill the array randomly
		ArrayList<Card> ctemp = new ArrayList<Card>();
		for(int n=0; n<13; n++)
			for(int s=0; s<4; s++)
				ctemp.add(new Card(n, s));
		Collections.shuffle(ctemp);
		for(int r=0; r<4; r++)
			for(int c=0; c<13; c++)
				cs[r][c] = ctemp.get(r*13+c);

		// Find all the aces and swap them to their right position
		for(int r=0; r<4; r++)
			for(int c=0; c<13; c++)
				if(cs[r][c].num == 12)
					swap(r, c, cs[r][c].suit, 13);
	}

	/**
	Moves the card at the specified index to the specified empty space.
	@param ri The row of the card to move
	@param ci The column of the card to move
	@param rf The row to the empty space
	@param cf The column of the empty space
	*/
	private void swap(int ri, int ci, int rf, int cf){
		try {
			if(
				((cs[ri][ci].num == 12 && cf == 13) // Aces can be swapped to the last row
					|| (cs[ri][ci].num == 0 && cf == 0) // Twos can be swapped to the first row
					|| (cs[ri][ci].num == cs[ri][ci+1].num - 1 && cs[ri][ci].suit == cs[ri+1][ci].suit) // Right order swap
					|| (cs[ri][ci].num == cs[ri][ci-1].num + 1 && cs[ri][ci].suit == cs[ri-1][ci].suit)) // Left order swap
				&& cs[rf][cf] == null	// End position is null
			){
				cs[rf][cf] = cs[ri][ci];
				cs[ri][ci] = null; 
			}
		} catch(NullPointerException | IndexOutOfBoundsException  e){
			e.printStackTrace();
		}
	}

	/** {@inheritdoc} */
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;

		// Draw the background
		g2d.setColor(Color.WHITE);
		g2d.drawRect(0, 0, WIDTH, HEIGHT);

		// Draw the grid and the card
		g2d.setColor(Color.BLACK);
		int gW = (WIDTH - 2*MAR)/14;
		int gH = (int) ((gW - 2*CMAR)*AR + 2*CMAR);
		for(int r=0; r<4; r++){
			for(int c=0; c<14; c++){
				g2d.drawRect(c*gW + MAR, r*gH + MAR, gW, gH);
				if(cs[r][c] != null)
					g2d.drawImage(cs[r][c].img, c*gW + MAR + CMAR, r*gH + MAR + CMAR, gW - 2*CMAR, gH - 2*CMAR, null, null);
			}
		}
	}

}