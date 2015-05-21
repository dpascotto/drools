package it.dpascotto.drools.examples.TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TicTacToeGame {
	
	public boolean debug = true;
	
	public int moveCount = 0;
	public XO playsFirst;
	
	public TicTacToeGame(XO first) {
		playsFirst = first;
	}

	public XO[][] positions = new XO[3][3];
	public XO lastMoved;
	
	public Status status;
	
	public boolean isUpToComputer() {
		if (moveCount == 0) {
			return playsFirst.isO();
		} else {
			return (lastMoved != null && lastMoved.isX());
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("     A  B  C " + "\r\n");
		sb.append("-------------" + "\r\n");
		sb.append(" 1 | " + positions[0][0] + "  " + positions[0][1] + "  " + positions[0][2] + "\r\n");
		sb.append(" 2 | " + positions[1][0] + "  " + positions[1][1] + "  " + positions[1][2] + "\r\n");
		sb.append(" 3 | " + positions[2][0] + "  " + positions[2][1] + "  " + positions[2][2] + "\r\n");
		sb.append("-------------" + "\r\n");
		
		if (debug) {
			
			//sb.append("Status ............ " + status + "\r\n");
			//sb.append("Who moved last .... " + lastMoved + " (" + lastMoved.toReadable() + ")\r\n");
		}
		
		return sb.toString();
	}
	
	public void resetGame() {
		resetPositions();
		
		status = Status.RESET;
		System.out.println("Game reset:");
		System.out.println(this);
	}
	
	public void resetPositions() {
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) { 
				positions[r][c] = XO._EMPTY();
			}
		}
	}
	
	public void humanMoves(String move) {
		move(XO._X(), move);
	}
	
	public void computerMoves(String move) {
		move(XO._O(), move);
		acceptInputFromHuman();
	}
	
	public void computerMoves(int r, int c) {
		_move(XO._O(), r, c);
		acceptInputFromHuman();
	}
	
	public void debug(String message) {
		if (debug) {
			System.out.println(message);
		}
	}
	
	public void evaluateGame() {
		
	}
	
	public XO[] getRow(int r) {
		XO[] row = new XO[3];
		for (int c = 0; c < 3; c++) {
			row[c] = positions[r][c];
		}
		return row;
	}
	
	public XO[] getCol(int c) {
		XO[] col = new XO[3];
		for (int r = 0; r < 3; r++) {
			col[r] = positions[r][c];
		}
		return col;
	}
	
	public XO[] getDiagonal1() {
		XO[] diag = new XO[3];
		for (int d = 0; d < 3; d++) {
			diag[d] = positions[d][d];
		}
		return diag;
	}
	
	public XO[] getDiagonal2() {
		XO[] diag = new XO[3];
		for (int d = 0; d < 3; d++) {
			diag[d] = positions[d][2 - d];
		}
		return diag;
	}
	
	private void move(XO who, String move) {
		int[] rc = translateMove(move);
		int r = rc[0];
		int c = rc[1];
		
		System.out.println(who.toReadable() + " moves to " + move + ":");
		
		_move(who, r, c);
	}

	private void _move(XO who, int r, int c) {
		if (!positions[r][c].isEmpty()) {
			throw new InvalidMoveException("Cell is not empty: [" + r + "," + c + "]");
		}
		
		positions[r][c] = who;
		lastMoved = who;
		moveCount ++;
		System.out.println(this);
		evaluateGame();
	}
	
	public boolean isEmpty(String move) {
		int[] rc = translateMove(move);
		int r = rc[0];
		int c = rc[1];
		
		return positions[r][c].isEmpty();
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

	public void acceptInputFromHuman() {
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    try {
	    	System.out.print("Human, make your move: ");
			String move = bufferRead.readLine();
			
			humanMoves(move);
		} catch (InvalidMoveException e) {
			System.out.print("Human, your move was invalid");
			acceptInputFromHuman();
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
	public int getFirstEmpty(XO[] list, int i, String x_o) {
		int count = 0;
		int e = -1;
		for (int j = 0; j < 3; j++) {
			if (list[j].val.equals(x_o)) {
				count++;
			} else {
				if (list[j].isEmpty()) {
					if (e < 0) { // the first NOT x_o
						e = j;
					}
				}
			}
			if (count == i) {
				return e;
			}
		}
		return -1;
	}


}
