package pages;

import java.io.IOException;

import org.openqa.selenium.remote.RemoteWebDriver;


public class Steps {
	
	Keywords keyword = new Keywords();
	
public void perform(RemoteWebDriver driver,String operation, String objectName, String typeLocator,String expected, String testdata) throws InterruptedException, IOException {
	
	switch (operation) {
	case "Enter_URL":
	driver.get(testdata);
	break;
		
	case "Enter":
	keyword.enter(driver, objectName, typeLocator, testdata);
	 
	case "Click":
	keyword.click(driver, objectName, typeLocator);

	default:
	break;
	}
	
	if(operation.contains("AssertElement")){
	 
		keyword.assertion(driver,objectName, typeLocator, expected, testdata);
	 
	}
	}
}