package com.jenkins.newtest;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    @BeforeTest
    public void beforeTest() {
        Configuration.remote = "http://localhost:4444/wd/hub";
    }
}
