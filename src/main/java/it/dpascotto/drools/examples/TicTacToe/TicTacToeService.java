package it.dpascotto.drools.examples.TicTacToe;

import it.dpascotto.drools.common.DroolsService;

import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tic-tac-toe")
public class TicTacToeService {
	
	@Autowired DroolsService droolsService;
	
	public void startGame() {
		TicTacToeGame ttt = new TicTacToeGame();
		ttt.resetGame();
		ttt.status = Status.WAITING_FOR_HUMAN_INPUT;
		
		StatelessKieSession kSess = droolsService.getKieSession();
		
		boolean gameIsFinished = false;
		
		while (!gameIsFinished) {
			kSess.execute(ttt);
			
			gameIsFinished = (ttt.status.equals(Status.HUMAN_WINS) ||
					ttt.status.equals(Status.COMPUTER_WINS) ||
					ttt.status.equals(Status.DRAW));
		}
		
		

	}

}
