import java.io.*;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

/**
 * This class creates an instance of a Client for a player and communicates with
 * the Server through a thread. It's also the main class handling the GUI
 * 
 * @author Raphaele
 */

public class Client implements Runnable {
	// Socket info
	private Socket sock; // Socket
	private static int PORT = 8787;
	private static String SERVER = "127.0.0.1";
	private static PrintWriter writer; // writer to write messages and alerts
	private static BufferedReader reader; // reader

	// GUI
	private static JFrame frame = new JFrame();
	private JPanel ScreenPanel = new JPanel();
	private JLabel MessageLabel = new JLabel(); // message box at the top
	private JPanel TicTacToePanel = new JPanel();
	private JPanel InputPanel = new JPanel(new GridLayout(1, 2));
	// GUI Elements
	private JLabel[] board = new JLabel[9]; // board
	private JTextField nameInput = new JTextField(0);
	private JButton btn_submit = new JButton("Submit");

	private String name = "";

	/**
	 * start new Client and set it's GUI
	 * @param args, won't be used
	 * 
	 * */
	public static void main(String[] args) {
		Client c = new Client();
		// set GUI
		c.setGUI(c);
	}

	/**
	 * Set GUI of the Client
	 * 
	 * @param c, the Client object to pass to startGame
	 * 
	 */
	public void setGUI(Client c) {
		// ScreenPanel Panel
		ScreenPanel.setLayout(new BorderLayout());
		ScreenPanel.setFont(new Font("Arial", 1, 24));
		// Message Label\
		MessageLabel.setText("Please enter your player name");
		// TicTacToePanel
		TicTacToePanel.setLayout(new GridLayout(3, 3));
		Font font = new Font("Arial", 1, 100);
		// making grid slots in the TicTacToePanel
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				JLabel gridSlot = new JLabel();
				gridSlot.setPreferredSize(new Dimension(100, 100));
				gridSlot.setFont(font);
				gridSlot.setHorizontalAlignment(SwingConstants.CENTER);
				gridSlot.setBackground(Color.WHITE);
				gridSlot.setBorder(border);
				gridSlot.setEnabled(false);
				board[i + 3 * j] = gridSlot;
				final int iStr = i;
				final int jStr = j;
				gridSlot.addMouseListener(new MouseListener() {
					public void mouseClicked(MouseEvent e) {
						// coordinates
						writer.println(Integer.toString(iStr) + "," + Integer.toString(jStr));
					}
					public void mousePressed(MouseEvent e) {}
					public void mouseReleased(MouseEvent e) {}
					public void mouseEntered(MouseEvent e) {}
					public void mouseExited(MouseEvent e) {}
				});
				TicTacToePanel.add(gridSlot);
			}
		}
		// Submit button -> Start Game
		btn_submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame(c);
			}

		});

		// InputPanel JPanel
		InputPanel.add(nameInput);
		InputPanel.add(btn_submit);

		// Compiling all elements to ScreenPanel Panel
		ScreenPanel.add(MessageLabel, BorderLayout.NORTH);
		ScreenPanel.add(TicTacToePanel, BorderLayout.CENTER);
		ScreenPanel.add(InputPanel, BorderLayout.SOUTH);

		// MenuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu menu_control = new JMenu("Control");
		JMenu menu_help = new JMenu("Help");
		JMenuItem menuItem_Exit = new JMenuItem("Exit");
		JMenuItem menuItem_Instruction = new JMenuItem("Instruction");
		menu_control.add(menuItem_Exit);
		menu_help.add(menuItem_Instruction);
		menuBar.add(menu_control);
		menuBar.add(menu_help);
		// Menu Exit
		menuItem_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeClient();
			}
		});

		// Menu Instructions
		menuItem_Instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame,
						"Some information about the game:\nCriteria for a valid move:\n- The move is not occupied by any mark.\n- The move is made in the player's turn.\n- The move is made within the 3 x 3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n- Player 1 wins.\n- Player 2 wins.\n- Draw.");
			}
		});

		// JFrame
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				closeClient();
			}
	        public void windowActivated(WindowEvent e) {}
	        public void windowClosed(WindowEvent e) {}
	        public void windowOpened(WindowEvent e) {}
	        public void windowDeiconified(WindowEvent e) {}
	        public void windowIconified(WindowEvent e) {}
	        public void windowDeactivated(WindowEvent e) {}
	        });
		frame.getContentPane().add(ScreenPanel);
		frame.setJMenuBar(menuBar);
		frame.setTitle("Tic Tac Toe");
		frame.setSize(500, 600);
		frame.setVisible(true);
	}

	/**
	 * Setup to start game on client
	 * 
	 * @param c, the Client object to start a new Thread
	 * 
	 */
	public void startGame(Client c) {
		name = nameInput.getText();
		MessageLabel.setText("Welcome " + name);
		frame.setTitle("Tic Tac Toe- Player: "+name);
		nameInput.setEnabled(false);
		btn_submit.setEnabled(false);
		Thread T = new Thread(c);
		T.start();
	}
	
	/**
	 * Handles game end when one player leaves
	 * 
	 */
	public void closeClient() {
		try {
			frame.setVisible(false);
			frame.dispose();
			writer.println("Left");
            reader.close();
            sock.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
			System.exit(0);
		}
	}

	/**
	 * Connects and starts conversation with the Server through the Thread
	 * 
	 */
	public void run() {
		try {
			// Socket setup
			sock = new Socket(SERVER, PORT);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream(), true);
			// enabling the grid slots to be clicked
			for (int i = 0; i < board.length; i++) {
				board[i].setEnabled(true);
			}
			// start conversation with server
			String type, arg;
			while (true) {
				type = reader.readLine();
				arg = reader.readLine();
				// System.out.println("client receive: " + type +" "+arg); // debug
				// process received message
				if (type.equals("board"))
					displayBoard(arg);
				else if (type.equals("message"))
					MessageLabel.setText(arg);
				else if (type.equals("alert")) {
					JOptionPane.showMessageDialog(frame, arg);
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Displays the board on the client
	 * 
	 * @param str, board strings to translate to each grid slot
	 */
	public void displayBoard(String str) {
		String[] boardStr = str.split(",");
		for (int i = 0; i < 9; i++) {
			if (boardStr[i].equals("o")) {
				board[i].setForeground(new Color(51, 153, 255));
				board[i].setText("O");
			}
			if (boardStr[i].equals(null)) {
				board[i].setText("");
			}
			if (boardStr[i].equals("x")) {
				board[i].setForeground(new Color(204, 0, 0));
				board[i].setText("X");
			}
		}
	}
}
