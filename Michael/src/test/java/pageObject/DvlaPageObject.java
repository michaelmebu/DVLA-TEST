package pageObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;

import utilities.ElementProcessor;
import utilities.LogMessage;

public class DvlaPageObject extends ElementProcessor {

	public DvlaPageObject(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".gem-c-button.gem-c-button--start")
	public WebElement StartNowButton;

	@FindBy(xpath = ".//*[@id='Vrm']")
	public static WebElement inputBox;

	@FindBy(css = ".button")
	public static WebElement searchButton;

	@FindBy(css = ".reg-mark")
	public WebElement regNumberDisplay;

	@FindBy(css = ".back-to-previous.link-back")
	public static WebElement backLink;

	@FindBy(xpath = ".//*[@id='content']/div/div/a")
	public static WebElement searchAgain;

	@FindBy(css = ".reg-mark")
	public static WebElement display;

	@FindBy(css = ".list-summary-item>span>strong")
	public static WebElement vehicleDescription;

	@FindBy(xpath = ".//*[@id='pr3']/div/ul/li[3]/span[2]/strong")
	public static WebElement vehicleColour;

	@FindBy(css = ".heading-large")
	public static WebElement confirmation;

	static List<String> error = new ArrayList<String>();

	SoftAssert softAssert = new SoftAssert();
	LogMessage message;

	public void getCarDetails(String mimeType, String baseDir) throws Throwable {

		List<String> files = getDirectoryFiles(baseDir, mimeType);

		for (String dirFiles : files) {
			System.out.println("The file namea are: " + dirFiles);
			validateCarRegs(dirFiles, baseDir);

		}
	}

	/**
	 * get the contents of the file and match it against the ui
	 * 
	 * @param fileName
	 * @throws Throwable
	 */
	public void validateCarRegs(String fileName, String baseDir) throws Throwable {

		switch (FilenameUtils.getExtension(fileName)) {
		case "csv":
			validateCSVContent(baseDir + "/" + fileName);
			;
		case "xlsx":
			validateExcelContent(fileName,baseDir + "/");
		}
		;

	}

	/**
	 * This method scans a directory for files
	 * 
	 * @param directory
	 */
	public  void scanDirectory(String directory,String extension) {
		File fileDir = new File(directory);
		File[] foundFiles = fileDir.listFiles();
		Boolean found = false;
		
		// check if directory is empty
		if (foundFiles.length == 0) {
			System.out.println("There are no files in " + directory + "directory");
		} else {

			softAssert.assertTrue(getTotalFileCount(directory)>10, "There are less than 10 files in the directory");

			for (File file : foundFiles) {
				System.out.println("The file name is: " + file.getName() + "\nThe file size is: " + file.length()
						+ "\nThe file extension is: " + FilenameUtils.getExtension(file.getName()));
				if (FilenameUtils.getExtension(file.getName()).equals(extension)){
					found = true;
				}

			}
			softAssert.assertTrue(found, extension+" not found");

		}
		
		softAssert.assertAll();
	}

	/**
	 * Get all the valid files in the directory based on file extension
	 * 
	 * @param directory
	 * @param mimeType
	 * @return
	 */
	public static List<String> getDirectoryFiles(String directory, String mimeTypes) {
		File fileDir = new File(directory);
		File[] foundFiles = fileDir.listFiles();
		List<String> allFiles = new ArrayList<String>();
		List<String> mimes = Arrays.asList(mimeTypes.split(","));

		// check if directory is empty
		if (foundFiles.length == 0) {
			System.out.println("There are no files in " + directory + "directory");
		} else {

			for (File file : foundFiles) {

				for (String validMimes : mimes) {
					if (FilenameUtils.getExtension(file.getName()).equals(validMimes)) {
						allFiles.add(file.getName());
					}
				}
			}
		}

		return allFiles;
	}

	/**
	 * Process files of a particular type
	 * 
	 * @param file
	 * @param extension
	 */
	public  void getFile(String file, String extension) {
		try {
			if (file.endsWith("." + extension)) {
				System.out.println("found");
			} else {
				System.out.println("not found");
			}
		} catch (Exception e) {
			message.logMessage(e.getMessage(), Level.INFO, DvlaPageObject.class.getName(), "getFile");
		} finally {
		}
	}

