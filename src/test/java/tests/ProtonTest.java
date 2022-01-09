package tests;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import constants.path.Path;
import init.DriverFactory;
import listner.ReportListener;
import pages.Keywords;
import utils.FileOperations;

public class ProtonTest {
	SoftAssert softAssert;
	Logger log = null;
	DriverFactory driverFactory;
	RemoteWebDriver driver;
	Keywords keywords= new Keywords();
	FileOperations file = new FileOperations();
	
	@BeforeClass(alwaysRun = true)
	public void startUp() throws Exception {
		log = Logger.getLogger(ProtonTest.class);
		PropertyConfigurator.configure(Path.CONFIG_LOG4J_FILE_PATH);
		this.driverFactory = new DriverFactory();
		this.driver = DriverFactory.getDriver();
		DriverFactory.setDriverFactory(this.driverFactory);
	}

	@Test(dataProvider="records")
	public void submitForm(int count) throws InterruptedException, AWTException, IOException {
			log.info("submitForm() "+Integer.toString(count)+" test started");
			file.ExecuteStepsFromExcel(driver, count);
			log.info("submitForm() "+Integer.toString(count)+" test completed");
			ReportListener.logToReport("Form "+Integer.toString(count)+" submitted successfully");		
	}

	
	@DataProvider
	public  Iterator<Object[]> records() {
		ArrayList<Object[]> allTestData=new ArrayList<Object[]>();
		int totalTestcases = file.readSuiteNameUsingKeyFromExcel().size();
		for(int count = 0; count < totalTestcases; count++){

			Object obj[] = {count};
			allTestData.add(obj);
		}
		return allTestData.iterator();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		this.driver.quit();
		this.driver = null;
	}

}
