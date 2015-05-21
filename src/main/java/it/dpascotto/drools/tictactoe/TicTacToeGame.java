package it.dpascotto.drools.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TicTacToeGame {
	
	public static final String X = "X";
	public static final String O = "O";
	public static final String EMPTY = " ";
	
	public static final String RESET = "RESET"; // Ready to (re)start
	public static final String X_WINS = "X_WINS";
	public static final String O_WINS = "O_WINS";
	public static final String DRAW = "DRAW";
	
	public boolean debug = false;
	
	public int moveCount = 0;
	public String playsFirst;
	public String lastMoved;
	
	public TicTacToeGame(String first) {
		playsFirst = first;
		resetGame();
	}

	public String[][] positions = new String[3][3];
	
	public String status;
	
	public String getWinner() {
		if (gameIsFinished()) {
			if (X_WINS.equals(status)) {
				return X;
			} else if (O_WINS.equals(status)) {
				return O;
			} else {
				return null;
			}
		}
		return null;
	}

	public boolean isUpTo(String player) {
		if (gameIsFinished()) {
			//
			//	If game is finished (someone wins or draw)
			//	nobody has to play
			//
			return false;
		}
		if (moveCount == 0) {
			//
			//	If game has just started, is up to the one marked as 'plays first'
			//
			return playsFirst.equalsIgnoreCase(player);
		} else {
			//
			//	If game is going on, is up to the other
			//
			String up2 = lastMoved.equalsIgnoreCase(X) ? O : X;
			return up2.equalsIgnoreCase(player);
		}
	
	}
	
	public String otherPlayer(String player) {
		if (player.equalsIgnoreCase(TicTacToeGame.X)) {
			return TicTacToeGame.O;
		} else if (player.equalsIgnoreCase(TicTacToeGame.O)) {
			return TicTacToeGame.X;
		}
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("     A  B  C " + "\r\n");
		sb.append("-------------" + "\r\n");
		sb.append(" 1 | " + positions[0][0] + "  " + positions[0][1] + "  " + positions[0][2] + "\r\n");
		sb.append(" 2 | " + positions[1][0] + "  " + positions[1][1] + "  " + positions[1][2] + "\r\n");
		sb.append(" 3 | " + positions[2][0] + "  " + positions[2][1] + "  " + positions[2][2] + "\r\n");
		sb.append("-------------" + "\r\n");
		
		return sb.toString();
	}
	
	public void resetGame() {
		resetPositions();
		
		status = RESET;
		
		if (debug) {
			System.out.println("Game reset:");
			System.out.println(this);
		}
	}
	
	public void resetPositions() {
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) { 
				positions[r][c] = EMPTY;
			}
		}
	}
	
	public boolean gameIsFinished() {
		return (status.equals(X_WINS) ||
				status.equals(O_WINS) ||
				status.equals(DRAW));
	}
	
	public void evaluateGame() {
		String s = null;
		for (int r = 0; r < 3; r++) {
			s = checkIfSomeoneWIns(getRow(r));
			if (s != null) {
				status = s;
				return;
			}
		}
		for (int c = 0; c < 3; c++) {
			s = checkIfSomeoneWIns(getCol(c));
			if (s != null) {
				status = s;
				return;
			}
		}
		s = checkIfSomeoneWIns(getDiagonal1());
		if (s != null) {
			status = s;
			return;
		}
		s = checkIfSomeoneWIns(getDiagonal2());
		if (s != null) {
			status = s;
			return;
		}
		if (moveCount >= 9) {
			status = DRAW;
		}
	}
	
	public String checkIfSomeoneWIns(String[] arr) {
		if (getFirstEmpty(arr, 3, X) == -1) {
			return X_WINS;
		}
		if (getFirstEmpty(arr, 3, O) == -1) {
			return O_WINS;
		}
		return null;
	}

	
	private void _print(String label, String[] arr) {
		/*
		System.out.print(label + ": ");
		for (String xo : arr) {
			System.out.print(xo + " ");
		}
		System.out.println();
		*/
	}

	public String[] getRow(int r) {
		String[] row = new String[3];
		for (int c = 0; c < 3; c++) {
			row[c] = positions[r][c];
		}
		_print("Row " + r, row);
		return row;
	}
	
	public String[] getCol(int c) {
		String[] col = new String[3];
		for (int r = 0; r < 3; r++) {
			col[r] = positions[r][c];
		}
		_print("Col " + c, col);
		return col;
	}
	
	public String[] getDiagonal1() {
		String[] diag = new String[3];
		for (int d = 0; d < 3; d++) {
			diag[d] = positions[d][d];
		}
		_print("Diag1", diag);
		return diag;
	}
	
	public String[] getDiagonal2() {
		String[] diag = new String[3];
		for (int d = 0; d < 3; d++) {
			diag[d] = positions[d][2 - d];
		}
		_print("Diag2", diag);
		return diag;
	}
	
	/**
	 * Version for 'humans' (move smthg like A1 B2 etc.)
	 * 
	 * @param who
	 * @param move
	 */
	public void move(String who, String move) {
		int[] rc = translateMove(move);
		int r = rc[0];
		int c = rc[1];
		
		System.out.println(who + " moves to " + move + ":");
		
		move(who, r, c);
	}

	public void move(String who, int r, int c) {
		if (!isUpTo(who)) {
			throw new InvalidMoveException(who + " requested to move but iy's not up to him/her");
		}
		
		if (!positions[r][c].equalsIgnoreCase(EMPTY)) {
			throw new InvalidMoveException("Cell is not empty: [" + r + "," + c + "]");
		}
		
		positions[r][c] = who;
		lastMoved = who;
		moveCount ++;
		if (debug) {
			System.out.println(this);
		}
		evaluateGame();
	}
	
	public boolean isEmpty(String move) {
		int[] rc = translateMove(move);
		int r = rc[0];
		int c = rc[1];
		
		return positions[r][c].equalsIgnoreCase(EMPTY);
	}
	
	public int[] translateMove(String move) {
		//
		//	Expecting something like 'A2'
		//
		if (move == null || move.length() > 3) {
			throw new InvalidMoveException("Your move was invalid: " + move);
		}
		int r, c = 0;
		
		String _ABC = move.substring(0, 1);
		String _123 = move.substring(1, 2);
		
		if (_ABC.equalsIgnoreCase("A")) {
			c = 0;
		} else if (_ABC.equalsIgnoreCase("B")) {
			c = 1;
		} else if (_ABC.equalsIgnoreCase("C")) {
			c = 2;
		} else {
			throw new InvalidMoveException("Your move was invalid: " + move);
		}
		
		if (_123.equalsIgnoreCase("1")) {
			r = 0;
		} else if (_123.equalsIgnoreCase("2")) {
			r = 1;
		} else if (_123.equalsIgnoreCase("3")) {
			r = 2;
		} else {
			throw new InvalidMoveException("Your move was invalid: " + move);
		}
		
		return new int[] {r, c};
	}

	public void acceptInputFromHuman__() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    try {
	    	System.out.print("Human, make your move: ");
			String move = bufferRead.readLine();
			
			//humanMoves(move);
		} catch (InvalidMoveException e) {
			System.out.print("Human, your move was invalid");
			acceptInputFromHuman__();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if in the given 3-list (row, column or diagonal)
	 * there are i occurrences of x_o
	 * 
	 * @param list
	 * @param i
	 * @param x
	 * @return
	 */
	public int getFirstEmpty(String[] list, int i, String x_o) {
		int count = 0;
		int e = -1;
		for (int j = 0; j < 3; j++) {
			if (list[j].equals(x_o)) {
				count++;
			} else {
				if (list[j].equalsIgnoreCase(EMPTY)) {
					if (e < 0) { // the first NOT x_o
						e = j;
					}
				}
			}
			if (count == i) {
				return e;
			}
		}
		return -2;
	}
	
	public static void main(String[] args) {
		TicTacToeGame tttg = new TicTacToeGame(O); // O plays first
		
		tttg.move(O, "A1");
		tttg.move(X, "A2");
		tttg.move(O, "B1");
		tttg.move(X, "B3");
		tttg.move(O, "C1");
		tttg.move(X, "B2");
	}



}
