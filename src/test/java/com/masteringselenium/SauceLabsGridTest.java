package com.masteringselenium;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SauceLabsGridTest {

	public static final String USERNAME = "mrselenium144";
	public static final String ACCESS_KEY = "164cbf02-b7fb-4011-a424-5bcdf85d1cf7";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";

	public static void main(String[] args) throws Exception {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("version", "latest");

		// Your issues is with this line. Just run URL string where You previously have
		// setup everything before

		WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

		driver.get("https://saucelabs.com/test/guinea-pig");

		System.out.println("title of page is: " + driver.getTitle());

		driver.quit();
	}
}
