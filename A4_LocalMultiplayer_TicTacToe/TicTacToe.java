/**
 * This class represents the TicTacToe board, that tracks the state of the game
 * 
 * @author Raphaele
 */

public class TicTacToe {
	// Instance Variables
	private boolean P1Turn;			// whether it's currently P1's turn
	private String[][] board;		// String array representing state of the board
	private int count;				// # of slots taken on the board
	
	/**
	 * Constructor, P1 always goes first
	 * 
	 * */
	TicTacToe() {
		this.P1Turn = true;
		this.board = new String[3][3];
		this.count = 0;
	}
	
	/**
	 * Checks whether an end condition has been met- Winners, Losers, or Draw
	 * Returns 0 if the game has not yet ended
	 * Returns 1 if Player 1 won
	 * Returns 2 if Player 2 won
	 * Returns 3 if it's a draw
	 * 
	 * @return whether end condition has been met (specified in description)
	 * 
	 * */
	public int checkEndGame() {
		// 4 win cases = row, col, 2 diag
		// 1 draw case
		// System.out.println("checking whether end condition is met..."); // debug
		for (int i = 0; i < 3; i++) {
			// Row condition
			if (board[i][0] == "x" && board[i][1] == "x" && board[i][2] == "x")
				return 1;	
			
			if (board[i][0] == "o" && board[i][1] == "o" && board[i][2] == "o")
				return 2;	
			
			// Col condition
			if (board[0][i] == "x" && board[1][i] == "x" && board[2][i] == "x")
				return 1;	
			
			if (board[0][i] == "o" && board[1][i] == "o" && board[2][i] == "o")
				return 2;	
			
		}
		// Diagonal case #1
		if (board[0][0] == "x" && board[1][1] == "x" && board[2][2] == "x")
			return 1;	
		
		if (board[0][0] == "o" && board[1][1] == "o" && board[2][2] == "o")
			return 2;	
		
		// Diagonal case #2
		if (board[2][0] == "x" && board[1][1] == "x" && board[0][2] == "x")
			return 1;	
		
		if (board[2][0] == "o" && board[1][1] == "o" && board[0][2] == "o")
			return 2;	
		
		// Draw case
		if (count == 9)
			return 3;
		// System.out.println("end condition not met, continue game"); // debug
		return 0;	
	}
	
	/**
	 * Turns the current board state into a string.
	 * 
	 * @return board joined by ","
	 * 
	 * */
	public String getBoard(){
		String boardStr = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				boardStr += (board[i][j])+",";
			}
		}
		System.out.println("getBoard: "+boardStr);
		return boardStr.substring(0, boardStr.length()-1);
	}
	
	/**
	 * Check whether the player has made a valid move via mouse click
	 * 
	 * @param isPlayer1, whether the caller is Player 1
	 * @param row, row index
	 * @param col, column index
	 * @return true if the move is valid and registered
	 * 
	 * */
	public synchronized boolean makeMove(boolean isPlayer1, int row, int col) {
		// if it's not play1 but is p1 turn || if it's p1 but not p1 turn
		if ((isPlayer1 && P1Turn) || (!isPlayer1 && !P1Turn)) {
			if (board[col][row] == null) {
				// System.out.println("valid move at "+board[col][row]+" spot"); // debug
				if (isPlayer1)
					board[col][row] = "x";
				else
					board[col][row] = "o";
				count += 1;
				P1Turn = !P1Turn;
				return true;
			}
			// System.out.println("space is occupied"); // debug
		}
		// System.out.println("not valid player turn"); // debug
			return false;
		
	}
}