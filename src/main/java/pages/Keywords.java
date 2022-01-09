package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import constants.path.Path;

public class Keywords {
public void click(RemoteWebDriver driver, String ObjectName, String locatorType) 
throws IOException, InterruptedException{
	Thread.sleep(1000);
	try {
		driver.findElement(this.getObject(ObjectName, locatorType)).click();
	}catch (Exception e) {
		//
	}
    }

public void enter(RemoteWebDriver driver, String ObjectName, String locatorType, String value) throws InterruptedException {
	Thread.sleep(1000);
	try {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(this.getObject(ObjectName, locatorType)));
		driver.findElement(this.getObject(ObjectName, locatorType)).click();
		new Actions(driver).sendKeys(value).build().perform();
	} catch (Exception e) {
		//
	}
}

public void assertion(RemoteWebDriver driver, String ObjectName, String locatorType, String Expected, String value) throws InterruptedException, IOException {
	boolean present = false;
	try {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(ObjectName, locatorType)));
		if (driver.findElement(this.getObject(ObjectName, locatorType)).getText().contains(value))
			present = true;
	} 
		catch (Exception e) {
			//
		}
	
	if(Expected.contains("true"))
		Assert.assertTrue(present);
	else Assert.assertFalse(present);
}
     
     
    By getObject(String ObjectName, String locatorType) throws
IOException{
//Object Repository is opened       
File file = new File(Path.CONFIG_OR_FILE_PATH);
FileInputStream fileInput = new FileInputStream(file);
     
//Properties file is read    
Properties prop = new Properties();
         prop.load(fileInput);
         
//find by xpath
if(locatorType.equalsIgnoreCase("XPATH")){
      return By.xpath(prop.getProperty(ObjectName)); 
// ObjectName is read and its value is returned
}
         
//find by class
else if(locatorType.equalsIgnoreCase("ID")){
       return By.id(prop.getProperty(ObjectName));
// ObjectName is read and its value is returned
 
}
             
//find by name
else if(locatorType.equalsIgnoreCase("NAME")){
       return By.name(prop.getProperty(ObjectName));
// ObjectName is read and its value is returned
 
}else {
}
return null;
         
    }
}