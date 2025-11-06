package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DriverFactory;
import utils.ScreenshotUtil;

public class Hooks {

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Scenario Screenshot");

                ScreenshotUtil.saveScreenshot(DriverFactory.getDriver(), scenario.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DriverFactory.quitDriver();
        }
    }
}
