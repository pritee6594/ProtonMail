package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import javax.naming.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.remote.RemoteWebDriver;

import constants.path.Path;
import constants.testdata.ExcelData;
import pages.Steps;

public class FileOperations {

	static String filePath = Path.TESTDATA_PATH;
	static String fileName = ExcelData.Excel_Name;
	static String sheetName = ExcelData.Sheet_Name;
	static Steps step = new Steps();

	/**
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String getConfigValue(String filePath, String key) {
		String keyValue = null;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(filePath));
			keyValue = prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return keyValue;
	}

	/**
	 * @param filePath
	 * @param key
	 * @param value
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws org.apache.commons.configuration.ConfigurationException
	 */
	public static void updateConfigValue(String filePath, String key, String value)
			throws IOException, ConfigurationException, org.apache.commons.configuration.ConfigurationException {

		PropertiesConfiguration config = new PropertiesConfiguration(filePath);

		config.setProperty(key, value);
		config.save();
	}

	public int getMaxColumn(int i) {

		/*
		 * generating maximum number of column in sheet
		 */
		File file = new File(filePath + File.separator + fileName);
		FileInputStream inputStream;
		Workbook workbook = null;

		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(readSuiteNameUsingKeyFromExcel().get(i));
		Row firstRow = sheet.getRow(0);

		return firstRow.getLastCellNum();

	}

	public ArrayList<String> readSuiteNameUsingKeyFromExcel() {
		File file = new File(filePath + File.separator + fileName);
		ArrayList<String> value = new ArrayList<String>();
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;

		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		for (int i = 1; i <= rowCount; i++) {
			row = sheet.getRow(i);
			for (int j = 0; j < firstRow.getLastCellNum(); j++) {
					if (!row.getCell(0).getStringCellValue().trim().equals("")) {
						if (firstRow.getCell(j).getStringCellValue().equalsIgnoreCase("RunMode")) {
							Cell cell = row.getCell(j);
							if(cell.getStringCellValue().equalsIgnoreCase("Y")) {
								value.add(row.getCell(j-1).getStringCellValue().toString());
							}
						}
					}

			}
		}

		return value;

	}

	@SuppressWarnings("deprecation")
	public void ExecuteStepsFromExcel(RemoteWebDriver driver, int count) throws InterruptedException, IOException {
		for(int l=1;l<=getMaxColumn(count)-4;l++) {
		File file = new File(filePath + File.separator + fileName);
		String value = null;
		FileInputStream inputStream;
		Workbook workbook = null;
		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {

		}
		Sheet worksheet = workbook.getSheet(readSuiteNameUsingKeyFromExcel().get(count));
		Row firstRow = worksheet.getRow(0);
		int row = worksheet.getLastRowNum();
		int column = worksheet.getRow(1).getLastCellNum();
		String Testdata = null;
		for (int i = 1; i <= row; i++) {
			LinkedList<String> Testexecution = new LinkedList<>();

			for (int j = 0; j < column; j++) {
				Cell Criteria = worksheet.getRow(i).getCell(j);

				String CriteriaText;
				if (Criteria == null) {
					CriteriaText = null;
				} else {
					CriteriaText = Criteria.getStringCellValue();
				}
				Testexecution.add(CriteriaText);
				if (firstRow.getCell(j).getStringCellValue().equalsIgnoreCase("TestData" + l)) {
					Cell cell = worksheet.getRow(i).getCell(j);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						value = cell.getRichStringCellValue().getString();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						value = Integer.toString(((int) cell.getNumericCellValue()));
						break;
					case Cell.CELL_TYPE_BLANK:
						value = "";
						break;
					}
				}
			}
			String Keyword = Testexecution.get(0);
			String TypeLocator = Testexecution.get(1);
			String ObjectName = Testexecution.get(2);
			String Expected = Testexecution.get(3);
			Testdata = value;

			if (i==1) {
				if (!Testdata.isBlank())
					step.perform(driver, Keyword, ObjectName, TypeLocator, Expected, Testdata);
			} else if (i > 1) {
				step.perform(driver, Keyword, ObjectName, TypeLocator, Expected, Testdata);
			}
			System.out.println("Row" + i + " is read and action performed");
		}
		System.out.println("****TEST CASE " + worksheet.getSheetName() + " is executed*****");

		}
	}

	public static void cleanDir(String dirName) {
		File directory = new File(dirName);

		// Get all files in directory
		File[] files = directory.listFiles();
		for (File file : files) {
			// Delete each file
			if (!file.delete()) {
				// Failed to delete file
				System.out.println("Failed to delete " + file);
			}
		}
	}

}
