package it.dpascotto.drools.examples.TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TicTacToeGame {
	
	public boolean debug = false;

	public XO[][] positions = new XO[3][3];
	public XO lastMoved;
	
	public Status status;
	
	public boolean isUpToComputer() {
		return (lastMoved != null && lastMoved.isO());
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
			
			sb.append("Status ............ " + status + "\r\n");
			sb.append("Who moved last .... " + lastMoved + " (" + lastMoved.toReadable() + ")\r\n");
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
	
	public void evaluateGame() {
		
	}
	
	private void move(XO who, String move) {
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
		
		if (!positions[r][c].isEmpty()) {
			throw new InvalidMoveException("Cell is not empty: " + move);
		}
		
		System.out.println(who.toReadable() + " moves to " + move + ":");
		positions[r][c] = who;
		lastMoved = who;
		System.out.println(this);
		evaluateGame();
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


}
