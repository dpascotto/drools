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
			System.out.println("\r\n -------------> Firing rules...");
			kSess.execute(Arrays.asList(ttt));
			
			if (ttt.gameIsFinished()) {
				if (ttt.status.equals(Status.HUMAN_WINS)) {
					System.out.println("Nice play!");
				} else if (ttt.status.equals(Status.COMPUTER_WINS)) {
					System.out.println("Yeah! Want a rematch?");
				} else if (ttt.status.equals(Status.DRAW)) {
					System.out.println("It's such a stupid game...");
				}
				break;
			}
			
			if (ttt.isUpToHuman()) {
				ttt.acceptInputFromHuman();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		

	}

}