	/**
	 * Process files of a particular type
	 * 
	 * @param file
	 * @param extension
	 */
	public static String getFileSize(File file) {
		return String.valueOf(file.length());
	}

	/**
	 * Return the number of files in a directory
	 * 
	 * @param directory
	 * @return
	 */
	public static int getTotalFileCount(String directory) {
		File fileDir = new File(directory);
		return fileDir.list().length;
	}

	public void validateExcelContent(String fileName,String baseDir){
		XSSFSheet sheet = getWorkSheet("Cars", fileName, baseDir);
		
		for (int iteration = 0; iteration < sheet.getLastRowNum(); iteration++) {

			sendSearchValue(sheet.getRow(iteration).getCell(0).toString());
			searchAndContinue();

			try {
				if (WaitForElementTobeVisible(confirmation).getText()
						.contains("Is this the vehicle you are looking for")) {

					softAssert.assertEquals(vehicleDescription.getText(), sheet.getRow(iteration).getCell(1).toString(),
							"Vehicle " + sheet.getRow(iteration).getCell(0).toString() + "description does not match");

					softAssert.assertEquals(vehicleColour.getText(), sheet.getRow(iteration).getCell(2).toString(),
							"Vehicle " + sheet.getRow(iteration).getCell(0).toString() + "colour does not match");

					WaitForElementTobeVisible(backLink).click();

				} else {
					softAssert.assertEquals(WaitForElementTobeVisible(confirmation).getText(),
							"Is this the vehicle you are looking for?", "Vehicle " + sheet.getRow(iteration).getCell(0).toString() + " was not found");
					WaitForElementTobeVisible(searchAgain).click();
				}
			} catch (Exception e) {
				message.logMessage(e.getMessage(), Level.INFO, DvlaPageObject.class.getName(), "validateExcelContent");

			}

		}

		softAssert.assertAll();
	}
	/**
	 * Gets the contents from a csv file
	 * 
	 * @param fileName
	 * @return
	 */
	public void validateCSVContent(String fileName) {

		HashMap<String, String> contents = new HashMap<String, String>();
		String row = "";
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(fileName));
			while ((row = reader.readLine()) != null) {

				// colon is used to separate the key value pair
				String[] car = row.split(":");

				contents.put(car[0], car[1]);

				sendSearchValue(car[0]);
				searchAndContinue();

				if (WaitForElementTobeVisible(confirmation).getText()
						.contains("Is this the vehicle you are looking for")) {

					softAssert.assertEquals(vehicleDescription.getText(), car[1],
							"Vehicle " + car[0] + "description does not match");

					softAssert.assertEquals(vehicleColour.getText(), car[2],
							"Vehicle " + car[0] + "colour does not match");

					WaitForElementTobeVisible(backLink).click();

				} else {
					softAssert.assertEquals(WaitForElementTobeVisible(confirmation).getText(),
							"Is this the vehicle you are looking for?", "Vehicle " + car[0] + " was not found");
					WaitForElementTobeVisible(searchAgain).click();
				}

			}

			softAssert.assertAll();

		} catch (Exception e) {
			message.logMessage(e.getMessage(), Level.INFO, DvlaPageObject.class.getName(), "validateCSVContent");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * process clicking the atrt button
	 */
	public void clickStartButton() {
		try {
			WaitForElementTobeVisible(StartNowButton).click();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void sendSearchValue(String values) {
		try {
			WaitForElementTobeVisible(inputBox).sendKeys(values);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void searchAndContinue() {
		try {
			WaitForElementTobeVisible(searchButton).click();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private XSSFSheet getWorkSheet(String workSheetName, String filename, String baseDir) {

		XSSFWorkbook excelWorkbook = getExcelTestData(baseDir, filename);
		XSSFSheet workSheet = excelWorkbook.getSheet(workSheetName);

		return workSheet;
	}

	public static XSSFWorkbook getExcelTestData(String baseDir, String filename) {
		try {
			File file;
			file = new File(baseDir+"/" + filename);
			
			FileInputStream filePath = new FileInputStream(file);
			XSSFWorkbook workBook = new XSSFWorkbook(filePath);
			return workBook;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

}
