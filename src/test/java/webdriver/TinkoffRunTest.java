package com.tinkoff.webdriver;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by 2fast on 02.07.2017.
 */
public class TinkoffRunTest {

    @Before
    public void startWebDriver(){
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");

    }
    @Test
    public void runTest(){

        WebDriver driver = new FirefoxDriver();

        driver.navigate().to("https://www.tinkoff.ru/");
        driver.quit();
    }
}
