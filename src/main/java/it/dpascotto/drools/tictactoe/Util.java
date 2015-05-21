package it.dpascotto.drools.tictactoe;

import java.util.Random;

public class Util {
	
	private static String[] abc = {"A", "B", "C"};

	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			System.out.println(getRandomMove());
		}

	}
	
	public static int random1_N(int N) {
		Random r = new Random();
		int x = Math.abs(r.nextInt());
		int rem = x % N;
		
		return rem + 1;
	}
	
	public static int random123() {
		return random1_N(3);
	}

	public static String randomABC() {
		int i = random123();
		return abc[i - 1];
	}
	
	public static String getRandomMove() {
		return randomABC() + random123();
	}
	
	public static Object getRandomAmong(Object... objects) {
		int dim = objects.length;
		int x = random1_N(dim);
		return objects[x - 1];
	}

}
