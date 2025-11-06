package utils;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {

    public static void saveScreenshot(WebDriver driver, String scenarioName) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "target/screenshots/" + scenarioName.replaceAll(" ", "_") + ".png";
            FileUtils.copyFile(srcFile, new File(path));
            System.out.println("📸 Screenshot saved at: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
