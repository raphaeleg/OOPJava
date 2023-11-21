import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This Main program implements a simple card game that contains all instance
 * variables, GUI objects and logic.
 * 
 * @author Raphaele Guillemot
 */
public class Main {
	// INSTANCE VARIABLES
	int bank = 100; // Stores the current balance user has throughout the program runtime
	int madeBet = 0; // Temporarily stores the bet the user made
	int replaceCount = 0; // Keeps track of the cards that has been replaced in one game

	Deck deckPool = new Deck(); // A Deck component that handles the logic to give cards out

	int[] dealerCards; // Stores the raw value of the dealer's card
	int[] playerCards; // Stores the raw value of the player's card

	// GUI VARIABLES
	// Card components
	JLabel Dealer_Card1 = new JLabel(); // Dealer Card 1
	JLabel Dealer_Card2 = new JLabel(); // Dealer Card 2
	JLabel Dealer_Card3 = new JLabel(); // Dealer Card 3
	JLabel Player_Card1 = new JLabel(); // Player Card 1
	JLabel Player_Card2 = new JLabel(); // Player Card 2
	JLabel Player_Card3 = new JLabel(); // Player Card 3

	ImageIcon Back = new ImageIcon(getClass().getResource("/images/card_back.gif")); // Back image for JLabel
																							// cards

	// Replace button components
	JButton btn_rpcard1 = new JButton("Replace Card 1"); // Replace Card 1 Button
	JButton btn_rpcard2 = new JButton("Replace Card 2"); // Replace Card 1 Button
	JButton btn_rpcard3 = new JButton("Replace Card 3"); // Replace Card 1 Button

	// Bet label components
	JLabel label_bet = new JLabel(); // "Bet: $" label
	JTextField txt_inputbet = new JTextField(10); // Textfield to bet
	JButton btn_start = new JButton("Start"); // Start Button
	JButton btn_result = new JButton("Result"); // Result button

	// Status label components
	JLabel label_status = new JLabel(); // Game status label
	JLabel label_bank = new JLabel(); // Current Currency label

	// Panels
	JPanel MainPanel = new JPanel(); // Main panel
	JPanel DealerPanel = new JPanel(); // Dealer panel
	JPanel PlayerPanel = new JPanel(); // Player panel
	JPanel RpCardBtnPanel = new JPanel(); // Replace Button panel
	JPanel ButtonPanel = new JPanel(); // Button panel
	JPanel InfoPanel = new JPanel(); // Info panel

	// Menu
	JMenuBar menuBar = new JMenuBar(); // Menu Bar
	JMenu controlMenu = new JMenu("Control"); // Menu Control
	JMenuItem exitItem = new JMenuItem("Exit"); // Menu Item Exit

	JFrame frame = new JFrame(); // Frame

	/**
	 * This is the main method which initialises the game
	 * 
	 * @param args unused
	 */
	public static void main(String args[]) {
		Main game = new Main();
		game.init();
	}

	/**
	 * This method calls all the relevant elements that needs to be initialised: the
	 * deck, the GUI, and set the GUI to start a game
	 */
	public void init() {
		deckPool.initDeck();
		createGUI();
		initGUI();
	}

