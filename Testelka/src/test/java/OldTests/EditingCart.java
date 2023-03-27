package OldTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class EditingCart {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    public void testsStart() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.navigate().to("https://fakestore.testelka.pl/");
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
        By cookieConsentBar = By.xpath(".//a[@class = 'woocommerce-store-notice__dismiss-link']");
        driver.findElement(cookieConsentBar).click();
        By product = By.xpath(".//li[contains(@class, 'post-61')]");
        driver.findElement(product).click();
        By button = By.xpath(".//button[@name = 'add-to-cart']");
        driver.findElement(button).click();
        By goToCart = By.xpath(".//div/a[@class='button wc-forward']");
        driver.findElement(goToCart).click();
    }

    @AfterEach
    public void driverQuit() {
        driver.quit();
    }

    @Test //użytkownik ma możliwość zmiany ilości wybranej wycieczki (pojedynczej pozycji) na stronie koszyka
    public void editCartQuantity() {
        wait.until(ExpectedConditions.urlContains("https://fakestore.testelka.pl/koszyk/"));
        By quantityField = By.xpath(".//input[starts-with(@id,'quantity')]");
        driver.findElement(quantityField).click();
        driver.findElement(quantityField).clear();
        driver.findElement(quantityField).sendKeys("2");

        By updateCartButton = By.xpath(".//button[@name = 'update_cart']");
        driver.findElement(updateCartButton).click();
        WebElement spinner = driver.findElement(By.xpath(".//div[@class ='blockUI blockOverlay']"));
        wait.until(ExpectedConditions.invisibilityOf(spinner));

        WebElement cartActualAlert = driver.findElement(By.xpath(".//div[@class ='woocommerce-message']"));
        wait.until(ExpectedConditions.visibilityOf(cartActualAlert));
        Assertions.assertTrue(cartActualAlert.isDisplayed(), "Message is not displayed. Cart was probably not edited");
    }

    @Test //użytkownik ma możliwość usunięcia wycieczki na stronie koszyka (całej pozycji)
    public void deleteProductFromCart() {
        By deleteButton = By.xpath(".//a[@class = 'remove']");
        driver.findElement(deleteButton).click();
        WebElement spinner = driver.findElement(By.xpath(".//div[@class ='blockUI blockOverlay']"));
        wait.until(ExpectedConditions.invisibilityOf(spinner));
        String actualCartEmptyInfo = driver.findElement(By.xpath(".//p[@class = 'cart-empty woocommerce-info']")).getText();
        String expectedCartEmptyInfo = "Twój koszyk jest pusty.";
        Assertions.assertEquals(expectedCartEmptyInfo, actualCartEmptyInfo, "Product was not deleted");
    }

}
