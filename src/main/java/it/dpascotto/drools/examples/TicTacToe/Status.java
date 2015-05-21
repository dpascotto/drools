package it.dpascotto.drools.examples.TicTacToe;

public enum Status {
	RESET("RESET"), // Ready to (re)start
	HUMAN_WINS("HUMAN_WINS"),
	COMPUTER_WINS("COMPUTER_WINS"),
	DRAW("DRAW")
	;
	
	private final String status;

    /**
     * @param status
     */
    private Status(final String status) {
        this.status = status;
    }

    
    public String toString() {
        return status;
    }
}
