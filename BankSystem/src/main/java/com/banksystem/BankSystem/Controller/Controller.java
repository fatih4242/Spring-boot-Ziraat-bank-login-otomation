package com.banksystem.BankSystem.Controller;

import org.springframework.web.bind.annotation.*;

import com.banksystem.BankSystem.WebDrive.WebDriverFactory;

@RestController
public class Controller {

	
	
	@RequestMapping(path = "/h")
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
