package it.dpascotto.drools.examples.helloworld;

import org.springframework.stereotype.Service;

@Service ("hello-world")
public class HelloWorld {
	public static void sayHello() {
		System.out.println("Hello, fucking world");
	}
}
