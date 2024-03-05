package library;

import static executionEngine.DriverScript.OR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
//import org.openqa.selenium
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.comparator.OcularResult;

import config.Config;
import executionEngine.DriverScript;

import utility.Log;
import utility.ScreenshotCapture;
import utility.Highlight;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ByAll;
//import org.openqa.selenium.p

public class ActionKeywords<IWebElement> {
	public static WebDriver driver;
	public static WebDriverWait wait;
	private static int defaultWait = 25;
	public static HashMap hm = new HashMap();

	public static WebElement objectLocator(String object) {
		if (object.endsWith("_id"))
			return driver.findElement(By.id(OR.getProperty(object)));
		if (object.endsWith("_class"))
			return driver.findElement(By.className(OR.getProperty(object)));
		if (object.endsWith("_xpath"))
			return driver.findElement(By.xpath(OR.getProperty(object)));
		if (object.endsWith("_css"))
			return driver.findElement(By.cssSelector(OR.getProperty(object)));
		if (object.endsWith("_link"))
			return driver.findElement(By.linkText(OR.getProperty(object)));
		if (object.endsWith("_name"))
			return driver.findElement(By.name(OR.getProperty(object)));
		if (object.endsWith("_tag"))
			return driver.findElement(By.tagName(OR.getProperty(object)));
		if (object.endsWith(")")) {
			String[] obj = object.split("\\(");
			String replaceChar = obj[1].replace(")", "").trim();
			String rObj = OR.getProperty(obj[0].trim()).trim();
			String replacedObj = null;
			if(replaceChar.contains(",")){
				String[] repChar = replaceChar.split(",");
				replacedObj = modifyLocator(rObj,repChar[0],repChar[1]);	
			}else{
				replacedObj = modifyLocator(rObj,replaceChar);
			
			}
			return driver.findElement(By.xpath(replacedObj));
		}
		return null;
		
	}
	
	public static void clear(String objLocator, String replaceString) {
		Log.info("Entering the text in " + objLocator);
		objectLocator(objLocator).clear();
		waitFor(objLocator,replaceString);
		//driver.findElement(new ByAll());
	}
	
	public static String modifyLocator(String objLocator, String replaceString) {
		if (objLocator != null && replaceString != null) {
			String Str = new String(objLocator);
			String mStr = Str.replace("ReplaceString", replaceString);
			return mStr;
		} else {
			Log.error("Replacing string in xpath is failed");
			return null;
		}
	}
	
	public static String modifyLocator(String objLocator, String replaceString1, String replaceString2) {
		if (objLocator != null && replaceString1 != null && replaceString2 != null) {
			String Str = new String(objLocator);
			String mStr1 = Str.replace("ReplaceString1", replaceString1);
			String mStr2 = mStr1.replace("ReplaceString2", replaceString2);
			return mStr2;
		} else {
			Log.error("Replacing string in xpath is failed");
			return null;
		}
	}
	
	

