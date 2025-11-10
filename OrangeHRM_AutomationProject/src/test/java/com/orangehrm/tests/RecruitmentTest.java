package com.orangehrm.tests;

import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.RecruitmentPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RecruitmentTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private RecruitmentPage recruitmentPage;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        loginPage = new LoginPage(driver);
        recruitmentPage = new RecruitmentPage(driver);

        loginPage.login("Admin", "admin123");
        Assert.assertTrue(loginPage.isDashboardDisplayed(), "Dashboard not visible!");
        System.out.println("Admin logged in successfully");
    }

    @Test(priority = 1, description = "Add candidate")
    public void testAddCandidate() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        recruitmentPage.addCandidate(
                "Hajira" + timestamp.substring(timestamp.length() - 4),
                "Test",
                "User",
                "test" + timestamp.substring(timestamp.length() - 6) + "@mail.com"
        );

        System.out.println("Candidate added");
    }

    @Test(priority = 2, description = "View candidates list")
    public void testViewCandidates() {
        recruitmentPage.openCandidatesPage();
        Assert.assertTrue(recruitmentPage.isCandidatesTableVisible(), "Candidates table not visible!");
        System.out.println("Candidates list is visible");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
