package utilities;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementProcessor {

	protected WebDriver driver;

	public ElementProcessor(WebDriver driver){
		this.driver = driver;
	}
	
	public WebElement WaitForElementTobeVisible(WebElement webElement)  {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		webElement = wait.until(ExpectedConditions.visibilityOf(webElement));

		return webElement;
	}
	
	public WebElement WaitForElementTobeclickable(WebElement webElement) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		webElement = wait.until(ExpectedConditions.elementToBeClickable(webElement));

		return webElement;
	}
	
	public void click(WebElement element){
		element.click();
	}
	
}