 	@SuppressWarnings("deprecation")
	public static void openBrowser(String object,String data){
		try{	
//			// For browser stack execution
			String brwsStackURL = "";
			String data2=null;
			String data3=null;
			if(data.contains(",")){
				String[] parArray = data.split(",");
				data=parArray[0];
				data2 = parArray[1];
				if(parArray.length > 2){
					data3 = parArray[2];	
				}
			} 
			if(data.equalsIgnoreCase("chrome")){
				//System.setProperty("webdriver.chrome.driver", Config.Base_Dir+"\\lib\\chromedriver.exe");
				WebDriverManager.chromedriver().setup();
				driver=new ChromeDriver();
				driver.manage().window().maximize();
				//driver.manage().window().setSize(new Dimension(411,700));
				//driver.manage().window().setSize(new Dimension(768,1024));
				Log.info("Chrome browser started");	
				driver.get(Config.Base_Dir+"/src/test/resources/Config/Web/openingscreen.html");
				
	
			}
			if(data.equalsIgnoreCase("firefox")){
				///System.setProperty("webdriver.gecko.driver", Config.Base_Dir+"\\lib\\geckodriver.exe");
				WebDriverManager.firefoxdriver().setup();
				driver=new FirefoxDriver();
				//driver.manage().window().maximize();
				driver.manage().window().setSize(new Dimension(1366,768));
				Log.info("Firefox browser started");
			}
			if(data.equalsIgnoreCase("ie")){
				//System.setProperty("webdriver.ie.driver", Config.Base_Dir+"\\lib\\IEDriverServer.exe");
				WebDriverManager.iedriver().setup();
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING,true);
				ieCapabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
				ieCapabilities.setCapability("nativeEvents", false);
				ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
				//ieCapabilities.setJavascriptEnabled(true);
				ieCapabilities.setCapability("ie.ensureCleanSession", true);
				ieCapabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,"https://www.google.com/");
				driver = new InternetExplorerDriver(ieCapabilities);
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
				//driver.get("file///" + Config.Base_Dir+"/src/test/resources/Config/Web/openingscreen.html");
				Log.info("IE browser started");
			}
			if(data.equalsIgnoreCase("opera")){
				System.setProperty("webdriver.chrome.driver", Config.Base_Dir+"\\lib\\operadriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.setBinary("C:\\Program Files\\Opera\\52.0.2871.30\\opera.exe");
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				driver=new ChromeDriver(capabilities);
				driver.manage().window().maximize();
				Log.info("Opera browser started");
			}
			if(data.equalsIgnoreCase("windows")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("os", "Windows");
				caps.setCapability("os_version", "10");
				caps.setCapability("browser",data2);
				if(!data3.isEmpty()){
					caps.setCapability("browser_version",data3);
				}
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}
			if(data.equalsIgnoreCase("mac")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("os", "OS X");
				caps.setCapability("os_version", "High Sierra");
				caps.setCapability("browser",data2);
				if(!data3.isEmpty()){
					caps.setCapability("browser_version",data3);
				}
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}
			if(data.equalsIgnoreCase("andriod")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("realMobile", "true");
				caps.setCapability("platform", "ANDROID");
				caps.setCapability("device",data2);
				//caps.setCapability("device", "Samsung Galaxy Note 8");
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}
			if(data.equalsIgnoreCase("andriod tab")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("realMobile", "true");
				caps.setCapability("platform", "ANDROID");
				caps.setCapability("device",data2);
				//caps.setCapability("device", "Samsung Galaxy Tab 4");
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}
			if(data.equalsIgnoreCase("ios")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("realMobile", "true");
				caps.setCapability("platform", "MAC");
				caps.setCapability("device",data2);
				//caps.setCapability("device", "iPhone 8");
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}if(data.equalsIgnoreCase("ios tab")){
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability("realMobile", "true");
				caps.setCapability("platform", "MAC");
				caps.setCapability("device",data2);
				//caps.setCapability("device", "iPad Pro");
				caps.setCapability("browserstack.local", "true");
				caps.setCapability("browserstack.debug", "true");
				java.net.URL myURL = new java.net.URL(brwsStackURL);
				driver = new RemoteWebDriver(myURL,caps);
			}
			int implicitWaitTime=(10);
			driver.manage().timeouts().implicitlyWait(implicitWaitTime, TimeUnit.SECONDS);
		}catch (Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.info("Not able to open the Browser --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void navigateURL(String object, String data){
		try{
			Log.info("Navigating to URL");
			System.out.println("Value = " + DriverScript.Auto_Url);
			if (!DriverScript.Auto_Url.equalsIgnoreCase("$IDM_Urls")){
				driver.get(DriverScript.Auto_Url);
				Log.info("Test Script URL is overridden by default URL provided in config file");
				System.out.println("Test Script URL is overridden by default URL provided in config file");
			} else {
				driver.get(data);
				Log.info("URL present in test script used");
				System.out.println("URL present in test script used");
			}
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.info("Not able to navigate --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void clickElement(String object, String data){		
		try{
			Log.info("Clicking on Webelement "+ object);
			//driver.findElement(By.xpath("Value")).click();
			WebElement element =  objectLocator(object);
			Highlight.highlightElement(element);
			element.click();				
			waitFor(object, data);
			DriverScript.bResult = true;
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Notlnk_ able to click --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
    public static void doubleClickElement(String object, String data){
    	try{
			Log.info("Double clicking on Webelement "+ object);
			Actions action = new Actions(driver);
			Highlight.highlightElement(objectLocator(object));
			action.moveToElement(objectLocator(object)).doubleClick().perform();
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Notlnk_ able to double click --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
    
    public static void clearField(String object, String data){        
        try{
              Log.info("Clearing the text in " + object);
              Highlight.highlightElement(objectLocator(object));
              objectLocator(object).clear();
              objectLocator(object).sendKeys(Keys.BACK_SPACE);
              objectLocator(object).sendKeys(Keys.CLEAR);
              
              Thread.sleep(1000);
              DriverScript.bResult = true;
        }catch(Exception e){
              ScreenshotCapture.takeScreenShot(driver);
              Log.error("Not able to clear data --- " + e.getMessage());
              DriverScript.bResult = false;
              DriverScript.failedException = e.getMessage();
        }
    }

	public static boolean retryingFindClick(String object) {
		boolean result = false;
		int attempts = 0;
		while(attempts < 2) {
			try {
				objectLocator(object).click();
				result = true;
				break;
			} catch(StaleElementReferenceException e) {
			}
			attempts++;
		}
		return result;
	}
	
	public static List<WebElement> getElements(String object, String data){
			Log.info("Get all elements within a dom "+object);
			if(object.endsWith("_css"))
				{ return driver.findElements(By.cssSelector(OR.getProperty(object))); }				
			if(object.endsWith("_xpath"))
				{ return driver.findElements(By.xpath(OR.getProperty(object))); }		
			if(object.endsWith("_id"))
				{ return driver.findElements(By.id(OR.getProperty(object))); }		
			if(object.endsWith("_name"))
				{ return driver.findElements(By.name(OR.getProperty(object))); }		
			if(object.endsWith("_class"))
				{ return driver.findElements(By.className(OR.getProperty(object))); }		
			if(object.endsWith("_linktext"))
				{ return driver.findElements(By.linkText(OR.getProperty(object))); }	
			if(object.endsWith("_partiallinktext")){
				return driver.findElements(By.partialLinkText(OR.getProperty(object)));}			
			if(object.endsWith("_tag"))
				{ return driver.findElements(By.tagName(OR.getProperty(object))); }
			if (object.endsWith(")")){
				String[] obj = object.split("\\(");
				String replaceChar = obj[1].replace(")", "").trim();
				String rObj = OR.getProperty(obj[0].trim()).trim();
				String replacedObj = modifyLocator(rObj, replaceChar);
				return driver.findElements(By.xpath(replacedObj));
			}
			return null;
	}
	
	public void dropFile(String target, String filename) {
		try {			
			// open upload window
			objectLocator(target).click();
			Thread.sleep(3000);
			Runtime.getRuntime().exec(Config.Base_Dir + "\\bin\\SetFilePath.exe" + " " + Config.Base_Dir + "\\uploadFiles");
			Thread.sleep(2000);
			Runtime.getRuntime().exec(Config.Base_Dir + "\\bin\\FileUpload.exe" + " "+ filename);
			Thread.sleep(7000);
		} catch (Exception e) {
			Log.error("Not able to upload document --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}

	public static void matchElements(String object, String data) {
		try {
			String[] NewData = data.split("\\n");
			Log.info("Expected data : " + Arrays.toString(NewData));
			Log.info("Get all elements within a dom " + OR.getProperty(object));
			List<WebElement> elements = getElements(object, data);
			List<Integer> indexCnt = new ArrayList<Integer>();
			List<String> webList = new ArrayList<String>();
			int cnt = 0;
			for (WebElement elemText : elements) {
				webList.add(elemText.getText());
			}
			for (int i = 0; i < NewData.length; i++) {
				if (webList.toString().contains(NewData[i])) {
					cnt = cnt + 1;
				} else {
					indexCnt.add(i);
				}
			}
			if (cnt == NewData.length) {
				DriverScript.bResult = true;
			} else {
				ScreenshotCapture.takeScreenShot(driver);
				DriverScript.failedException = "false";
				for(int j=0;j<indexCnt.size();j++){
					Log.error("Expected text --- " + NewData[indexCnt.get(j)]);
					Log.error("Expected is not in actual string --- " + webList.toString());
			}
				indexCnt.clear();
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able find elements with text --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void enterInput(String object, String data){		
		try{
			
			Log.info("Entering the text in " + object);
			String expectedText = "";
			if (data.trim().toLowerCase().contains("var")) {
				expectedText = (String) hm.get(data);
			} else {
				expectedText = data;
			}
			System.out.print("expectedText : " + expectedText +"\n");
			//highLighterMethod
			WebElement element =  objectLocator(object);
			element.click();
			Highlight.highlightElement(element);
			element.sendKeys(expectedText);
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to Enter UserName --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void enterUniqueInput(String object, String data){
		try{
			Log.info("Entering the text in " + object);
			data = data + RandomStringUtils.randomAlphanumeric(5).toLowerCase();;
			Log.info("Entering the text in " + object);
			Highlight.highlightElement(objectLocator(object));
			objectLocator(object).sendKeys(data);	
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to Enter UserName --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void selectDropdown(String object, String data){
		try{
			Log.info("Selecting the dropdown value in " + object);
			WebElement element = null;
			element = objectLocator(object);
			Highlight.highlightElement(objectLocator(object));
			Select se = new Select(element);
			Log.info(data);
			
			se.selectByVisibleText(data);
			DriverScript.bResult = true;
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to select dropdown value --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
			//Assert.fail();//***//
		}
	}

	public static void selectListBox(String object, String data){	
		try{
			Log.info("Selecting the list value in " + object);
			WebElement element = null;
			element = objectLocator(object);	
			Select listBox = new Select(element);
			System.out.println(data);
			Log.info(data);
			listBox.selectByValue(data);
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to select dropdown value --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void checkElement(String object, String data){
		try{
			Log.info("check element: "+ object);
			Boolean isChecked = null;
			isChecked = objectLocator(object).isSelected();
			if(isChecked.equals(false))
				objectLocator(object).click();			
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to check element --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void unCheckElement(String object, String data){
		try{
			Log.info("uncheck element: "+ object);
			Boolean isChecked = null;
			isChecked = objectLocator(object).isSelected();
			if(isChecked.equals(true))
				objectLocator(object).click();			
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to uncheck element --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void waitFor(String object, String data) {
		try{
			Log.info("Wait for " + data + " miliseconds");
			int userwaittime = Integer.parseInt(data);
			Thread.sleep(userwaittime);
		
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to Wait --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
		
	public static void ScreenshotCapture(String object, String data){
			ScreenshotCapture.takeScreenShot(driver);
	}
	
	public static void closeBrowser(String object, String data){
		try{
			
			driver.navigate().to(Config.Base_Dir+"\\src\\test\\resources\\Config\\Web\\closingscreen.html");
			//();
			Thread.sleep(1000);
			Log.info("Closing the browser");
			driver.quit();
			DriverScript.bResult = true;
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to Close the Browser --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void verifyPageTitle(String object, String data){
		try{
			if (driver.getTitle().equals(data) == true) {
				DriverScript.bResult = true;
			
			}else{
	        	ScreenshotCapture.takeScreenShot(driver);
	        	Log.error("Expected text: " + data + " Actual text: "+ driver.getTitle());
	           	DriverScript.failedException = "Expected text: " + data + " Actual text: "+ driver.getTitle();
			}
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to get page title --- " + e.getMessage());
			DriverScript.failedException = "false";
		}
	}
	
	public static void verifyTextPresent(String object, String data) {
		
		try {
			
			String expectedText = "";
			if (data.trim().toLowerCase().contains("variable")) {
				expectedText = (String) hm.get(data);
			} else {
				expectedText = data;
			}
			String txtElement = objectLocator(object).getText();
			System.out.println(txtElement);
			if (txtElement.trim().toUpperCase().contains(expectedText.trim().toUpperCase()) && !expectedText.equals("")){
				DriverScript.bResult = true;
				Log.info("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				
			} else {
				highLightElementAndScreenCapture(object, data);
				Log.error("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				//DriverScript.bResult = ("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				DriverScript.bResult = false;
				DriverScript.failedException = ("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to get text of the element --- " + e.getMessage());
			DriverScript.failedException = "false";
		}
	}
	
	public static void verifyTextPresentinInputfield(String object, String data) {
		
		try {
			
			String expectedText = "";
			if (data.trim().toLowerCase().contains("variable")) {
				expectedText = (String) hm.get(data);
			} else {
				expectedText = data;
			}
			
			//String expectedText = data;
			String txtElement = objectLocator(object).getAttribute("value");
		
			if (txtElement.trim().toUpperCase().contains(expectedText.trim().toUpperCase()) && !expectedText.equals("")){
				DriverScript.bResult = true;
				Log.info("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				
			} else {
				highLightElementAndScreenCapture(object, data);
				Log.error("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				//DriverScript.bResult = ("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				DriverScript.bResult = false;
				DriverScript.failedException = ("Expected text: " + expectedText + " ; Actual text: " + txtElement);
				
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to get text of the element --- " + e.getMessage());
			DriverScript.failedException = "false";
		}
	}

	public static void verifyElementPresent(String object, String data){
		try {
			Log.info("Verify Element present " + object);
			boolean exists = getElements(object, data).size() != 0;
			if (exists) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objectLocator(object));
				Thread.sleep(500); 
				Highlight.highlightElement(objectLocator(object));
				DriverScript.bResult = true;
			} else {
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Element not present in the screen");
				DriverScript.bResult = false;
				DriverScript.failedException = "Element not present in the screen";
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to locate element --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void verifyElementDisabled(String object, String data){
		try {
			Log.info("Verify Element Disabled " + object);
			String eenabled = objectLocator(object).getAttribute("disabled");
			
			//Log.error(eenabled);
			System.out.println(eenabled);
			if (eenabled!= null) {
				Highlight.highlightElement(objectLocator(object));
				DriverScript.bResult = true;
			} else {
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Element not present in the screen");
				DriverScript.bResult = false;
				DriverScript.failedException = "Element not present in the screen";
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to locate element --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void verifyElementNOTPresent(String object, String data){
		try {
			Log.info("Verify Element is NOT present " + object);
			boolean exists = getElements(object, data).size() == 0;
			if (exists) {
					DriverScript.bResult = true;
				
			} else {
				
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objectLocator(object));
				Thread.sleep(500); 
				ScreenshotCapture.takeScreenShot(driver);
				Highlight.highlightElement(objectLocator(object));
				Log.error("Element  present in the screen");
				DriverScript.bResult = false;
				DriverScript.failedException = "Element  present in the screen";
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element Present --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
		
	public static int pickRandomValue(int maxValue) {
		int r = 1;
		int[] numbers = new int[maxValue];
		for (int i = 0; i < numbers.length; i++) {
			r = (int) (Math.random() * maxValue);
			return r;
		}
		return r;
	}

	public static int getTableRowCount(String object, String data){
		int count = 0;
		try{
			Log.info("Get total row count "+ object);
			count = driver.findElements(By.xpath(object)).size();
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element not present --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		return count;		 
	}
	
	public static int getTableColCount(String object, String data){
		int count = 0;
		try{
			Log.info("Get total row count "+ object);
			count = driver.findElements(By.xpath(object)).size();
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element not present --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		return count;		 
	}

	public static String getText(String object, String data){
		String elemText = "";
		try{
			Log.info("Get text of: "+ object);
			Highlight.highlightElement(objectLocator(object));
			elemText = objectLocator(object).getText();
			hm.put(data, elemText);
		
			
			Log.info("Text of the object is: " + elemText);
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable get text --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		return elemText;
	}
	
	public static void randomNumber(String len,String varName) {
		String genString = "123456789";
		int length = Integer.parseInt(len);

		StringBuilder randString = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int character = (int) (Math.random() * genString.length());
			randString.append(genString.charAt(character));
		}
		System.out.print("Random String : " + randString.toString() + "\n");
		hm.put(varName,randString.toString());
	}
		
	public static void pressEnter(String object, String data) {
		try {
			objectLocator(object).sendKeys(Keys.ENTER);
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to send enter key to the object --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void pageRefresh(String object, String data){
			driver.navigate().refresh();
	}
	
	public static void setDateByTextEntry(String object, String data){
		try{
			Log.info("Get Date: "+object);
			objectLocator(object).sendKeys(data);
			objectLocator(object).sendKeys(Keys.ENTER);
			String dateval = getText(object,data);
			if(dateval.equals(data)){
				Log.info("Date in field -- "+dateval);
				DriverScript.bResult = true;				
			}
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to set date --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}		
	}
	
	public static boolean columnSortingoption(String columntosort, String data) {

		ArrayList<String> beforesort = new ArrayList<String>();
		ArrayList<String> sortedvalue = new ArrayList<String>();

		List<WebElement> columnlisttochksort = driver.findElements(By.cssSelector(columntosort));
		for (WebElement sortingColvalue : columnlisttochksort) {
			beforesort.add(sortingColvalue.getText().trim().toUpperCase());
			sortedvalue.add(sortingColvalue.getText().trim().toUpperCase());
		}
		if (data.equalsIgnoreCase("ascending")){
			Collections.sort(sortedvalue, String.CASE_INSENSITIVE_ORDER);	
		}else if (data.contains("descending")) {
			Collections.sort(sortedvalue, String.CASE_INSENSITIVE_ORDER);
			Collections.reverse(sortedvalue);
		}
		
		System.out.println("before sorting:" + beforesort);
		System.out.println("sortedvalue:" + sortedvalue);
		if (sortedvalue.equals(beforesort)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean columndateASCsortingoption(String columntosort, String data) {

		ArrayList<String> beforesort = new ArrayList<String>();
		ArrayList<String> sortedvalue = new ArrayList<String>();

		List<WebElement> columnlisttochksort = driver.findElements(By.cssSelector(columntosort));
		for (WebElement sortingColvalue : columnlisttochksort)

		{
			if (sortingColvalue.getText().matches("") || sortingColvalue.getText().matches("--")) {
				beforesort.remove(sortingColvalue.getText().trim().toUpperCase());
				sortedvalue.remove(sortingColvalue.getText().trim().toUpperCase());
			} else {
				beforesort.add(sortingColvalue.getText().trim().toUpperCase());
				sortedvalue.add(sortingColvalue.getText().trim().toUpperCase());

			}
		}

		Collections.sort(sortedvalue, new Comparator<String>() {
			DateFormat f = new SimpleDateFormat("MM/dd/yyyy");

			public int compare(String o1, String o2) {
				try {
					return f.parse(o1).compareTo(f.parse(o2));
				} catch (java.text.ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});

		System.out.println("before sorting:" + beforesort);
		System.out.println("revesrted date:" + sortedvalue);
		if (beforesort.equals(sortedvalue)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean columndateDSCsortingoption(String columntosort, String data) {

		ArrayList<String> beforesort = new ArrayList<String>();
		ArrayList<String> sortedvalue = new ArrayList<String>();

		List<WebElement> columnlisttochksort = driver.findElements(By.cssSelector(columntosort));
		for (WebElement sortingColvalue : columnlisttochksort)

		{
			if (sortingColvalue.getText().matches("") || sortingColvalue.getText().matches("--")) {
				beforesort.remove(sortingColvalue.getText().trim().toUpperCase());
				sortedvalue.remove(sortingColvalue.getText().trim().toUpperCase());
			} else {
				beforesort.add(sortingColvalue.getText().trim().toUpperCase());
				sortedvalue.add(sortingColvalue.getText().trim().toUpperCase());

			}
		}

		Collections.sort(sortedvalue, new Comparator<String>() {
			DateFormat f = new SimpleDateFormat("MM/dd/yyyy");

			public int compare(String o1, String o2) {
				try {
					return f.parse(o1).compareTo(f.parse(o2));
				} catch (java.text.ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		System.out.println("intermediate sort: " + sortedvalue);
		Collections.reverse(sortedvalue);
		System.out.println("before sorting:" + beforesort);
		System.out.println("revesrted date:" + sortedvalue);

		if (beforesort.equals(sortedvalue)) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean validateDropdown(String object,String data) {

		String spilteddata[] = data.split(",");
		Select selectObject = new Select(objectLocator(object));
		List<String> options = new ArrayList<String>();
		for (int i = 0; i < selectObject.getOptions().size(); i++) {
			options.add(selectObject.getOptions().get(i).getText().toLowerCase());
		}
		if (options.size() == spilteddata.length) {
			for (String temp : spilteddata)
				if (!options.contains(temp.toLowerCase()))
				{
					System.out.println("Option " + temp + " is not displayed in the options " + options);
					return false;
			}

		} else {
			System.out.println("Options count Mismatch.\n Expected values: " + data + " \n Actual values: " + options);
			return false;
		}
		System.out.println("Options are displayed as Expected .\n Expected values: " + data + " \n Actual values: " + options);
		return true;
	}
	
	public static String validateText(String object, String data){
		String expectedText = "";
		if(data.trim().toLowerCase().contains("variable")){
			expectedText = (String) hm.get(data);
		}else{
			expectedText=data;
		}
		WebElement element = objectLocator(object); 			
		if (element.getAttribute("value").trim().equals(expectedText.trim()) && !expectedText.equals("")) {
        	DriverScript.bResult = true;
        	Log.info("Expected text: " + expectedText.trim() + " Actual text: "+ element.getAttribute("value").trim());
		} else{
			highLightElementAndScreenCapture(object,data);
        	Log.error("Expected text: " + expectedText.trim() + " Actual text: "+ element.getAttribute("value").trim());
           	DriverScript.failedException = "false";
		}
		return expectedText;
	}
	
	public static int getInt(String object, String text) {
		int count = 0;
		if (text != null)
			try {
				text = text.replaceAll("(\\D+)(.)*", "").trim();
				if (!"".equals(text))
					count = Integer.parseInt(text.trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		return count;
	}
	
	public void isElementPresent(String object, String data) {
		try {
			Log.info("Verify Element isPresent " + object);
			boolean exists = getElements(object,data).size() != 0;
			if(exists) {
				Highlight.highlightElement(objectLocator(object));
				DriverScript.stepRun = true;
				Log.info("Element is present and next step will be executed");
			} else {
				DriverScript.stepRun = false;
				Log.info("Element is not present and next step will be SKIPPED");
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Not able to locate element --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static boolean isValidURL(String object, String pageUrl) {
		try {
			new URL(pageUrl);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	public boolean isAlertDisplayedWithinTimeout(int timeout) throws InterruptedException{
		for (int counter = 1; counter <= (timeout) / 500; counter++) {
			try {
				Alert alert = driver.switchTo().alert();
				
				return true;
			} catch (Exception e) {
				Thread.sleep(500);
			}
		}
		return false;
	}
	
	public static boolean isElementByCSSPresent(String selector) {
		int count = driver.findElements(By.cssSelector(selector)).size();
		if (count > 0)
			return true;
		else
			return false;

	}
	
	public static WebElement FindByCssSelector(String selector,String data) {
		try {
			WebElement element = driver.findElement(By.cssSelector(selector));
			return element;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isDateValid(String actualDate, String expectedDateFormat){
		try {
			SimpleDateFormat format = new SimpleDateFormat(expectedDateFormat);
			format.parse(actualDate);
		} catch (ParseException e) {
			Log.error("Date is not valid --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
		return true;
	}
		
	public static void SelectFromDateInCalendar(String object, String data) {
		try{
			//month-0;date-1;year-2; 
			String[] formatDate = data.split("/");
			String fromMonth=formatDate[0],date=formatDate[1],fromYear=formatDate[2];
			System.out.println("cal: "+fromMonth+date+fromYear);
			objectLocator(object).click();
			Select selectYear = new Select(driver.findElement(By.cssSelector("#calYearPicker")));
			selectYear.selectByVisibleText(fromYear);
			Select selectMonth = new Select(driver.findElement(By.cssSelector("#calMonthPicker")));
			if (fromMonth.matches("\\d*")) {
				// since in calendar values for months are from '0' to '11'
				fromMonth = String.valueOf(Integer.valueOf(fromMonth) - 1);
				selectMonth.selectByValue(fromMonth);
			} else if (fromMonth.matches("\\D*")) {
				selectMonth.selectByVisibleText(fromMonth);
			}
			//driver.findElement(By.xpath(".//*[@id='datePicker']//td[@class='weekday' and text()="+date+"]")).click();
			driver.findElement(By.xpath(".//*[@id='datePicker']//td[contains(@class,'weekday') or @class='selectedDate'] [text()="+date+"]")).click();
			String datetext = driver.findElement(By.xpath(OR.getProperty(object))).getText();
			if(datetext.equals(data)){
				Log.info("Date in field -- "+datetext);
				DriverScript.bResult = true;				
			}
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to set date --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public String selectOptionFromDropDown(String selector, String selectText) {
		try {

			driver.findElement(By.cssSelector(selector)).click();
			new Select(driver.findElement(By.cssSelector(selector))).selectByVisibleText(selectText);

		} catch (Exception e) {
			System.out.println("Exception in selectOptionFromDropDown:" + e);
		}
		return null;
	}
	
	public static void sswitchToNewTab(String object, String data) {
		try {
			String currentTab = "";
			if (data.trim().toLowerCase().contains("variable")) {
				//driver.switchTo().frame(arg0)
				currentTab = (String) hm.get(data);
			} else {
				currentTab = data;
			}
			ArrayList<String> newTabs = new ArrayList<String>(driver.getWindowHandles());
			// To get total tabs
			int totalTabs = newTabs.size();
			int navigateToTab = 0;
			if (totalTabs >= 1) {
				int tabCount = 0;
				for (String newTab : newTabs) {
					if (newTab.equals(currentTab)) {
						navigateToTab = tabCount;
						break;
					}
					tabCount = tabCount + 1;
				}
				if (tabCount == 0 && totalTabs > 1) {
					driver.switchTo().window(newTabs.get(1));
					DriverScript.bResult = true;
				} else {
					driver.switchTo().window(newTabs.get(0));
					DriverScript.bResult = true;
				}

				/*
				 * newTabs.remove(currentTab); totalTabs = newTabs.size();
				 * System.out.println("totalTabs : " + totalTabs);
				 * driver.switchTo().window(newTabs.get(navigateToTab));
				 */
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to close new browser --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
	public static void switchToNewTab(String object, String data) {
		try {
			
			String winHandleBefore = driver.getWindowHandle();

			String TestFile = ".\\src\\test\\java\\library\\SessionDetails.txt";
			File FC = new File(TestFile);
			FC.createNewFile();//Create file.
			FileWriter FW = new FileWriter(TestFile);
			BufferedWriter BW = new BufferedWriter(FW);
			BW.write(winHandleBefore); 
			BW.close();

		
			for(String winHandle : driver.getWindowHandles()){
			    driver.switchTo().window(winHandle);
			}				
			
		}
		catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to switch --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
	public static void switchParentTab(String object, String data) {
		try {
			String Content ="";
			String TestFile = ".\\src\\test\\java\\library\\SessionDetails.txt";
			FileReader FR = new FileReader(TestFile);
			BufferedReader BR = new BufferedReader(FR);
			while((Content = BR.readLine())!= null){
			driver.switchTo().window(Content);
			}				
			
		}
		catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to switch --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
		
	public static void switchAndCloseLatestTab(String object, String data) {
		try {
			String parentWindow = getCurrentWindow(object,data);
			ArrayList<String> newTabs = new ArrayList<String>(driver.getWindowHandles());
			int totalTabs = newTabs.size();
			System.out.println("totalTabs : " + totalTabs);
			int navigateToTab = 0;
			if (totalTabs > 1) {
				int tabCount = 0;
				for (String newTab : newTabs) {
					if (newTab.equals(parentWindow)) {
						navigateToTab = tabCount;
						break;
					}
					tabCount = tabCount + 1;
				}
				if (navigateToTab == 0) {
					driver.switchTo().window(newTabs.get(1));
					driver.close();
					driver.switchTo().window(newTabs.get(0));
				} else {
					driver.switchTo().window(newTabs.get(navigateToTab));
					driver.close();
					driver.switchTo().window(newTabs.get(0));
				}
			}
		} catch (Exception e) {
			Log.error("Unable to close new browser --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
	public static void navigateBack(String object, String data){
		try {
			Log.info("Browser navigate");
			driver.navigate().back();
			waitFor(object, data);
			DriverScript.bResult = true;
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to navigate back --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
	//hover over related to web

	public static void hoverAndClick(String object, String data){
		try {
			Log.info("Hover on menu : "+ data + "; click on object : " + object);
			
			WebElement we = objectLocator(object);
			Actions actions = new Actions(driver);
			actions.moveToElement(we).perform();
			
	
			//actions.moveToElement(we).moveToElement(objectLocator(object)).click().build().perform();

			//WebElement subMenu = driver.findElement(By.xpath("//*[@id='menu-links']/ul/li[2]/a"));
			//actions.moveToElement(objectLocator(object)).build().perform();
			//actions.
			//actions.click().build().perform();
			//actions.build().
			
			
			
			
			//WebElement we = objectLocator(data);
			//actions.moveToElement(we).moveToElement(objectLocator(object)).click().build().perform();
			/*WebDriverWait wait = new WebDriverWait(driver, 30);
		    wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));*/
			waitFor(object,"5000");
			DriverScript.bResult = true;
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to click --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
		
	public static void scrollAndClick(String object, String data){
		try{
			Log.info("Clicking on Element "+ object);
			WebElement element = objectLocator(object);
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].click();", element);				
			waitFor(object, data);
			DriverScript.bResult = true;
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to click --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
		
	public static void scrollToElement(String object, String data){		
		try{
			Log.info("X and Y axis are : "+ data);
			String axis[] = data.split(",");
			int x = Integer.parseInt(axis[0]);
			int y = Integer.parseInt(axis[1]) - 200;
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollTo(" + x + "," + y +")", "");
		}catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to scroll --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
		
	public static void switchToAlert(String object, String data) {
		try {
			Alert alert = driver.switchTo().alert();
			Thread.sleep(1000);
			if (data.trim().toLowerCase().contains("ok")) {
				alert.accept();
			} else if (data.trim().toLowerCase().contains("cancel")){
				alert.dismiss();
			} else {
				Log.info("Switch to alert is successful");
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to identify alert --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
		
	public static String getCurrentWindow(String object,String data){
		String parentWinHandle = null;
		try {
			 Log.info("Get window handle of current window");
			 parentWinHandle = driver.getWindowHandle();
			 System.out.println("handle : " + parentWinHandle);
			 hm.put(data, parentWinHandle);
			 DriverScript.bResult = true;
		} catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to get current window --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		return parentWinHandle;
	}
	
	public static void switchToNewWindow(String object,String data){
		try {
				driver.switchTo().window(object);
		} catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to get window --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void closeWindow(String object, String data){
		try {
			driver.close();
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to close window --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void dragAndDropFile(String object, String filePath) {
		WebElement target = objectLocator(object);
		int offsetX = 0; int offsetY = 0;
	    
	    WebDriver driver = ((RemoteWebElement)target).getWrappedDriver();
	    JavascriptExecutor js = (JavascriptExecutor)driver;
	    WebDriverWait wait = new WebDriverWait(driver,30);

	    String JS_DROP_FILE = "var target = arguments[0],offsetX = arguments[1],offsetY = arguments[2]," +
	    		"document = target.ownerDocument || document," +
	    		"window = document.defaultView || window;"+
	    		"var input = document.createElement('INPUT');"+
	    		"input.type = 'file';"+
	    		"input.style.display = 'none';"+
	    		"input.onchange = function () {"+
	    		"target.scrollIntoView(true);"+
	          "var rect = target.getBoundingClientRect(),"+
	              "x = rect.left + (offsetX || (rect.width >> 1)),"+
	              "y = rect.top + (offsetY || (rect.height >> 1)),"+
	              "dataTransfer = {files: this.files};"+
	              "['dragenter', 'dragover', 'drop'].forEach(function (name) {"+
	            "var evt = document.createEvent('MouseEvent');"+
	            "evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"+
	            "evt.dataTransfer = dataTransfer;"+
	            "target.dispatchEvent(evt);" +
	            "});" +
	            "setTimeout(function() {document.body.removeChild(input);}, 25);" +
	            "}; " +	            
	            "document.body.appendChild(input);" +
	            "return input;";	    	    
	    WebElement input =  (WebElement)js.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
	    input.sendKeys(filePath);
	    wait.until(ExpectedConditions.stalenessOf(input));
	}
	
	public void sliderControl(String object1, String object2) throws InvocationTargetException{
		try{
		
			WebElement slidebar = objectLocator(object1);
			int widthSlideBar = slidebar.getSize().width;
			System.out.println("widthSlideBar Length: "+widthSlideBar);
			WebElement slider =  objectLocator(object2);
			Actions sliderAction = new Actions(driver);
			sliderAction.clickAndHold(slider);
			sliderAction.moveByOffset(40,0).build().perform();
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to get window --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();			
		}
	 }
	
	public void switchToFrame(String object, String data) {
		try {
			if(data.toLowerCase().trim().contains("frame")){
				WebElement iFrame = objectLocator(object);
				driver.switchTo().frame(iFrame);
				DriverScript.bResult = true;
			}else if (data.toLowerCase().trim().contains("default")){
				driver.switchTo().defaultContent();
				DriverScript.bResult = true;
			}else{
				System.out.println("Frame name doesn't match");
				Log.error("Frame name is not valid");
				DriverScript.bResult = false;
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Unable to switch frame  --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void getPageUrl(String object, String data) {
		try {
			String expectedText = "";
			if (data.trim().toLowerCase().contains("variable")) {
				expectedText = (String) hm.get(data);
			} else {
				expectedText = data;
			}
			String URL = driver.getCurrentUrl();
			System.out.println("Current URL: " + URL);
			if (URL.trim().toUpperCase().contains(expectedText.trim().toUpperCase()) && !expectedText.equals(""))
				DriverScript.bResult = true;
			else {
				Log.error("Expected text: " + data + " ; Actual text: " + URL);
				DriverScript.failedException = "false";
			}
		} catch (Exception e) {
			Log.error("Invalid page url --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}

	// Method to Highlight Element

	public static void highLightElementAndScreenCapture(String object, String data){
		try {
			WebElement element = objectLocator(object);
			JavascriptExecutor executor = (JavascriptExecutor)driver;
			executor.executeScript("arguments[0].scrollIntoView(true);", element);
			executor.executeScript("window.scrollBy(0,-25)", "");
			Thread.sleep(1000);
			executor.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "border: 2px solid red;");
			ScreenshotCapture.takeScreenShot(driver);
			executor.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "");
		} catch (InterruptedException e) {
			Log.error("Unable to highlight the element" + e.getMessage());
		} 
	}

	public static void extractDigits(String object, String data) {
		String dataText="";
		if(data.trim().toLowerCase().contains("variable")){
			dataText=(String) hm.get(data);
			System.out.println("dataText : " + dataText);
			final StringBuilder sb = new StringBuilder(dataText.length());
			for (int i = 0; i < dataText.length(); i++) {
				final char c = dataText.charAt(i);
				if (c > 47 && c < 58) {
					sb.append(c);
				}
			}
			// Check if it adds duplicate
			hm.put(data, sb.toString());
			DriverScript.bResult = true;
			System.out.println("extractDigits : " + sb.toString());
			//hm.put(data + "Int", sb.toString());
		}else{
			DriverScript.failedException = "false";
			Log.error("Variable name is expected (Eg: VariabelVal1)");
		}
	}
	
	public static void compareString(String object, String data) {
		String[] dataText = data.split("=");
		String strCmp1 = dataText[0];
		String strCmp2 = dataText[1];

		System.out.println("strCmp1 : " + strCmp1);
		System.out.println("strCmp2 : " + strCmp2);
		
		// Check arithmetic operator is present in string 1
		if (Pattern.compile("[-+*/]").matcher(strCmp1.trim()).find()) {
			// split with arithmetic operator, and operator will also be
			// considered in array Eg: array will have [a, +, b, -, c, *, d, /, e]
			String[] result1 = strCmp1.split("(?<=[-+*/])|(?=[-+*/])");	
			// check if it has a variable name and get value
			if (result1[0].trim().toLowerCase().contains("var")) {
				result1[0] = (String) hm.get(result1[0]);
			}
			if (result1[2].trim().toLowerCase().contains("var")) {
				result1[2] = (String) hm.get(result1[2]);
			}
			
			Double str1 = Double.parseDouble(result1[0]);
			Double str2 = Double.parseDouble(result1[2]);
			Double str3 = 0.00;
			// perform operation accordingly and store the value in string
			if (result1[1].equals("+")) {
				str3 = str1 + str2;
				System.out.println("str3 : " + str3);
			} else if (result1[1].equals("-")) {
				str3 = str1 - str2;
			} else if (result1[1].equals("*")) {
				str3 = str1 * str2;
			}
			strCmp1 = String.valueOf(Math.round(str3));
			System.out.println(strCmp1);

		}
		if (Pattern.compile("[-+*/]").matcher(strCmp2.trim()).find()) {
			String[] result2 = strCmp2.split("(?<=[-+*/])|(?=[-+*/])");
			if (result2[0].trim().toLowerCase().contains("var")) {
				result2[0] = (String) hm.get(result2[0]);
			}
			if (result2[2].trim().toLowerCase().contains("var")) {
				result2[2] = (String) hm.get(result2[2]);
			}
			
			Double str1 = Double.parseDouble(result2[0]);
			Double str2 = Double.parseDouble(result2[2]);
			Double str3 = 0.00;
			if (result2[1].equals("+")) {
				str3 = str1 + str2;
				System.out.println("str3 : " + str3);
			} else if (result2[1].equals("-")) {
				str3 = str1 - str2;
			} else if (result2[1].equals("*")) {
				str3 = str1 * str2;
			}
			strCmp2 = String.valueOf(Math.round(str3));
			System.out.println(strCmp2);
		}

		if (strCmp1.trim().toLowerCase().contains("var")) {
			strCmp1 = (String) hm.get(strCmp1);
		}
		if (strCmp2.trim().toLowerCase().contains("var")) {
			strCmp2 = (String) hm.get(strCmp2);
		}
		
		System.out.println("cmp strCmp1 : " + strCmp1);
		System.out.println("cmp strCmp2 : " + strCmp2);
		
		if (strCmp1.equals(strCmp2)) {
			System.out.println("Scenario1 passed");
			DriverScript.bResult = true;
		} else {
			Log.error("String1 : " + strCmp1 + " ; String2 : " + strCmp2);
			DriverScript.failedException = "false";
		}
	}

	public void verifyAttributeText(String object, String data){
		try{
			String[] dataText = data.split("=");
			String attribute = dataText[0].trim();
			String attributeExpValue = dataText[1].trim();
			String attributeActValue = objectLocator(object).getAttribute(attribute).trim();
			
			if (attributeExpValue.equalsIgnoreCase("blank")){
				if (attributeActValue.isEmpty()){
					DriverScript.bResult = true;
				}else{
		        	highLightElementAndScreenCapture(object,data);
		        	Log.error("Attribute value is not blank");
		           	DriverScript.failedException = "false";
				}
			}else{
				if (attributeActValue.trim().toUpperCase().contains(attributeExpValue.trim().toUpperCase()) && !attributeExpValue.equals("")){
					DriverScript.bResult = true;	
					Log.info("Expected " + attribute + " value: " + attributeExpValue + " ; Actual " + attribute + " value: "+ attributeActValue);
				}else{
		        	highLightElementAndScreenCapture(object,data);
		        	Log.error("Expected " + attribute + " value: " + attributeExpValue + " ; Actual " + attribute + " value: "+ attributeActValue);
		           	DriverScript.failedException = "false";
				}	
			}
		} catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Object attribute not found --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
		}
	}
	
	public static void dragAndDropElement(String Fromobject, String ToObject) {         
        WebElement From = objectLocator(Fromobject);
        WebElement To = objectLocator(ToObject);
        Actions builder = new Actions(driver);
        Action dragAndDrop = builder.clickAndHold(From)
        .moveToElement(To)
        .release(To)
        .build();
        dragAndDrop.perform();
    }
	
	public static void rightClick(String object, String data) {
        try {
        	WebElement element = objectLocator(object);
            Actions action = new Actions(driver).contextClick(element);
            action.build().perform();
            DriverScript.bResult = true;
            waitFor(object,data);
        } catch (Exception e) {
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Object not found to perform right click --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getLocalizedMessage();
        }
	}
	
	public void imageCompare(String object, String data) {
		try {
			// Image compare using Ocular
			String filePath = Config.Base_Dir+"\\Screenshots\\" + data + "\\" + DriverScript.sTestCaseID + "_" + DriverScript.sTestStepID + ".png";
			 Ocular.config()
			 	   .resultPath(Paths.get(Config.Base_Dir+"/Screenshots/Output"))
			 	   .snapshotPath(Paths.get(Config.Base_Dir+"/Screenshots/Output"))
			 	   .globalSimilarity(100).saveSnapshot(true);
			 System.out.println("Ocular : Screen comparision is done");
			 cmpImage(filePath,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	 public static void cmpImage(String fileName, String extra){
	        Path expectedFilePath = Paths.get(fileName);
	        OcularResult result = Ocular.snapshot()
	                                    .from(expectedFilePath)	                           
	                                    .sample()
	                                    .using(driver)
	                                    .compare();
	        System.out.println("Result : " + result.getComparisonStatus());
	        System.out.println("Result : " + result.getSimilarity());
	    }
	 
	 
	 /*****************Method for New Actions for IDM***************************/
	    /*************************@Author kcbiswal*************************/

	public static void getGIDSave(String object, String data) throws IOException{
			
			try{
				Log.info("Get text of: "+ object);
				Highlight.highlightElement(objectLocator(object));
				String elemText = objectLocator(object).getText();
				String TestFile = ".\\src\\test\\java\\library\\UserDetails.txt";
				File FC = new File(TestFile);
				FC.createNewFile();//Create file.
				FileWriter FW = new FileWriter(TestFile);
				BufferedWriter BW = new BufferedWriter(FW);
				String[] sTemp=elemText.split("\\(");
				String xx = sTemp[1];
				String[] iTemp = xx.split("\\)");
				String yy = iTemp[0];
				BW.write(yy); 
				BW.close();
				
				}
			catch(Exception e){
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Element is not displayed --- " + e.getMessage());
				DriverScript.bResult = false;
				DriverScript.failedException = e.getMessage();
			}
			}
	
	public static void readGIDEnterinput (String object,String data){
			String Content = "";
			try{
			String TestFile = ".\\src\\test\\java\\library\\UserDetails.txt";
			FileReader FR = new FileReader(TestFile);
			BufferedReader BR = new BufferedReader(FR);
			while((Content = BR.readLine())!= null){
			Highlight.highlightElement(objectLocator(object));
			String[] sTemp=Content.split("\\(");
			String xx = sTemp[1];
			String[] iTemp = xx.split("\\)");
			String yy = iTemp[0];
			//BW.write(yy);
			objectLocator(object).sendKeys(yy);
			Log.info("Enterd Input"+ Content);
			
			}
			
			}
			catch(Exception e){
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Element is not displayed --- " + e.getMessage());
				DriverScript.bResult = false;
				DriverScript.failedException = e.getMessage();
			}
			}
			
	public static void getTextSave(String object, String data) throws IOException{
		
		try{
			Log.info("Get text of: "+ object);
			Highlight.highlightElement(objectLocator(object));
			String elemText = objectLocator(object).getText();
			String TestFile = ".\\src\\test\\java\\library\\UserDetails.txt";
			File FC = new File(TestFile);
			FC.createNewFile();//Create file.
			FileWriter FW = new FileWriter(TestFile);
			BufferedWriter BW = new BufferedWriter(FW);
			//String[] sTemp=elemText.split("\\(");
			//String xx = sTemp[1];
			//String[] iTemp = xx.split("\\)");
			//String yy = iTemp[0];
			BW.write(elemText); 
			BW.close();
			
			}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element is not displayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		}

	public static void readTextEnterinput (String object,String data){
		String Content = "";
		try{
		String TestFile = ".\\src\\test\\java\\library\\UserDetails.txt";
		FileReader FR = new FileReader(TestFile);
		BufferedReader BR = new BufferedReader(FR);
		while((Content = BR.readLine())!= null){
		Highlight.highlightElement(objectLocator(object));
		objectLocator(object).sendKeys(Content);
		Log.info("Login with "+ Content);
		
		}
		
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element is not displayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
			
		}
}

	public static void empsearchandClick(String object,String data){
		String Content = "";
		try{
		String TestFile = ".\\src\\test\\java\\library\\UserDetails.txt";
		FileReader FR = new FileReader(TestFile);
		BufferedReader BR = new BufferedReader(FR);
		while((Content = BR.readLine())!= null){
			Highlight.highlightElement(	driver.findElement(By.xpath("//span[text()='"+Content+"']")));
			driver.findElement(By.xpath("//span[text()='"+Content+"']")).click();;
			
				}
			}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element is not displayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	
	}
	
	public static void waitUntilldisplayed(String object, String data){
	
	
	try{
		WebDriverWait wait = new WebDriverWait(driver,50);
		wait.until(ExpectedConditions.visibilityOf(objectLocator(object)));
		wait.until(ExpectedConditions.elementToBeClickable(objectLocator(object)));
		//Highlight.highlightElement(objectLocator(object));
		DriverScript.bResult = true;
	}
	catch(Exception e){
		ScreenshotCapture.takeScreenShot(driver);
		Log.error("Element is not displayed --- " + e.getMessage());
		DriverScript.bResult = false;
		DriverScript.failedException = e.getMessage();
	}
}

	public static void waitUntillclickable(String object, String data){
		
		
		try{
			WebDriverWait wait = new WebDriverWait(driver,50);
			wait.until(ExpectedConditions.elementToBeClickable(objectLocator(object)));
			//Highlight.highlightElement(objectLocator(object));
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Element is not clickable --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void waitUntillInvisible(String obejct, String data){
	
	
	try{
		WebDriverWait wait = new WebDriverWait(driver,100);
		Highlight.highlightElement(objectLocator(obejct));
		wait.until(ExpectedConditions.invisibilityOf(objectLocator(obejct)));
	}
	catch(Exception e){
		ScreenshotCapture.takeScreenShot(driver);
		Log.error("Element is not displayed --- " + e.getMessage());
		DriverScript.bResult = false;
		DriverScript.failedException = e.getMessage();
	}
}
	
	public static void selectMatchingrecord(String object ,String data){
		try {
		String tt ="//td[span[span[contains(text(),'"+ data +"')]]]/following-sibling::td/div/button";
		Highlight.highlightElement(driver.findElement(By.xpath(tt)));
		driver.findElement(By.xpath(tt)).click();;
		
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Team Not Present--- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
		
	}
	
	public static void verifyobjectpresent(String object ,String data){
		try {
		WebElement tt =driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tt);
		Thread.sleep(500); 
		Highlight.highlightElement(tt);
		
		String WO = tt.getText();
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Object Not Present--- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
		
	}
	
	public static void selectMatchingbyText(String object ,String data){
		try {
		WebElement tt =driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tt);
		Thread.sleep(500); 
		Highlight.highlightElement(tt);
		tt.click();
		Thread.sleep(500); 
		
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Object Not Present--- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
		
	}
	
	public static void verifyobjectNOTpresent(String object ,String data){
		try {
			
			//String exist ="//*[contains(text(),'" + data + "')]";
			///WebElement tt =driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"));
			//Highlight.highlightElement(tt);
			Log.info("Verify Element is NOT present " + data);
			boolean exists = driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]")).getSize() !=null;
			
			//boolean exists = getElement(tt, data).size() == 0;
			if (exists) {
				DriverScript.bResult = true;
			} else {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]")));
				Thread.sleep(500); 
				ScreenshotCapture.takeScreenShot(driver);
				Highlight.highlightElement(driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]")));
				Log.error("Element  present in the screen");
				DriverScript.bResult = false;
				DriverScript.failedException = "Element  present in the screen";
				
			
			}
		
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Object Not Present--- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
		
	}
	
	public static void selectMatchingTeam(String ojbect , String data){
		try{
		    List<WebElement> rows = driver.findElements(By.cssSelector("tbody > tr"));
		    for(WebElement row : rows){
		        if(row.findElement(By.cssSelector("tbody > tr > td > span > span")).getText().equalsIgnoreCase(data));
		           row.findElement(By.cssSelector(" tbody > tr > td > div> button")).click();  
		             break;
		        }
		}
			catch(Exception e){
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Team Not Found--- " + e.getMessage());
				DriverScript.bResult = false;
				DriverScript.failedException = e.getMessage();
			}
		   }
		
	
	
	public static void verifynewExternaluserdisplayed(String object,String data){
	String data1 = data;
	try {
		String tt ="//*[contains(text(),'" + data + "')]";
		Highlight.highlightElement(driver.findElement(By.xpath(tt)));
		String statususer=driver.findElement(By.xpath("//div[div[div[div[div[table[tbody[tr[td[span[span[contains(text(),'" + data + "')]]]]]]]]]]]/preceding-sibling::h3[1]/span")).getText();
		//System.out.println("New Created External User Dsiplayed under" + statususer + "seaction");
		String extusersta="Active External User";
		//ScreenshotCapture.takeScreenShot(driver);
				if(statususer.contentEquals(extusersta)){
					DriverScript.bResult = true;
					
		}
				else{
					//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tt);
					//Thread.sleep(500);
					ScreenshotCapture.takeScreenShot(driver);
					Log.error("Newly Created External User displayed under--- " + statususer);
					DriverScript.bResult = false;
					DriverScript.failedException = ("Newly Created External User displayed under : " + statususer);
				}
				
	}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("External User Not Present--- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
		
	}
	
	public static void VerifyTextfromtableandClickonresButton (String object ,String data) {
		//try{
		WebElement nodd = driver.findElement(By.name("F0_ctl00_ControlRef8_ControlRef15_ControlRef15_ControlRef8b_Main_Main_GridBand2_gridLoader$ctl14"));
		Select xyz = new Select(nodd);
		String expteamname = data.toLowerCase();
//		WebElement results = driver.findElement(By.xpath("//*[@id='F0_ctl00_ControlRef8_ControlRef15_ControlRef15_ControlRef8b_Main_Main_GridBand2_gridLoader_pagingControl']/span[1]"));
//		String xxxx = results.getText();
//		String[]  aTemp= xxxx.split("//s+");
//		String noofteam = aTemp[0];
//		Log.info(noofteam);
//		int result = Integer.parseInt(noofteam);
////		if(result < 10){
//		xyz.selectByValue("10");
//			
//		}
//		else if(result > 10 && result < 20){
//			xyz.selectByValue("20");
//		}
//		else if(result > 20 && result < 50){
//			xyz.selectByValue("50");
//		}
//		else if(result > 50 && result < 100){
//			xyz.selectByValue("100");
//		}
//		else if(result > 100 && result < 200){
//			xyz.selectByValue("200");
//		}
//		else if(result > 200){
//			xyz.selectByValue("500");
//		}
//		else{}
		//Thread.sleep(1000);		
		for(int i=0;i<=100;i++){
		String teamname = driver.findElement(By.id("F0_ctl00_ControlRef8_ControlRef15_ControlRef15_ControlRef8b_Main_Main_GridBand2_gridLoader_R0"+i+"_R0"+i+"_0_Label1")).getText();
		Log.info(teamname);							
		String Actualteamname = teamname.toLowerCase();
		//Thread.sleep(1000);
		if(Actualteamname.contains(expteamname)){
		driver.findElement(By.id("F0_ctl00_ControlRef8_ControlRef15_ControlRef15_ControlRef8b_Main_Main_GridBand2_gridLoader_R0"+i+"_Container4_Button3")).click();
        break;
				}		
			}
		}
	
	public static void howerandclickbyCord(String object, String data)
	{
		
		//try {
		//WebElement extteam = driver.findElement(By.id("F0_ctl00_ControlRef8_ControlRef15_ControlRef15_ControlRef2_ContainerTemplate2_RA_Label11"));	
		WebElement we = objectLocator(data);
		Actions act = new Actions(driver);
		Point pt = we.getLocation();
		 
		int NumberX=pt.getX();
		int NumberY=pt.getY();
		System.out.println(NumberX);
		System.out.println(NumberY);
		act.moveToElement(we).build().perform();
		
		
//		catch(Exception e){
//			ScreenshotCapture.takeScreenShot(driver);
//			Log.error("Expected Team Name not Displayed--- " + e.getMessage());
//			DriverScript.bResult = false;
//			DriverScript.failedException = e.getMessage();
//		}
		
	}
	
	public static void messagedisplayed(String object,String data)
		{
		try{
			
			//String errtxt = objectLocator(object).getCssValue("value");
			Highlight.highlightElement(objectLocator(object));
			String errtxt = objectLocator(object).getAttribute("value");
			//System.out.println("errtxt is :"  + errtxt);
			if ((errtxt != null)&(errtxt.contains(data))){
				Log.info(errtxt);
				DriverScript.bResult = true;
				System.out.println("Actual Text " + errtxt + " and " + " Expected Text " + data);
				
			}
			else{
			
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Expected Message Not Displayed");
			DriverScript.bResult = false;
			DriverScript.failedException = ("Actual Text " + errtxt + " and " + " Expected Text " + data);
			//DriverScript.failedException = "false";
			}
		}
			catch(Exception e){
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Expected Message Not Displayed");
				DriverScript.bResult = false;
				DriverScript.failedException = e.getMessage();
			}
		}	
		
	public static void hoveronelementClick(String object,String data ) throws InterruptedException
		
		{
			
			try{
			WebElement hoverEle = objectLocator(object);
			Highlight.highlightElement(hoverEle);
			Actions act = new Actions(driver);
		
			WebElement hoverClickEle = objectLocator(data);
			
			act.moveToElement(hoverEle).clickAndHold().build().perform();
			
			Log.info("Hover on the element");
			Highlight.highlightElement(hoverClickEle);
			hoverClickEle.click();
			Log.info("Clicked on the desired option");
			}
			catch(Exception e)
			{
				ScreenshotCapture.takeScreenShot(driver);
				Log.error("Not Able to Click on the --- " + e.getMessage());
				DriverScript.bResult = false;
				DriverScript.failedException = e.getMessage();	
				
				
			}
			
			}
		
	public static void highlightElement(String object,String data) throws InterruptedException
		{
	try {
		 WebElement element = objectLocator(object);
		 JavascriptExecutor js = (JavascriptExecutor) driver;
	   	 js.executeScript("arguments[0].setAttribute('style', 'background: dullwhite; border: 2px solid blue;');", element);
		 Thread.sleep(1000);
		 js.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "");
	}
	catch (InterruptedException e) {
		Log.error("Unable to highlight the element" + e.getMessage());
	} 
	 }
		
	public static void searchEmployeeorNonEmployee(String object, String data)
	{
		
		
	}
	
	public static void productResultreqbutton(String object, String data)
	{
		try{			
			Log.info("Clicking on Webelement "+ object);
			//data.toLowerCase()
			String tt ="//td[div[span[span[text()='" + data + "']]]]/following-sibling::td[3]/button";
			Highlight.highlightElement(driver.findElement(By.xpath(tt)));
			driver.findElement(By.xpath(tt)).click();
			DriverScript.bResult = true;
			//td[div[span[span[text()='GBS Insight - GBSQA_KK_Special@#@#$Char - Team']]]]/following-sibling::td[3]/button
			//td[div[span[span[text()='GBS Insight - GBSQA_KK_Special@#@#$Char - Team']]]]/preceding-sibling::td/div/span/input
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Search Result Not displayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void pendingReqdecision(String object,String data){
		
		try{
			//**Product Along with Decision user need to enter in Excel 
			String[] sTemp=data.split(",");
			String decision = sTemp[1];
			String prodname = sTemp[0];
			Log.info("Clicking on "+ decision);
			String xx = decision.toLowerCase();
			
			if(xx.contentEquals("approve")){
				driver.findElement(By.xpath("(//td[div[div[div[span[contains(text(),'"+ prodname +"')]]]]]/following-sibling::td[5]/span/div/input)[1]")).click();;
				DriverScript.bResult = true;
			}
			else if(xx.contentEquals("deny")){
				driver.findElement(By.xpath("(//td[div[div[div[span[contains(text(),'"+ prodname +"')]]]]]/following-sibling::td[5]/span/div/input)[2]")).click();;
				DriverScript.bResult = true;
			}
			
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Decision buttons not dislpayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
			
	}
	
	public static void pendingExternaluserattestation(String object,String data){
		
try{
			
			String[] sTemp=data.split(",");
			String decision = sTemp[1];
			String extemail = sTemp[0];
			Log.info("Clicking on "+ decision);
			String xx = decision.toLowerCase();
			
			if(xx.contentEquals("approve")){
				driver.findElement(By.xpath("(//td[span[contains(text(),'"+ extemail +"')]]/following-sibling::td/span/div/input)[1]")).click();
				DriverScript.bResult = true;
			}
				else if(xx.contentEquals("deny")){
				driver.findElement(By.xpath("//(td[span[contains(text(),'"+ extemail +"')]]/following-sibling::td/span/div/input)[2]")).click();
				DriverScript.bResult = true;
				}
			
		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Decision buttons not dislpayed --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
		
			
	}
	
	public static void whitePagenameverification(String object,String data){
		
		try{
			while (driver.findElement(By.xpath("(//button[contains(@title,'Go to page')])[2]")).isDisplayed()){
				
				for(int i=1;i<21;i++){
					WebElement whitepageUsername = driver.findElement(By.xpath("(//tr/td/div/span/span)["+i+"]"));
					String name = whitepageUsername.getText();
					Highlight.highlightElement(whitepageUsername);
					
					if(data.contains(name)){
						DriverScript.bResult = true;
						break;
					}
			
					else if(data != name & i >= 20 & driver.findElement(By.xpath("(//button[contains(@title,'Go to page')])[2]")).isEnabled() 
											& driver.findElement(By.xpath("(//button[contains(@title,'Go to page')])[2]")).isDisplayed()){
						driver.findElement(By.xpath("(//button[contains(@title,'Go to page')])[2]")).click();
						Thread.sleep(4000);
						Log.info("User not found on first page , So navigating to th next page :");
						i=0;
					}
					
				}
				
			}
		
		}		


		catch (Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("User not found --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}

	public static void whitePagesearch(String object,String data){
		
		try{
			
		
		String[] sTemp=data.split(" ");
		String fpart = sTemp[0];
		String lpart = sTemp[1];
		String initfpart = fpart.substring(0, 1).toUpperCase() + fpart.substring(1);
		String initlpart = lpart.substring(0, 1).toUpperCase() + lpart.substring(1);
		String lfname = initlpart +','+' '+initfpart;
	
		if(driver.findElements(By.xpath("//tr/td/div/span/span[contains(text(),'"+lfname+"')]")).size()>0){
		Thread.sleep(2000);
		Highlight.highlightElement(driver.findElement(By.xpath("//tr/td/div/span/span[contains(text(),'"+lfname+"')]")));
		DriverScript.bResult = true;	
		}
		else{
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("User not found --- ");
			DriverScript.bResult = false;
			DriverScript.failedException = ("User not found --- "+ data + lfname);
			}


		}
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("User not found --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
		}
	}
	
	public static void WaitForMinutes(String object,String data) throws InterruptedException{
	
	Thread.sleep(200000);
	
	}
	
	
	public static void selectMatchingChkbox(String object,String data) {
	
	try{
		
		driver.findElement(By.xpath("//span[contains(text(),data)]/../../child::td//input")).click();
		Log.info("Selected the Matching Record Check box");
	}
	
	
	catch(Exception e){
		ScreenshotCapture.takeScreenShot(driver);
		Log.error("User not found --- " + e.getMessage());
		DriverScript.bResult = false;
		DriverScript.failedException = e.getMessage();
		
		
	}
	
}
	
	
	public static void ChooseTemplate(String object,String data) {
		
		try{
			
			driver.findElement(By.xpath("//td//a[contains(text(),'"+data+"')]/parent::*/following-sibling::*//button")).click();
			Log.info("Select button clicked for desired record");
		}
		
		
		catch(Exception e){
			ScreenshotCapture.takeScreenShot(driver);
			Log.error("Particular Template not found --- " + e.getMessage());
			DriverScript.bResult = false;
			DriverScript.failedException = e.getMessage();
			
			
		}
		
	}
		
	
	
	
}