	/**
	 * This method sets up all the GUI that will last for the rest of the program
	 * runtime
	 */
	public void createGUI() {
		// SETTING UP
		// Adding all the Menu components to each other
		controlMenu.add(exitItem);
		menuBar.add(controlMenu);
		frame.setJMenuBar(menuBar);

		// Setting the label texts
		label_bet.setText("Bet: $");
		label_status.setText("Please place your bet!");
		label_bank.setText("Amount of money you have: $"+bank);

		// Setting all the cards to face back
		Dealer_Card1.setIcon(Back);
		Dealer_Card2.setIcon(Back);
		Dealer_Card3.setIcon(Back);
		Player_Card1.setIcon(Back);
		Player_Card2.setIcon(Back);
		Player_Card3.setIcon(Back);

		// ADDING COMPONENTS TO PANEL
		// Dealer Cards to Dealer Panel
		DealerPanel.add(Dealer_Card1);
		DealerPanel.add(Dealer_Card2);
		DealerPanel.add(Dealer_Card3);

		// Player Cards to Player Panel
		PlayerPanel.add(Player_Card1);
		PlayerPanel.add(Player_Card2);
		PlayerPanel.add(Player_Card3);

		// Replace Buttons to Replace Panel
		RpCardBtnPanel.add(btn_rpcard1);
		RpCardBtnPanel.add(btn_rpcard2);
		RpCardBtnPanel.add(btn_rpcard3);

		// Start, Result, and Bet Labels to Button Panel
		ButtonPanel.add(label_bet);
		ButtonPanel.add(txt_inputbet);
		ButtonPanel.add(btn_start);
		ButtonPanel.add(btn_result);

		// Information Labels to Info Panel
		InfoPanel.add(label_status);
		InfoPanel.add(label_bank);

		// GRID LAYOUT
		MainPanel.setLayout(new GridLayout(5, 1));
		MainPanel.add(DealerPanel);
		MainPanel.add(PlayerPanel);
		MainPanel.add(RpCardBtnPanel);
		MainPanel.add(ButtonPanel);
		MainPanel.add(InfoPanel);
		// Adding Background Colors to Dealer, Player, and Replace Panels
		DealerPanel.setBackground(Color.darkGray);
		PlayerPanel.setBackground(Color.darkGray);
		RpCardBtnPanel.setBackground(Color.darkGray);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(MainPanel);
		frame.setTitle("A Simple Card Game");
		frame.setSize(400, 700);
		frame.setVisible(true);

		// ACTION LISTENERS
		// Menu Exit
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Exit the program
				System.exit(0);
			}
		});

		// Start Button
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// A method that checks whether it's a valid bet -> starts the game
				makeBet(txt_inputbet.getText());
			}
		});

		// Replace Card 1
		btn_rpcard1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceCards(0, Player_Card1, btn_rpcard1);
			}
		});

		// Replace Card 2
		btn_rpcard2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceCards(1, Player_Card2, btn_rpcard2);
			}
		});

		// Replace Card 3
		btn_rpcard3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceCards(2, Player_Card3, btn_rpcard3);
			}
		});

		// Results Button
		btn_result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// A method that concludes the game
				resultAction();
			}
		});
	}

	/**
	 * This method sets the GUI so that it's ready for a new game. This is called
	 * every time a game ends
	 */
	public void initGUI() {
		disableBtn(btn_rpcard1);
		disableBtn(btn_rpcard2);
		disableBtn(btn_rpcard3);
		disableBtn(btn_result);
		enableBtn(btn_start);
		replaceCount = 0;
	}

	/**
	 * This method checks whether it's a valid bet, and if it is, saves the bet, and
	 * starts the game through startGame()
	 * 
	 * @param bet, which is the userInput
	 */
	public void makeBet(String bet) {
		int val = -1; // val holds the integer of the bet

		// makes sure input is a valid integer
		try {
			val = Integer.parseInt(bet);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please put a valid input!");
		}

		// checks whether the input is legal to use for bet
		if (val <= 0) { // negative
			JOptionPane.showMessageDialog(null, "Please put a valid input!");
		} else if (val > bank) { // Over possible amount
			JOptionPane.showMessageDialog(null,
					"You don't have that much money! please make a smaller bet or restart the game!");
		} else {
			madeBet = val; // save the bet amount and update statuses
			label_status.setText("Your current bet is: $" + val);
			startGame();
		}
	}

	/**
	 * A Method that starts the game by hiding the Dealer's cards, updating both
	 * dealer's and player's hand, and enabling buttons, as well as reshuffle
	 */
	public void startGame() {
		deckPool.shuffleDeck();
		// Hide Dealer's hand again
		Dealer_Card1.setIcon(Back);
		Dealer_Card2.setIcon(Back);
		Dealer_Card3.setIcon(Back);

		// Each player take initial three cards
		dealerCards = deckPool.getThree();
		playerCards = deckPool.getThree();

		// Show Player's hand
		setCardImg(playerCards[0], Player_Card1);
		setCardImg(playerCards[1], Player_Card2);
		setCardImg(playerCards[2], Player_Card3);

		// Enable everything but start button
		disableBtn(btn_start);
		enableBtn(btn_rpcard1);
		enableBtn(btn_rpcard2);
		enableBtn(btn_rpcard3);
		enableBtn(btn_result);
	}

	/**
	 * The method handling the replace card button action. Takes the next card from
	 * deck and updates image, while disabling the corresponding button
	 * 
	 * @param pos, an integer that identifies which card is being replaced
	 * @param obj, a JLabel that represents the Card to be updated
	 * @param rp,  the replace card JButton that is linked to the targeted card
	 */
	public void replaceCards(int pos, JLabel obj, JButton rp) {
		if (replaceCount < 2) { // if can still replace cards
			playerCards[pos] = deckPool.getOne(); // replace old card value with new
			setCardImg(playerCards[pos], obj); // update card image
			replaceCount++; // increment replaceCount
			disableBtn(rp); // disable the corresponding replace button
		}
		if (replaceCount == 2) { // if user has used all it's replacements
			// disable all buttons
			disableBtn(btn_rpcard1);
			disableBtn(btn_rpcard2);
			disableBtn(btn_rpcard3);
		}
	}

	/**
	 * This method concludes the game by showing the dealer's hand, updating the
	 * bank and pops up an alert of the results.
	 */
	public void resultAction() {
		// Show the Dealer cards
		setCardImg(dealerCards[0], Dealer_Card1);
		setCardImg(dealerCards[1], Dealer_Card2);
		setCardImg(dealerCards[2], Dealer_Card3);
		initGUI();

		// A method that calculates the winner and outputs it
		// 0 = dealer won, 1 = player won
		int result = calcWinner();

		// if dealer won
		if (result == 0) {
			bank -= madeBet; // Update bank
			label_bank.setText("Amount of money you have: $"+bank);

			// If there's no more money -> game over
			if (bank <= 0) {
				label_status.setText("No more money! Please start a new game!");
				JOptionPane.showMessageDialog(null, "You have no more money! Please start a new game!");
				disableBtn(btn_start); // disable start button
			} else {
				label_status.setText("You Lost!");
				JOptionPane.showMessageDialog(null, "Sorry! The Dealer wins this round!");
				label_status.setText("Please place your bet!");
			}
			// if player won
		} else {
			bank += madeBet; // Update bank
			label_status.setText("Congratulations!");
			label_bank.setText("Amount of money you have: $"+bank);
			JOptionPane.showMessageDialog(null, "Congratulations! You win this round!");
			label_status.setText("Please place your bet!");
		}
		
	}

	/**
	 * A helper method that disables buttons
	 * 
	 * @param btn, the JButton to be disabled
	 */
	public void disableBtn(JButton btn) {
		btn.setEnabled(false);
	}

	/**
	 * A helper method that enables buttons
	 * 
	 * @param btn, the JButton to be enabled
	 */
	public void enableBtn(JButton btn) {
		btn.setEnabled(true);
	}

	/**
	 * A helper method that updates a card's image
	 * 
	 * @param file, an integer that represents the card's value and therefore file
	 *              name
	 * @param card, the JLabel card that is to be updated with the new image
	 */
	public void setCardImg(int file, JLabel card) {
		ImageIcon Card_Value = new ImageIcon(getClass().getResource("/images/card_" + file + ".gif"));
		card.setIcon(Card_Value);
	}

	/**
	 * A helper method that calculates who the winner is.
	 * 
	 * @return an integer that indicated who won. 0 for Dealer, 1 for Player
	 */
	public int calcWinner() {
		int dealSp = 0; // count of dealer's special cards
		int dealTotal = 0; // sum of dealer's remaining face value cards
		int playSp = 0; // count of player's special cards
		int playTotal = 0; // sum of player's remaining face value cards

		// for all the dealer cards
		for (int i = 0; i < 3; i++) {
			dealerCards[i] = dealerCards[i] % 13; // there are 4 houses each with 13 cards
			// the special cards are in position 11, 12, 13.
			if (dealerCards[i] == 0 || dealerCards[i] == 12 || dealerCards[i] == 11) {
				dealSp++; // only count 1
			} else {
				dealTotal += dealerCards[i]; // increment thw face value
			}
		}

		// repeat above but for player
		for (int i = 0; i < 3; i++) {
			playerCards[i] = playerCards[i] % 13;
			if (playerCards[i] == 0 || playerCards[i] == 12 || playerCards[i] == 11) {
				playSp++;
			} else {
				playTotal += playerCards[i];
			}
		}

		// divide the face value sum by 10 and get it's remainder
		dealTotal = dealTotal % 10;
		playTotal = playTotal % 10;

		// CONDITIONS
		// check special cards first
		if (dealSp > playSp) {
			return 0;
		} else if (dealSp < playSp) {
			return 1;
			// check total values
		} else if (dealTotal > playTotal) {
			return 0;
		} else if (dealTotal < playTotal) {
			return 1;
		} else { // else dealer wins
			return 0;
		}
	}
}
