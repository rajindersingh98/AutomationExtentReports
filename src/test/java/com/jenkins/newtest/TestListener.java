package com.jenkins.newtest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in on Start method " + iTestContext.getName());
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println(("*** Test Suite " + iTestContext.getName() + " ending ***"));
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println(("*** Running test method " + iTestResult.getMethod().getMethodName() + "..."));
       // ExtentTestManager.startTest(iTestResult.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("*** Executed " + iTestResult.getMethod().getMethodName() + " test successfully...");
        ExtentTestManager.getTest().log(LogStatus.PASS, "Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("*** Test execution " + iTestResult.getMethod().getMethodName() + " failed...");
        String testClassName = iTestResult.getName();
        Configuration.reportsFolder=System.getProperty("user.dir") +"/test-output/screenshot/";
        Selenide.screenshot(testClassName);
        System.out.println("after screenshot path"+System.getProperty("user.dir") +"\\test-output\\screenshot\\"+testClassName+".png");
        ExtentTestManager.getTest().log(LogStatus.FAIL, iTestResult.getThrowable().getMessage(),ExtentTestManager.getTest().addScreenCapture(System.getProperty("user.dir") +"\\test-output\\screenshot\\"+testClassName+".png"));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }
}
