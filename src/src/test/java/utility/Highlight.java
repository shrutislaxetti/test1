package utility;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import library.ActionKeywords;

public class Highlight {
	
	
public static void highlightElement(WebElement element){
	try {
		
		 JavascriptExecutor js = (JavascriptExecutor)ActionKeywords.driver;
	     js.executeScript("arguments[0].setAttribute('style', 'background: dullwhite; border: 4px solid yellow;');", element);
		 Thread.sleep(500);
		 js.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "");
	}
	catch (Exception e) {
		Log.error("Unable to highlight the element" + e.getMessage());
	} 
	 }
		
}
