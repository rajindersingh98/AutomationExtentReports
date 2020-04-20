package com.jenkins.newtest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class RunOnSeleniumGrid {
    //Setting up Hub Command : java -jar selenium-server-standalone-3.141.59.jar -role hub
    //Setting up node with Chrome Driver on node and Registering same on hub :
    //java -Dwebdriver.chrome.driver="F:\chromedriver\chromedriver_win32\chromedriver.exe" -jar selenium-server-standalone-3.141.59.jar -role node -hub http://192.168.0.128:4444/grid/register -port 5555
    @Test
    public void testRunOnSeleniumGrid() throws MalformedURLException {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Selenide.open("https://www.google.com");
        Selenide.$(".abc").click();
    }
}
