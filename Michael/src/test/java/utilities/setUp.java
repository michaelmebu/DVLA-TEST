package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class setUp {

	public static WebDriver driver;
	public static LogMessage message;
	public static Logger logger;
	public static String chrome_driver;
	public static String firefox_driver;
	public static String app_url;
	public static String max_wait_time;
	public static String baseDir;

	public static void getEnvironmentProperties() throws IOException {
		InputStream input = new FileInputStream("src/test/resources/config/Config.properties");
		Properties properties = new Properties();
		properties.load(input);
		chrome_driver = properties.getProperty("chrome.browser");
		firefox_driver = properties.getProperty("firefox.browser");
		app_url = properties.getProperty("app.url");
		max_wait_time = properties.getProperty("maxwaittime");
		baseDir = properties.getProperty("baseDir");
	}

	public static void intialize() throws Exception {
		getEnvironmentProperties();

		System.setProperty("webdriver.chrome.driver", chrome_driver);
		driver = new ChromeDriver();
		driver.navigate().to(app_url);
		driver.manage().window().maximize();

		/**
		 * Starts an instance of the message logger
		 */
		try {
			message = new LogMessage("src/test/resources/logs/log-"
					+ new SimpleDateFormat("dd.MM.yyyy.HH.mm").format(new Date()) + ".xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * close the driver after running each scenario
	 */
	
	public static void teardown() {
		if (driver != null) {

			driver.quit();
		}
	}

}
