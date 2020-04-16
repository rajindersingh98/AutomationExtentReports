package com.jenkins.newtest;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@Listeners(TestListener.class)
public class RunFromJenkins {
    private SoftAssert softAssert =null;

    @Test
    public void test(){
        softAssert = new SoftAssert();
        softAssert.assertEquals(1,3 , "Test 1 Passed");
        softAssert.assertEquals(1,1, "Test 1 Failed");
        softAssert.assertAll();
   }

   @Test
    public void testNew(){
       softAssert = new SoftAssert();
       softAssert.assertEquals(1,3 ,"Test 2 Failed one");
       softAssert.assertEquals(2,3 , "test 2 Failed 2");
       softAssert.assertAll();
    }

   @AfterTest
    public void afterMethod() {
       softAssert =null;
   }
}
