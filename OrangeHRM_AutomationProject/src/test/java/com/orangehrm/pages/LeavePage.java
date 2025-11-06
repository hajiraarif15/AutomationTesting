package com.orangehrm.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class LeavePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By leaveMenu = By.xpath("//span[text()='Leave']");
    private final By applyButtonLink = By.linkText("Apply"); // friend used this
    private final By myLeaveLink = By.linkText("My Leave");

    // leave form locators
    private final By leaveTypeDropdown = By.xpath("//label[text()='Leave Type']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text-input')]");
    private final By leaveTypeOptions = By.xpath("//div[@role='option']//span");
    private final By fromDateField = By.xpath("//label[text()='From Date']/parent::div/following-sibling::div//input");
    private final By toDateField = By.xpath("//label[text()='To Date']/parent::div/following-sibling::div//input");
    private final By commentsField = By.xpath("//label[text()='Comments']/parent::div/following-sibling::div//textarea");
    private final By submitApplyButton = By.cssSelector("button[type='submit']");
    private final By leaveListTable = By.cssSelector(".oxd-table"); // my leave table

    public LeavePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    // navigate to Leave menu (common)
    public void navigateToLeaveMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveMenu)).click();
    }

    // open Apply page
    public void openApply() {
        navigateToLeaveMenu();
        wait.until(ExpectedConditions.elementToBeClickable(applyButtonLink)).click();
        // wait for form to appear - either leave type dropdown or from date visible
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(leaveTypeDropdown),
                ExpectedConditions.visibilityOfElementLocated(fromDateField)
        ));
    }

    // choose leave type by visible text; if text not found, choose first option (fallback)
    public void selectLeaveType(String visibleText) {
        retryClick(leaveTypeDropdown);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(leaveTypeOptions));
        List<WebElement> options = driver.findElements(leaveTypeOptions);
        for (WebElement opt : options) {
            if (opt.getText().trim().equalsIgnoreCase(visibleText.trim())) {
                safeClick(opt);
                return;
            }
        }
        // fallback: choose first non-empty
        if (!options.isEmpty()) {
            safeClick(options.get(0));
        }
    }

    public void setFromDate(String date) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(fromDateField));
        clearAndType(el, date);
    }

    public void setToDate(String date) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(toDateField));
        clearAndType(el, date);
    }

    public void setComments(String comments) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(commentsField));
        el.sendKeys(comments);
    }

    public void clickApplySubmit() {
        retryClick(submitApplyButton);
        // small wait for submission to process
        sleep(1000);
    }

    // high-level apply
    public void applyLeave(String fromDate, String toDate, String comments, String leaveTypeVisibleText) {
        openApply();
        selectLeaveType(leaveTypeVisibleText);
        setFromDate(fromDate);
        setToDate(toDate);
        setComments(comments);
        clickApplySubmit();
    }

    // open My Leave page and wait for leave list
    public void openMyLeave() {
        navigateToLeaveMenu();
        wait.until(ExpectedConditions.elementToBeClickable(myLeaveLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(leaveListTable));
    }

    // simple verification that table is visible and has at least one row
    public boolean isMyLeaveListVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(leaveListTable));
            WebElement table = driver.findElement(leaveListTable);
            return table.isDisplayed() && table.findElements(By.xpath(".//div[@role='row']")).size() > 0;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    // ---------- helper utilities ----------
    private void clearAndType(WebElement el, String text) {
        try {
            el.clear();
            el.sendKeys(text);
        } catch (InvalidElementStateException | StaleElementReferenceException ex) {
            WebElement fresh = wait.until(ExpectedConditions.elementToBeClickable(elLocator(el)));
            fresh.clear();
            fresh.sendKeys(text);
        }
    }

    // approximate locator fallback for stale element (best-effort)
    private By elLocator(WebElement el) {
        try {
            String tag = el.getTagName();
            if ("input".equalsIgnoreCase(tag)) return By.xpath("//input");
            if ("textarea".equalsIgnoreCase(tag)) return By.xpath("//textarea");
        } catch (StaleElementReferenceException ignored) {}
        return By.xpath("//*");
    }

    private void retryClick(By by) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException | TimeoutException ex) {
                attempts++;
                sleep(300);
            }
        }
        // final attempt (let exception bubble if fails)
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
        el.click();
    }

    private void safeClick(WebElement el) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                el.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException ex) {
                attempts++;
                sleep(250);
            }
        }
        el.click();
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
