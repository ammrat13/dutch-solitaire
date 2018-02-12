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
public class GamePanel extends JPanel implements KeyListener {

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

	/** Row of the active card. */
	int rAct = 0;
	/** Column of the active card. */
	int cAct = 0;

	/** Row of the selected card. Negative one if no card is selected. */
	int rSel = -1;
	/** Column of the selected card. Negative one if no card is selected. */
	int cSel = -1;

	/** Stores the keys that are currently down so we don't count twice. */
	Set<Integer> kDs = new HashSet<>();

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
	}

	/**
	Moves the card at the specified index to the specified empty space.
	@param ri The row of the card to move
	@param ci The column of the card to move
	@param rf The row to the empty space
	@param cf The column of the empty space
	*/
	private void swap(int ri, int ci, int rf, int cf){
		try{
			System.out.println(cs[ri][ci]);
			System.out.println(cs[rf][cf+1]);
			System.out.println(cs[rf][cf-1]);
		} catch(Exception e){}
		try {
			if(
				((cs[ri][ci].num == 12 && cf == 13) // Aces can be swapped to the last row
					|| (cs[ri][ci].num == 0 && cf == 0) // Twos can be swapped to the first row
					|| (cs[ri][ci].num == cs[rf][cf+1].num - 1 && cs[ri][ci].suit == cs[rf][cf+1].suit) // Right order swap
					|| (cs[ri][ci].num == cs[rf][cf-1].num + 1 && cs[ri][ci].suit == cs[rf][cf-1].suit)) // Left order swap
				&& (cs[rf][cf] == null && cs[ri][ci] != null)	// End position is null, but start is not
			){
				cs[rf][cf] = cs[ri][ci];
				cs[ri][ci] = null; 
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
		int gW = (int) (WIDTH - 2*MAR)/14;	// Width of the space
		int gH = (int) ((gW - 2*CMAR)*AR + 2*CMAR);	// Height of the space
		for(int r=0; r<4; r++){
			for(int c=0; c<14; c++){
				// Draw the border
				g2d.setColor(Color.BLACK);
				g2d.drawRect(c*gW + MAR, r*gH + MAR, gW, gH);
				// Draw a highlight if it is selected
				g2d.setColor(Color.RED);
				if(r == rSel && c == cSel)
					g2d.fillRect(c*gW + MAR + 1, r*gH + MAR + 1, gW - 1, gH - 1);
				// Draw a highlight if it is active
				g2d.setColor(Color.BLUE);
				if(r == rAct && c == cAct)
					g2d.fillRect(c*gW + MAR + 1, r*gH + MAR + 1, gW - 1, gH - 1);
				// Draw the card if it exists
				if(cs[r][c] != null)
					g2d.drawImage(cs[r][c].img, c*gW + MAR + CMAR, r*gH + MAR + CMAR, gW - 2*CMAR, gH - 2*CMAR, null, null);
			}
		}
	}

	/** {@inheritdoc} */
	@Override
	public void keyPressed(KeyEvent e){
		if(isKeyPressed(e, KeyEvent.VK_ESCAPE)){
			reset();
		}

		if(isKeyPressed(e, KeyEvent.VK_UP)){
			rAct = Math.max(rAct-1, 0);
		}
		if(isKeyPressed(e, KeyEvent.VK_DOWN)){
			rAct = Math.min(rAct+1, 3);
		}
		if(isKeyPressed(e, KeyEvent.VK_LEFT)){
			cAct = Math.max(cAct-1, 0);
		}
		if(isKeyPressed(e, KeyEvent.VK_RIGHT)){
			cAct = Math.min(cAct+1, 13);
		}

		if(isKeyPressed(e, KeyEvent.VK_ENTER)){
			if(rSel < 0 || cSel < 0){
				rSel = rAct;
				cSel = cAct;
			} else {
				swap(rSel, cSel, rAct, cAct);
				rSel = -1;
				cSel = -1;
			}
		}

		repaint();
	}

	/** {@inheritdoc} */
	@Override
	public void keyReleased(KeyEvent e){
		kDs.remove(e.getKeyCode());
	}

	/** {@inheritdoc} */
	@Override
	public void keyTyped(KeyEvent e){

	}

	/**
	Handles the logic of checking if a key is pressed. Also adds the key to the 
	set of pressed keys if it is pressed.
	@param e The key event
	@param k The key to check
	@return If the key is pressed and hasn't triggered yet
	*/
	private boolean isKeyPressed(KeyEvent e, int k){
		if(e.getKeyCode() == k && !kDs.contains(e.getKeyCode())){
			kDs.add(e.getKeyCode());
			return true;
		}
		return false;
	}

}