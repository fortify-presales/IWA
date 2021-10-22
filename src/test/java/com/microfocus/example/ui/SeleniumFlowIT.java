package com.microfocus.example.ui;

import com.microfocus.example.BaseSeleniumTest;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;

public class SeleniumFlowIT extends BaseSeleniumTest {

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        // tell the system where to find geckodriver on Windows.
        //System.setProperty("webdriver.gecko.driver", "bin/selenium/windows/geckodriver.exe");
        // tell the system where to find chromedriver on Windows.
        System.setProperty("webdriver.chrome.driver", "bin/selenium/windows/chromedriver.exe");

        //driver = new FirefoxDriver();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void UITest1() {

        driver.get("http://localhost:" + getPort() + "/");
        driver.manage().window().setSize(new Dimension(1200, 800));
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement shopNowButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("SHOP NOW")));
        shopNowButton.click();

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("keywords")));
        searchInput.click();
        searchInput.sendKeys("alphadex");
        searchInput.sendKeys(Keys.ENTER);

        WebElement searchResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a:nth-child(1) .img-thumbnail")));
        searchResults.click();

        WebElement addToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart")));
        addToCartButton.click();

        /*
        driver.findElement(By.id("keywords")).click();
        driver.findElement(By.id("keywords")).sendKeys("alphadex");
        driver.findElement(By.id("keywords")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("a:nth-child(2) .img-thumbnail")).click();
        {
            WebElement element = driver.findElement(By.id("add-to-cart"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.id("add-to-cart")).click();
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
*/

/*
        //download_button_path = "//[@class='lmn-edititem-modal']/../[@class=''btn-primary']"
        //WebDriverWait wait = WebDriverWait(driver, 10);
        //download_button = wait.until(EC.visibility_of_element_located((By.XPATH, download_button_path)))
        //download_button .click()
        driver.findElement(By.id("checkout-now")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".col-lg-6:nth-child(1) > .banner-1"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }
        driver.findElement(By.cssSelector("#eec467c8-5de9-4c7c-8541-7b31614d31a0 .input-group-append > .btn")).click();
        driver.findElement(By.id("checkout-cart")).click();
        driver.findElement(By.id("username")).sendKeys("user1");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("login-submit")).click();
        driver.findElement(By.cssSelector(".btn-lg")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".col-lg-6:nth-child(2) > .banner-1"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        driver.findElement(By.cssSelector(".lead span")).click();
        driver.findElement(By.linkText("Logout")).click();

 */
    }
}
