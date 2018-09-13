package com.masteringselenium.config;

import static com.masteringselenium.config.DriverType.FIREFOX;
import static com.masteringselenium.config.DriverType.valueOf;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
	private RemoteWebDriver webDriver;
	private DriverType selectedDriverType;

	private final String operatingSystem = System.getProperty("os.name").toUpperCase();
	private final String systemArchitecture = System.getProperty("os.arch");
	private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");

	public DriverFactory() {
		DriverType driverType = FIREFOX;
		String browser = System.getProperty("browser", driverType.toString().toUpperCase());
		try {
			driverType = valueOf(browser);
		} catch (IllegalArgumentException ignored) {
			System.err.println("Uknown driver specified, defaulting to '" + driverType + "'...");
		} catch (NullPointerException ignored) {
			System.err.println("No driver specified, defaulting to '" + driverType + "'...");
		}
		selectedDriverType = driverType;
	}

	public RemoteWebDriver getDriver() throws MalformedURLException {
		if (null == webDriver) {
			instantiateWebDriver(selectedDriverType);
		}
		return webDriver;
	}

	public void quitDriver() {
		if (null != webDriver) {
			webDriver.quit();
			webDriver = null;
		}
	}

	public void instantiateWebDriver(DriverType driverType) throws MalformedURLException {
		System.out.println(" ");
		System.out.println("Local Operating System: " + operatingSystem);
		System.out.println("Local Architecture: " + systemArchitecture);
		System.out.println("Selected Browser: " + selectedDriverType);
		System.out.println("Connecting to Selenium Grid: " + useRemoteWebDriver);
		System.out.println(" ");
		System.out.println("Current thread: " + Thread.currentThread().getId());

		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

		if (useRemoteWebDriver) {
			URL seleniumGridURL = new URL(System.getProperty("gridURL"));
			String desiredBrowserVersion = System.getProperty("desiredBrowserVersion");
			String desiredPlatform = System.getProperty("desiredPlatform");
			String desiredBrowserName = System.getProperty("browser");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
			}

			if (null != desiredBrowserVersion && !desiredBrowserVersion.isEmpty()) {
				desiredCapabilities.setVersion(desiredBrowserVersion);
			}

			if (null != desiredBrowserName && !desiredBrowserName.isEmpty()) {
				desiredCapabilities.setBrowserName(desiredBrowserName);
			}

			webDriver = new RemoteWebDriver(seleniumGridURL, desiredCapabilities);

		} else {
			webDriver = selectedDriverType.getWebDriverObject(desiredCapabilities);
		}

	}

}
