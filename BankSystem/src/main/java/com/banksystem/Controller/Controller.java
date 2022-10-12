package com.banksystem.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import com.banksystem.BankSystem.WebDrive.WebDriverFactory;


public class Controller {

	
	
	
	@GetMapping("/")
	public void String() {
		WebDriverFactory driver = new WebDriverFactory();
		try {
			driver.createDriver();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
