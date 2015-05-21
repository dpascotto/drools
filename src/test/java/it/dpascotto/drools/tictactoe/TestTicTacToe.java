package it.dpascotto.drools.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "/test-context.xml" })

public class TestTicTacToe {
	
	@Autowired TicTacToePlayer tttPlayer;
	
	
	@Test
	public void testTicTacToe() {
		try {
			tttPlayer.runStatistic(10000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	

}
