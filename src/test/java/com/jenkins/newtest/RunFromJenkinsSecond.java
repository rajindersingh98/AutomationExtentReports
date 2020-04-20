package com.jenkins.newtest;

import com.codeborne.selenide.Selenide;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
@Listeners(TestListener.class)
public class RunFromJenkinsSecond extends BaseTest{
    @Test
    public void testthree(){
        Selenide.open("https://www.mailinator.com");
        Selenide.$(".abc").click();
   }

}
