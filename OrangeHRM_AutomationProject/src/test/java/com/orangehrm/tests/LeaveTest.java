package com.orangehrm.tests;

import com.orangehrm.base.BaseTest;
import com.orangehrm.pages.LeavePage;
import com.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LeaveTest extends BaseTest {

    @Test(description = "Login -> Apply Leave (CAN - Vacation) -> Verify in My Leave")
    public void testApplyAndVerifyLeave() {
        LoginPage login = new LoginPage(driver);
        login.login("Admin", "admin123");
        
        LeavePage leave = new LeavePage(driver);
        String leaveType = "CAN - Vacation";
        String fromDate = "2025-11-10";
        String toDate = "2025-11-10";
        String comments = "Automation - CAN Vacation";

        leave.applyLeave(fromDate, toDate, comments, leaveType);

        leave.openMyLeave();
        boolean present = leave.isMyLeaveListVisible();
        Assert.assertTrue(present, "Leave entry not visible in My Leave list");
        System.out.println("Leave applied and visible in My Leave.");
    }
}
