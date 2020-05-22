package com.jenkins.newtest;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class MyFirstSerenityTest {
    @Managed
    WebDriver driver;

    @Test
    public void myFirstTest(){
      driver.navigate().to("https://google.com");
    }
}
