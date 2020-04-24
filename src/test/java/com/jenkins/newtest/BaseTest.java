package com.jenkins.newtest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.After;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    @BeforeTest
    public void beforeTest() {
       // Configuration.remote = "http://localhost:4444/wd/hub";
    }

    @AfterTest
    public void afterTest(){
        Selenide.closeWebDriver();
    }
}
