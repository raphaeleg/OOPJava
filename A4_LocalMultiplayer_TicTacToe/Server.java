import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class starts a s to play TicTacToe
 * 
 * @author Raphaele
 * 
 * */
public class Server implements Runnable {
	private static Server s;
	private TicTacToe game = new TicTacToe();
	private boolean startGame = false;
	// Socket info
	private Socket player1Sock;
	private Socket player2Sock;
	private PrintWriter player1Writer;
	private PrintWriter player2Writer;
	private BufferedReader player1Reader;
	private BufferedReader player2Reader;
	private static int PORT = 8787; 

	/**
	 * run server through go()
	 * @param args, won't be used
	 * 
	 * */
	public static void main(String[] args) {
		s = new Server();
		s.go();
	}
	/**
	 * Starts server and wait for 2 connections to initialize session.
	 * Creates 2 threads to communicate with the 2 connected clients.
	 * 
	 * */
	public void go() {
		try {
			ServerSocket serverSock = new ServerSocket(PORT);
			s.player1Sock = serverSock.accept();
			s.player1Writer = new PrintWriter(s.player1Sock.getOutputStream(),true);
			s.player1Reader = new BufferedReader(new InputStreamReader(s.player1Sock.getInputStream()));
			//System.out.println("connected"); // debug
			
			s.player2Sock = serverSock.accept();
			s.player2Writer = new PrintWriter(s.player2Sock.getOutputStream(),true);
			s.player2Reader = new BufferedReader(new InputStreamReader(s.player2Sock.getInputStream()));
			//System.out.println("connected"); // debug
			
			Thread player1 = new Thread(s);  
			player1.setName("player1");
			Thread player2 = new Thread(s);
			player2.setName("player2");
			startGame = true;
			player1.start();
			player2.start();
			player1.join();
			player2.join();
			
			serverSock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Communicate with the assigned client.
	 * 
	 * */
	public void run() {
		boolean isPlayer1 = (Thread.currentThread().getName().equals("player1"));
		System.out.println("thread started"); //debug
		String response;
		while (startGame) {
			try {
				response = read(isPlayer1);
				// System.out.println(response); //debug
				process(isPlayer1, response);
			} catch (IOException e) {
				write("alert","Game Ends. One of the players left");
				startGame = false;
			}
		}
	}

	/**
	 * Checks and handles endgame through makeMove(), checkEndGame()
	 * and updating startGame and messageLable
	 * 
	 * @param isPlayer1, whether the request is from Player 1
	 * @param response, the updated board or other exceptions (e.g. leaving the game)
	 * 
	 * */
	public void process(boolean isPlayer1, String response) throws IOException {
		if (response.equals("ok")) return;
		else if (response.equals("Left")) {
			if (isPlayer1)	write_p2("alert","Game Ends. One of the players left");
			else write_p1("alert","Game Ends. One of the players left");
		}
		// separating the response into row and col
		String[] input = response.split(",");
		int row = Integer.parseInt(input[0]);
		int col = Integer.parseInt(input[1]);
		// System.out.println("process: "+response); // debug
		// System.out.println("row:"+row+" col:"+col); // debug
		
		// check whether this was a valid move
		boolean valid = game.makeMove(isPlayer1, row, col);
		if (valid) {
			// update board
			write("board",game.getBoard());
			// System.out.println(game.getBoard()); // debug
			// check whether end game conditions has been met
			int check = game.checkEndGame();
			// 0 = no end condition, continue the game
			if (check == 0) {
				// System.out.println("normal game, please go in"); // debug
				if (isPlayer1) {
					write_p1("message", "Valid move, wait for you opponent.");
					write_p2("message","Your opponent has moved, now is your turn.");
				} else {
					write_p1("message","Your opponent has moved, now is your turn.");
					write_p2("message","Valid move, wait for you opponent.");
				}
			}
			// 1 = player 1 won
			else if (check == 1) {
				write_p1("message", "Congratulations. You Win. Please Exit the Game.");
				write_p1("alert","Congratulations. You Win.");
				write_p2("message","Sorry, You lose. Please Exit the Game.");
				write_p2("alert","Sorry, You lose.");
				startGame = false;
			}
			// 2 = player 2 won
			else if (check == 2) {
				write_p1("message","Sorry, You lose. Please Exit the Game.");
				write_p1("alert","Sorry, You lose.");
				write_p2("message", "Congratulations. You Win. Please Exit the Game.");
				write_p2("alert","Congratulations. You Win.");
				startGame = false;
			}
			// 3 = it's a draw
			else if (check == 3) {
				write("alert","It's a Draw! Please Exit the Game");
				write("message","It's a Draw! Please Exit the Game");
				startGame = false;
			}
			else {
				write("invalid","Invalid move!");
			}
		}
	}
	
	/**
	 * Sets messageLabel for player 1 in player1Writer
	 * 
	 * @param type of message
	 * @param argument, message contents
	 * 
	 * */
	public void write_p1(String type, String argument) {
		// System.out.println("p1 s: "+type+" "+argument); // debug
		player1Writer.println(type);
		player1Writer.println(argument);
	}
	
	/**
	 * Sets messageLabel for player 2 in player2Writer
	 * 
	 * @param type of message
	 * @param argument, message contents
	 * 
	 * */
	public void write_p2(String type, String argument) {
		// System.out.println("p2 s: "+type+" "+argument); // debug
		player2Writer.println(type);
		player2Writer.println(argument);
	}
	
	/**
	 * Sets messageLabel for player 2 in player2Writer
	 * 
	 * @param type of message
	 * @param argument, message contents
	 * 
	 * */
	public void write(String type, String argument) {
		// System.out.println("server: "+type+" "+argument); // debug
		player1Writer.println(type);
		player1Writer.println(argument);
		player2Writer.println(type);
		player2Writer.println(argument);
	}
	
	/**
	 * Read inputs from players
	 * 
	 * @param isPlayer1, which client has sent a message to server
	 * @return response, what the client has sent
	 * 
	 * */
	public String read(boolean isPlayer1) throws IOException {
		String response;
		if (isPlayer1) response = player1Reader.readLine();
		else response = player2Reader.readLine();
		// System.out.println("server receive "+response+" from p1? "+isPlayer1); // debug
		return response;
	}

}