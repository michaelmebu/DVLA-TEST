package testHelpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;
import org.apache.commons.io.FilenameUtils;

import utilities.setUp;

public class FileProcessor extends setUp {

	public static void main(String[] args) throws Throwable {
		// scanDirectory("src/test/resources/TestData");
		getEnvironmentProperties();
		List<String> files = getDirectoryFiles(baseDir, "csv,java");

		for (String dirFiles : files) {
			System.out.println("The file namea are: " + dirFiles);
			validateCarRegs(dirFiles);

		}

	}

	/**
	 * get the contents of the file and match it against the ui
	 * 
	 * @param fileName
	 */
	public static void validateCarRegs(String fileName) {
		HashMap<String, String> fileContent = new HashMap<String, String>();
		fileContent = getCSVContent(baseDir + "/" + fileName);

		switch (FilenameUtils.getExtension(fileName)) {
		case "csv":
			for (String key : fileContent.keySet()) {
				System.out.println("The car reg is: " + key);
				System.out.println("The car make is: " + fileContent.get(key));
			}
			;
		case "xls":
			break;
		}
		;

	}

	/**
	 * This method scans a directory for files
	 * 
	 * @param directory
	 */
	public static void scanDirectory(String directory) {
		File fileDir = new File(directory);
		File[] foundFiles = fileDir.listFiles();
		String fileName = "";
		HashMap<String, String> fileContent = new HashMap<String, String>();

		// check if directory is empty
		if (foundFiles.length == 0) {
			System.out.println("There are no files in " + directory + "directory");
		} else {

			System.out.println("The number of files is:" + getTotalFileCount(directory));

			for (File file : foundFiles) {
				System.out.println("The file name is: " + file.getName() + "\nThe file size is: " + file.length()
						+ "\nThe file extension is: " + FilenameUtils.getExtension(file.getName()));
				fileName = file.getName();
				getFile(fileName, "java");
			}
		}
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
	public static void getFile(String file, String extension) {
		try {
			if (file.endsWith("." + extension)) {
				System.out.println("found");
			} else {
				System.out.println("not found");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	/**
	 * Gets the contents from a csv file
	 * 
	 * @param fileName
	 * @return
	 */
	public static HashMap<String, String> getCSVContent(String fileName) {

		HashMap<String, String> contents = new HashMap<String, String>();
		String row = "";
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(fileName));
			while ((row = reader.readLine()) != null) {

				// colon is used to separate the key value pair
				String[] car = row.split(":");

				contents.put(car[0], car[1]);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return contents;

	}
}
