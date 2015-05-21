package it.dpascotto.drools.examples.TicTacToe;

import java.util.Arrays;

import it.dpascotto.drools.common.DroolsService;

import org.kie.api.runtime.StatelessKieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tic-tac-toe")
public class TicTacToeService {
	
	@Autowired DroolsService droolsService;
	
	public void startGame() {
		TicTacToeGame ttt = new TicTacToeGame(XO._O());
		ttt.resetGame();
		//ttt.status = Status.WAITING_FOR_HUMAN_INPUT;
		
		StatelessKieSession kSess = droolsService.getKieSession();
		
		boolean gameIsFinished = false;
		
		
		while (!gameIsFinished) {
			kSess.execute(Arrays.asList(ttt));
			
			gameIsFinished = (ttt.status.equals(Status.HUMAN_WINS) ||
					ttt.status.equals(Status.COMPUTER_WINS) ||
					ttt.status.equals(Status.DRAW));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\r\n -------------> Restating rules...");
		}
		
		

	}

}
