package it.dpascotto.drools.examples.TicTacToe;

public enum Status {
	RESET("RESET"), // Ready to (re)start
	MOVE_TO_BE_VALIDATED("MOVE_TO_BE_VALIDATED"),
	HUMAN_MOVE_INVALID("HUMAN_MOVE_INVALID"),
	COMPUTER_MOVE_INVALID("COMPUTER_MOVE_INVALID"),
	WAITING_FOR_HUMAN_INPUT("WAITING_FOR_HUMAN_INPUT"),
	WAITING_FOR_COMPUTER_ELABORATION("WAITING_FOR_COMPUTER_ELABORATION"),
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
