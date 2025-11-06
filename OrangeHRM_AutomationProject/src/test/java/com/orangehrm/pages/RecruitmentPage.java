package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RecruitmentPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By recruitmentMenuLink = By.xpath("//span[text()='Recruitment']");
    private By addCandidateButton = By.xpath("//button[normalize-space()='Add']");
    private By firstNameInput = By.name("firstName");
    private By middleNameInput = By.name("middleName");
    private By lastNameInput = By.name("lastName");
    private By emailInput = By.xpath("//label[text()='Email']/parent::div/following-sibling::div//input");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By candidatesPageLink = By.linkText("Candidates");
    private By candidatesTable = By.cssSelector(".oxd-table");
    private By deleteIcon = By.xpath("//i[contains(@class,'bi-trash')]");
    private By confirmDeleteBtn = By.xpath("//button[normalize-space()='Yes, Delete']");
    private By editIcon = By.xpath("//i[contains(@class,'bi-pencil')]");

    public RecruitmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Navigate to Recruitment main page
    public void openRecruitmentPage() {
        wait.until(ExpectedConditions.elementToBeClickable(recruitmentMenuLink));
        driver.findElement(recruitmentMenuLink).click();
    }

    // Click 'Add' button
    public void pressAddCandidate() {
        wait.until(ExpectedConditions.elementToBeClickable(addCandidateButton));
        driver.findElement(addCandidateButton).click();
    }

    // Fill candidate details
    public void typeFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput)).clear();
        driver.findElement(firstNameInput).sendKeys(firstName);
    }

    public void typeMiddleName(String middleName) {
        driver.findElement(middleNameInput).clear();
        driver.findElement(middleNameInput).sendKeys(middleName);
    }

    public void typeLastName(String lastName) {
        driver.findElement(lastNameInput).clear();
        driver.findElement(lastNameInput).sendKeys(lastName);
    }

    public void typeEmail(String email) {
        driver.findElement(emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    // Save candidate
    public void pressSaveButton() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    // Add a new candidate
    public void addCandidate(String firstName, String middleName, String lastName, String email) {
        openRecruitmentPage();
        pressAddCandidate();
        typeFirstName(firstName);
        typeMiddleName(middleName);
        typeLastName(lastName);
        typeEmail(email);
        pressSaveButton();
    }

    // Navigate to Candidates list
    public void openCandidatesPage() {
        openRecruitmentPage();
        wait.until(ExpectedConditions.elementToBeClickable(candidatesPageLink));
        driver.findElement(candidatesPageLink).click();
    }

    // Check if candidate table is visible
    public boolean isCandidatesTableVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(candidatesTable));
            return driver.findElement(candidatesTable).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Delete first candidate in the list
    public void deleteFirstCandidate() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteIcon));
        driver.findElements(deleteIcon).get(0).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn));
        driver.findElement(confirmDeleteBtn).click();
    }

    // Edit first candidate in the list
    public void editFirstCandidate() {
        wait.until(ExpectedConditions.elementToBeClickable(editIcon));
        driver.findElements(editIcon).get(0).click();
    }

    // Update middle name of candidate
    public void updateCandidateMiddleName(String newMiddleName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(middleNameInput));
        WebElement middleNameField = driver.findElement(middleNameInput);
        middleNameField.clear();
        middleNameField.sendKeys(newMiddleName);
        pressSaveButton();
    }
}
