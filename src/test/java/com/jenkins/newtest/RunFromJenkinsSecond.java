package com.jenkins.newtest;

import com.codeborne.selenide.Selenide;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RunFromJenkinsSecond {
    @Test
    public void testthree(){
        Selenide.open("https://www.mailinator.com");
        Selenide.$(".abc").click();
   }

}
