package com.jenkins.serenity;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
@RunWith(SerenityRunner.class)
public class MySerenityTest {
    @Managed
    WebDriver driver;
    @Title("Rajinder test")
    @Test
    public void myFirstTest(){
        driver.navigate().to("https://google.com");
    }
}
