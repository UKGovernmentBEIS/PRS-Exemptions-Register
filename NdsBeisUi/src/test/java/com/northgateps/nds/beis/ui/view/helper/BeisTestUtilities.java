package com.northgateps.nds.beis.ui.view.helper;

import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import com.northgateps.nds.platform.ui.selenium.core.NdsUiWait;

/**
 * Helper methods for the beis tests 
 */
public class BeisTestUtilities  {
    
    private static final String defaultFileLocation = "file:/C:/grid/beis/uploadfiles/";
    
    public String applyGridLocation() {
        return defaultFileLocation;
    }

    public void selectFile(WebDriver driver, String filename) {
        NdsUiWait wait = new NdsUiWait(driver);        
        wait.untilElementEnabled(By.id("resource"));
        WebElement el = driver.findElement(By.id("resource"));
        System.out.println("selectFile driver class = " +((RemoteWebElement) el).getWrappedDriver().getClass().getName());
        if (((RemoteWebElement) el).getWrappedDriver().getClass().getName().contains("Chrome") || ((RemoteWebElement) el).getWrappedDriver().toString().contains("chrome")) {
            // For Chrome, the fileDetector MUST be set. For Firefox, it MUST NOT be set
            // also Firefox needs the "file:/" prefix to the filename, but it must not be present for Chrome. 
            ((RemoteWebElement) el).setFileDetector(new LocalFileDetector());
            el.sendKeys(chromePath(getFilePath(filename)));
        } else {
            el.sendKeys(geckoDriverPath(getFilePath(filename)));
        }
    }
    
    /** Work out where the files are. */
    private String getFilePath(String fileName) {
    	String altFileGridLocation = System.getProperty("nds.integrationTest.uploadFilePath", null);
    	
    	if (altFileGridLocation != null) {
    		return Paths.get(altFileGridLocation, fileName).toString();
        } else {
        	return defaultFileLocation + fileName;
        }
    }
    
    private String chromePath(String path) {
        if (path.startsWith("file:/")) {
            return path.substring(6);
        } else {
            return path;
        }
    }
    
    private String geckoDriverPath(String path) {
        path = chromePath(path);
        path = path.replace("/", "\\\\");
        return path;
    }

}
