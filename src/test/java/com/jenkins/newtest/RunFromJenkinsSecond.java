package com.jenkins.newtest;

import com.codeborne.selenide.Selenide;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RunFromJenkinsSecond {
    @Test
    public void testthree(){
        Selenide.open("https://www.mailinator.com");
       // System.out.println("checking logger two");
     //   Selenide.$(".abc").click();
   //     Assert.assertEquals(1,2, "failing test");
       // ExtentTestManager.getTest().log(LogStatus.FAIL, "Hellooo started base test1");
   }

}
