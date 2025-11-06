package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("Admin");
        loginPage.enterPassword("admin123");
        loginPage.clickLogin();

        String currentURL = driver.getCurrentUrl();
        Assert.assertTrue(currentURL.contains("dashboard"),
                "Login failed! Dashboard not opened.");
    }
}
