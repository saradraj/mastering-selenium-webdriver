package com.masteringselenium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.masteringselenium.config.DriverFactory;
import com.masteringselenium.listeners.ScreenshotListener;

@Listeners(ScreenshotListener.class)
public class DriverBase {

	private static List<DriverFactory> webDriverThreadPool = Collections
			.synchronizedList(new ArrayList<DriverFactory>());
	private static ThreadLocal<DriverFactory> driverThread;

	@BeforeSuite(alwaysRun = true)
	public static void instantiateDriverObject() {
		driverThread = new ThreadLocal<DriverFactory>() {

			protected DriverFactory initialValue() {
				DriverFactory webDriverThread = new DriverFactory();
				webDriverThreadPool.add(webDriverThread);
				return webDriverThread;
			}
		};
	}

	public static RemoteWebDriver getDriver() throws Exception {
		return driverThread.get().getDriver();
	}

	@AfterMethod(alwaysRun = true)
	public static void clearCookies() throws Exception {
		try {
			getDriver().manage().deleteAllCookies();
		} catch (Exception ex) {
			System.err.println("Unable to clear cookies: " + ex.getCause());
		}

	}

	@AfterSuite(alwaysRun = true)
	public static void closeDriverObjects() {
		for (DriverFactory webDriverThread : webDriverThreadPool) {
			webDriverThread.quitDriver();
		}
	}

}
