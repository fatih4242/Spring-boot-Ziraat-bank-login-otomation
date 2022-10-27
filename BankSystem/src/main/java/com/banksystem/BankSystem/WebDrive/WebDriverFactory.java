package com.banksystem.BankSystem.WebDrive;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Browser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class WebDriverFactory {

	public static final ThreadLocal<WebDriver> THREAD_LOCAL_DRIVER = new ThreadLocal<>();
	
	private static List<String> BROWSER_LIST;
	private static boolean randomizeBrowser;
	private static String defaultBrowser;
	
	private static String tckimlik="", password="";
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static final Random random = new Random();
	
	@Value("#{'${test.browser.list}'.split(',')}")
	public void setBrowserList(List<String> browserList) {
		BROWSER_LIST = browserList;
	}
	
	@Value("${test.browser.randomize}")
	public void setRandomizeBrowser(boolean randomizeBrowser) {
		WebDriverFactory.randomizeBrowser = randomizeBrowser;
	}
	
	@Value("${test.browser.default}")
	public void setDefaultBrowser(String defaultBrowser) {
		WebDriverFactory.defaultBrowser = defaultBrowser;
	}
	
	public static WebDriver getDriver() {
		if(THREAD_LOCAL_DRIVER.get() != null){
			return THREAD_LOCAL_DRIVER.get();
		}else {
			System.out.println("Webdriver is null");
			throw new RuntimeException("WebDriver is null!!");
		}
	}
	
	public static void cleanUpDriver() {
		WebDriverFactory.quitDriver();
		WebDriverFactory.removeDriver();
	}
	
	public static void quitDriver() {
		if(THREAD_LOCAL_DRIVER.get() != null) {
			THREAD_LOCAL_DRIVER.get().quit();
		}
	}
	public static void removeDriver() {
		if(THREAD_LOCAL_DRIVER.get() != null) {
			THREAD_LOCAL_DRIVER.remove();
		}
	}
	
	public static void createDriver() throws InterruptedException {
		System.out.println("Lütfen Tc kimli giriniz");
		tckimlik = scanner.nextLine();
		scanner.nextLine();
		System.out.println("Lütfen Tc kimli giriniz");
		password = scanner.nextLine();
		
		String browserType = defaultBrowser;
		
		if(randomizeBrowser) {
			int randomItem = random.nextInt(BROWSER_LIST.size());
			browserType = BROWSER_LIST.get(randomItem);
		}
		
		System.out.println("Using Browser type : "+ browserType);
		if(Browser.CHROME.is(browserType)) {
			THREAD_LOCAL_DRIVER.set(createLocalChromeDriver());
		}else {//The other browser type can be added here in this class
			System.out.println("Unknown browser type entered");
			throw new RuntimeException("Unknown browser type entered");
		}
	
	}
	
	private static WebDriver createLocalChromeDriver() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();
		WebDriver webDriver = new ChromeDriver(chromeOptions);
		
		setBasicWebDriverProperties(webDriver);
		return webDriver;
	}
	
	private static void setBasicWebDriverProperties(WebDriver webDriver) throws InterruptedException {
		webDriver.manage().window().maximize();
		waitForLoad(webDriver);
		webDriver.get("https://bireysel.ziraatbank.com.tr/Transactions/Login/FirstLogin.aspx?customertype=rtl");
		waitForLoad(webDriver);
		LoginZiraat(webDriver, tckimlik, password);
	}
	private static void LoginZiraat(WebDriver webDriver, String userID, String password) throws InterruptedException {
		webDriver.findElement(By.xpath("/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[2]/div[1]/div[1]/input[1]")).sendKeys(userID);
		webDriver.findElement(By.xpath("/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[2]/div[1]/div[3]/input[1]")).sendKeys(password);
		webDriver.findElement(By.xpath("/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[2]/div[1]/div[5]/a")).click();
		waitForLoad(webDriver);
		String xpathforSecure= "/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[3]/p";
		String clickButton = "/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[2]/div/div/div[3]/button";
		String sendSms = "/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[4]/a[2]";
		while(true) {
			if(!webDriver.findElement(By.xpath(clickButton)).isDisplayed()) {
				System.out.println("click button is not enabled");
				if(webDriver.findElement(By.xpath(xpathforSecure)).isEnabled()) {
					System.out.println(webDriver.findElement(By.xpath(xpathforSecure)).getText());
				}
			}
			else {
				System.out.println("click button is enabled");
				webDriver.findElement(By.xpath(clickButton)).click();
				webDriver.findElement(By.xpath(sendSms)).click();
				break;
			}
			TimeUnit.SECONDS.sleep(1);
			
		}
		String sms = "/html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[4]/a[2]";
		webDriver.findElement(By.xpath(sms)).click();	
		System.out.println("exit while");
		///html/body/form/div[3]/div[2]/div/div/div[1]/div/div[1]/div[3]/p#Ziraat Onay girişiniz bekleniyor...
		///html/body/form/div[3]/div[2]/div/div/div[1]/div/div[2]/div/div/div[3]/button#for ok button after this will come sms 
	}

	private static void waitForLoad(WebDriver webDriver) {
		webDriver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
}
