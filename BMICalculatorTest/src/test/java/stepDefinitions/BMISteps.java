package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.junit.Assert;
import utils.DriverFactory;

public class BMISteps {

    WebDriver driver = DriverFactory.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));

    @Given("I open the BMI calculator")
    public void i_open_the_bmi_calculator() {
        driver.get("https://www.calculator.net/bmi-calculator.html");
        driver.manage().window().maximize();
    }

    @And("I select the {string} tab")
    public void i_select_the_tab(String tabName) {
        try {
            WebElement metricTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[text()='" + tabName + "']")));
            metricTab.click();
            System.out.println("✅ '" + tabName + "' tab selected successfully.");
        } catch (Exception e) {
            Assert.fail(" Failed to select tab: " + tabName + " - " + e.getMessage());
        }
    }

    @When("I enter age {string}, gender {string}, height {string}, and weight {string}")
    public void i_enter_age_gender_height_and_weight(String age, String gender, String height, String weight) {
        try {
            WebElement ageInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='cage']")));
            ageInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            ageInput.sendKeys(age);

            if (gender.equalsIgnoreCase("male")) {
                WebElement maleRadio = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//label[@for='csex1']")));
                maleRadio.click();
            } else {
                WebElement femaleRadio = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//label[@for='csex2']")));
                femaleRadio.click();
            }

            WebElement heightInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='cheightmeter']")));
            heightInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            heightInput.sendKeys(height);

            WebElement weightInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='ckg']")));
            weightInput.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
            weightInput.sendKeys(weight);

            System.out.println("Age, Gender, Height, and Weight entered successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to enter BMI inputs: " + e.getMessage());
        }
    }

    @And("I calculate BMI")
    public void i_calculate_bmi() {
        WebElement calcButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Calculate']")));
        calcButton.click();
        System.out.println("Calculate button clicked.");
    }

    @Then("I should see the entered height {string} and weight {string} reflected correctly")
    public void i_should_see_the_entered_height_and_weight_reflected_correctly(String expectedHeight, String expectedWeight) {
        try {
            WebElement heightField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='cheightmeter']")));
            WebElement weightField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@id='ckg']")));

            String actualHeight = heightField.getAttribute("value").trim();
            String actualWeight = weightField.getAttribute("value").trim();

            Assert.assertEquals("Height mismatch!", expectedHeight, actualHeight);
            Assert.assertEquals("Weight mismatch!", expectedWeight, actualWeight);

            System.out.println("Height and Weight validated successfully.");

        } catch (Exception e) {
            Assert.fail("Failed to validate height/weight reflection: " + e.getMessage());
        }
    }

    @Then("I should see a valid BMI result")
    public void i_should_see_a_valid_bmi_result() {
        WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='bigtext']/b")));
        String resultText = resultElement.getText();
        System.out.println("Raw BMI Text: " + resultText);

        String numericPart = resultText.replaceAll("[^0-9.]", "");
        Assert.assertTrue("Invalid BMI numeric value: " + resultText,
                numericPart.matches("\\d+(\\.\\d+)?"));

        System.out.println("BMI Value verified successfully: " + numericPart);
    }
}
