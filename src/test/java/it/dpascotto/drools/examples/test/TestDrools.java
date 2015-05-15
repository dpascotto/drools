package it.dpascotto.drools.examples.test;

import it.dpascotto.drools.examples.TicTacToe.TicTacToeService;
import it.dpascotto.drools.examples.helloworld.HelloWorld;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = { "/test-context.xml" })

public class TestDrools {
	
	@Autowired HelloWorld helloWorld;
	@Autowired TicTacToeService tttService;
	
	@Test
	public void testHelloWorld() {
		try {
			helloWorld.sayHello("Hello, Diego");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTicTacToe() {
		try {
			tttService.startGame();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	

}
