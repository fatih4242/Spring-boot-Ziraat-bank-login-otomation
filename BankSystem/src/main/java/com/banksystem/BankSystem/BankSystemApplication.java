package com.banksystem.BankSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.banksystem.BankSystem.WebDrive.WebDriverFactory;

@SpringBootApplication
public class BankSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankSystemApplication.class, args);
		WebDriverFactory driver = new WebDriverFactory();
		try {
			driver.createDriver();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
