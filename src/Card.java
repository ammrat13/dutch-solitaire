import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
This file represents an individual card. It has utilities for processing card 
numbers and suits and also stores the image of the card. Note: {@code 0} is 
2, {@code 1} is 3 ..., {@code 8} is 10, {@code 9} is Jack, {@code 10} is 
Queen, and {@code 11} is King, and {@code 12} is Ace.
*/
public class Card {

	/** Stores the root directorry of the images */
	private final String IMGROOT = "../img/";

	/** Static variable for the clubs suit. */
	public final static int CLUBS = 0;
	/** Static variable for the diamonds suit. */
	public final static int DIAMONDS = 1;
	/** Static variable for the hearts suit. */
	public final static int HEARTS = 2;
	/** Static variable for the spades suit. */
	public final static int SPADES = 3;

	/** Instance variable storing the number of the card. */
	public final int num;
	/** Instance variable storing the suit of the card. */
	public final int suit;
	/** Instance variable storing the image of the card. */
	public final BufferedImage img;

	/**
	Constructs the card itself
	@param n The number of the card
	@param s The suit of the card
	*/
	public Card(int n, int s) throws IllegalArgumentException {
		// Error checking: n is on [0,12] and s is on [0,3]
		if(n < 0 || n > 12 || s < 0 || s > 3)
			throw new IllegalArgumentException("Invalid card.");

		num = n;
		suit = s;

		BufferedImage temp = null;
		try {
			temp = ImageIO.read(new File(getImgPath(n, s)));
		} catch (IOException e){
			temp = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			e.printStackTrace();
		}
		img = temp;
	}

	/**
	Gets the path for the image of the specified number and suit.
	@param n The number of the card
	@param s The suit of the card
	@return The path to the image of that card
	*/
	private String getImgPath(int n, int s){
		String ret = IMGROOT;

		n += 2;
		switch(n){
			case 11:
				ret += "J"; break;
			case 12:
				ret += "Q"; break;
			case 13:
				ret += "K"; break;
			case 14:
				ret += "A"; break;
			default:
				ret += n;
		}

		switch(s){
			case CLUBS:
				ret += "C"; break;
			case DIAMONDS:
				ret += "D"; break;
			case HEARTS:
				ret += "H"; break;
			case SPADES:
				ret += "S"; break;
		}
		ret += ".png";
		
		return ret;
	}

}