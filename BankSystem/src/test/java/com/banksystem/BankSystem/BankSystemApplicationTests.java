package com.banksystem.BankSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankSystemApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Passed 1");
	}
	
	@Test
	void openServer() {
		System.out.println("Server Open");
	}

}
