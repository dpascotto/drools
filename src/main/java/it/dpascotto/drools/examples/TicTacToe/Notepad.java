package it.dpascotto.drools.examples.TicTacToe;

public class Notepad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TicTacToeGame ttt = new TicTacToeGame(XO._O());
		ttt.resetGame();
		
		
		ttt.computerMoves("C2");
		//ttt.humanMoves("C3");
		ttt.computerMoves("C3");
	
		System.out.println(ttt);
			
		//////////////////////////////////////////////
		
		int e = -1;
		for (int r = 0; r < 3; r++) {
			e = ttt.getFirstEmpty(ttt.getRow(r), 2, XO.X);
			ttt.debug("Row " + r + ", e = " + e);
			if (e >= 0) {
				ttt.debug("Computer defends in " + r + ", " + e);
				ttt.computerMoves(r, e);
				return;
			}
		}
		for (int c = 0; c < 3; c++) {
			e = ttt.getFirstEmpty(ttt.getCol(c), 2, XO.X);
			ttt.debug("Column " + c + ", e = " + e);
			if (e >= 0) {
				ttt.debug("Computer defends in " + e + ", " + c);
				ttt.computerMoves(e, c);
				return;
			}
		}
		e = ttt.getFirstEmpty(ttt.getDiagonal1(), 2, XO.X);
		ttt.debug("Diagonal 1, e = " + e);
		if (e >= 0) {
			ttt.debug("Computer defends in " + e + ", " + e);
			ttt.computerMoves(e, e);
			return;
		}
		e = ttt.getFirstEmpty(ttt.getDiagonal2(), 2, XO.X);
		ttt.debug("Diagonal 2, e = " + e);
		if (e >= 0) {
			ttt.debug("Computer defends in " + e + ", " + (2 - e));
			ttt.computerMoves(e, 2 - e);
			return;
		}
		//////////////////////////////////////////////

		
		
		
	}

	
	
	
	
}
