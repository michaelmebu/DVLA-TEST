package pageDefinition;

import org.openqa.selenium.support.PageFactory;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pageObject.DvlaPageObject;
import utilities.setUp;

public class DvlaPageDev extends setUp {

	private DvlaPageObject dvlaObject;

	public DvlaPageDev() {
		// dvlaObject= new DvlaPageObject(driver);
		dvlaObject = PageFactory.initElements(driver, DvlaPageObject.class);

	}

	
	@Given("^I have navigated to the dvla vehicle search page$")
	public void i_have_navigated_to_the_dvla_vehicle_search_page() throws Throwable {
		intialize();
	}

	@Given("^I start the vehicle search process$")
	public void i_start_the_vehicle_search_process() throws Throwable {
		dvlaObject = PageFactory.initElements(driver, DvlaPageObject.class);

		dvlaObject.clickStartButton();
	}

	@When("^I find existing \"([^\"]*)\" files$")
	public void i_find_existing_files(String extension) throws Throwable {
		dvlaObject.scanDirectory(baseDir, extension);
	}

	@Then("^The car registration from the \"([^\"]*)\" files should be found$")
	public void the_car_registration_from_the_files_should_be_found(String mimeType) throws Throwable {
		dvlaObject.getCarDetails(mimeType, baseDir);

	}

	@After
	public void close() {
		teardown();
	}
}
