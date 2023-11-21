import java.util.*;

/**
* This Deck class is a special class that handles all Deck movements.
* This includes shuffling and drawing cards from the deck
* 
* @author  Raphaele Guillemot
*/
public class Deck {
	ArrayList<Integer> deck = new ArrayList<Integer>(); // deck array

	/**
	 * This method initialises the deck by setting an array from 0-52 and shuffling
	 * it. It is only called once (in init())
	 * 
	 * 
	 */
	public void initDeck() {
		for (int i = 0; i < 52; i++) {
			deck.add(i + 1);
		}
		shuffleDeck();
	}
	
	/**
	 * A Method that shuffles the deck
	 */
	public void shuffleDeck() {
		// Shuffle using Collections.shuffle method
		Collections.shuffle(deck);
	}

	/**
	 * This method draws three cards from the top of the deck. It's called at the
	 * start of every game for the dealer and player
	 * 
	 * @return integer array that holds the value of the three drawn cards
	 */
	public int[] getThree() {
		for (int i = 0; i < 3; i++) {
			deck.add(deck.get(i)); // moving the card to the bottom
			deck.remove(i); // then removing the card from the top deck
		}
		int[] result = { deck.get(49), deck.get(50), deck.get(51) };
		return result;
	}

	/**
	 * This method draws one card from the top of the deck. It's called whenever the
	 * player wishes to replace a card
	 * 
	 * @return integer that holds the value of the drawn card
	 */
	public int getOne() {
		deck.add(deck.get(0)); // moving the card to the bottom
		deck.remove(0); // then removing the card from the top deck
		return deck.get(51);
	}
}
