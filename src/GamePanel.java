import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
The {@code JPanel} that will contain the game itself. It will be responsible for
the logic and whatnot for running the game.
*/
public class GamePanel extends JPanel implements MouseListener {

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

	/** The width of a grid space */
	private final int GW;
	/** The height of a grid space */
	private final int GH;

	/** Stores the cards we have on the table. */
	private Card[][] cs = new Card[4][14];

	/** Row of the selected card. Negative one if no card is selected. */
	private int rSel = -1;
	/** Column of the selected card. Negative one if no card is selected. */
	private int cSel = -1;

	/** Stores if we have won yet. */
	private boolean won = true;

	/**
	Constructs the panel itself.
	@param w The width of the panel
	@param h The height of the panel
	*/
	public GamePanel(int w, int h){
		WIDTH = w;
		HEIGHT = h;

		GW = (int) (WIDTH - 2*MAR)/14;
		GH = (int) ((GW - 2*CMAR)*AR + 2*CMAR);

		reset();
	}

	/**
	Resets the game. Resuffles the deck and redeals the cards.
	*/
	private void reset(){
		won = false;

		do{
			// Empty the current array. We only empty the last column as the rest 
			//	will be overwritten
			for(int r=0; r<4; r++)
				for(int c=0; c<14; c++)
					cs[r][c] = null;

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
		} while(hasWon());
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
			// Make sure the first one is not null but the second is
			if(cs[ri][ci] != null && cs[rf][cf] == null){
				// All the swap conditions
				if( (cs[ri][ci].num == 12 && cf == 13) // Aces can be swapped to the last row
				||	(cs[ri][ci].num == 0 && cf == 0) // Twos can be swapped to the first row
				||	(cs[rf][cf+1] != null && cs[ri][ci].num == cs[rf][cf+1].num - 1 && cs[ri][ci].suit == cs[rf][cf+1].suit) // Right order swap
				||	(cs[rf][cf-1] != null && cs[ri][ci].num == cs[rf][cf-1].num + 1 && cs[ri][ci].suit == cs[rf][cf-1].suit) // Left order swap
				){
					cs[rf][cf] = cs[ri][ci];
					cs[ri][ci] = null; 
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/** {@inheritdoc} */
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D) g;

		// Draw the background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		// Draw the grid and the card
		for(int r=0; r<4; r++){
			for(int c=0; c<14; c++){
				// Draw the border
				g2d.setColor(Color.BLACK);
				g2d.drawRect(c*GW + MAR, r*GH + MAR, GW, GH);
				// Draw a highlight if it is selected
				g2d.setColor(Color.RED);
				if(r == rSel && c == cSel)
					g2d.fillRect(c*GW + MAR + 1, r*GH + MAR + 1, GW - 1, GH - 1);
				// Draw the card if it exists
				if(cs[r][c] != null)
					g2d.drawImage(cs[r][c].img, c*GW + MAR + CMAR, r*GH + MAR + CMAR, GW - 2*CMAR, GH - 2*CMAR, null, null);
			}
		}

		// Draw the win phrase
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font(null, Font.BOLD, 50));
		if(won)
			g2d.drawString("You Win", WIDTH/2-110, MAR + 4*GH + MAR + 30);
	}

	/** {@inheritdoc} */
	@Override
	public void mouseClicked(MouseEvent e){
		// The cell that was clicked on
		Point cell = getGridPos(e.getPoint());
		if(cell != null && !won){
			if(rSel < 0 || cSel < 0){
				rSel = cell.y;
				cSel = cell.x;
			} else {
				swap(rSel, cSel, cell.y, cell.x);
				rSel = -1;
				cSel = -1;

				if(hasWon())
					won = true;
			}
		}

		repaint();
	}

	/** {@inheritdoc} */
	@Override
	public void mouseEntered(MouseEvent e){

	}

	/** {@inheritdoc} */
	@Override
	public void mouseExited(MouseEvent e){

	}

	/** {@inheritdoc} */
	@Override
	public void mousePressed(MouseEvent e){

	}

	/** {@inheritdoc} */
	@Override
	public void mouseReleased(MouseEvent e){

	}

	/**
	This method takes in a {@code Point} representing a clicked coordinate and 
	returns a point with the row and column of the grid posiyion that was 
	clicked. This method returns {@code null} if no grid cell was clicked.
	@param p The coordinate clicked
	@return A {@code Point} representing the row and column of the cell clicked
	*/
	private Point getGridPos(Point p){
		if(p.x < MAR || p.x > 14*GW + MAR)
			return null;
		if(p.y < MAR || p.y > 4*GH + MAR)
			return null;

		return new Point(
			(p.x - MAR)/GW,
			(p.y - MAR)/GH
		);
	}

	/**
	Determines whether the player has won. It compares the current layout to 
	determine whether the cards are in the winning position.
	@return If the player has won then {@code true}, otherwise {@code false}
	*/
	private boolean hasWon(){
		for(int r=1; r<14; r++)
			for(int c=0; c<4; c++)
				if(!cs[r][c].equals(new Card(r-1,c)))
					return false;
		return true;
	}

}