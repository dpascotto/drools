package it.dpascotto.drools.examples.TicTacToe;

import java.util.Random;

public class Util {
	
	private static String[] abc = {"A", "B", "C"};

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			System.out.println(getRandomMove());
		}

	}
	
	public static int random123() {
		Random r = new Random();
		int x = Math.abs(r.nextInt());
		int rem = x % 3;
		
		return rem + 1;
	}

	public static String randomABC() {
		int i = random123();
		return abc[i - 1];
	}
	
	public static String getRandomMove() {
		return randomABC() + random123();
	}

}
