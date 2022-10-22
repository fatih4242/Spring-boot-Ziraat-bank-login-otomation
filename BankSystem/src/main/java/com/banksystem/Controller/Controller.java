package com.banksystem.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banksystem.BankSystem.WebDrive.WebDriverFactory;

@RestController
public class Controller {

	
	
	@RequestMapping("/hey")
	public String sayHi(){
		try {
			new WebDriverFactory().createDriver();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hey";
	}
}
