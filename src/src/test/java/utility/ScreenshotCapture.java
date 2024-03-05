package utility;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import config.Config;
import executionEngine.DriverScript;

public class ScreenshotCapture {
	public static WebDriver driver;
	public static String testcaseID;
	public static String teststepID;
	
	public static void setTestDetails(String testcasenumber, String teststepnumber) {
		testcaseID = testcasenumber;
		teststepID = teststepnumber;
	}
	
	public static void takeScreenShot(WebDriver driver) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			
			//FileUtils.copyFile(source, new File(Config.Base_Dir+"/src/test/resources/Screenshots/" + DriverScript.screenShotFolderName + "/" + testcaseID + "_" + teststepID + ".png"));
			FileUtils.copyFile(source, new File(Config.Base_Dir+"/Screenshots/IDM/" + DriverScript.screenShotFolderName + "/" + testcaseID + "_" + teststepID + ".png"));
		}
		catch (Exception e) {
			System.out.println("Exception while taking screenshot " + e.getMessage());
			Log.error("Exception while taking screenshot --- " + e.getMessage());
		}
	}
}