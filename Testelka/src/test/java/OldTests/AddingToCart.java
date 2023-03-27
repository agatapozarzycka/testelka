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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddingToCart {
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

    }

    @AfterEach
    public void driverQuit() {
        driver.quit();
    }

    @Test
    public void addToCartFromProductPage() {
        //użytkownik ma możliwość dodania wybranej wycieczki do koszyka ze strony tej wycieczki
        By product = By.xpath(".//li[contains(@class, 'post-61')]");
        driver.findElement(product).click();
        By button = By.xpath(".//button[@name = 'add-to-cart']");
        driver.findElement(button).click();
        String actualText = driver.findElement(By.xpath(".//div/a[@class='button wc-forward']")).getText();
        String expectedText = "Zobacz koszyk";
        assertEquals(expectedText, actualText, "Product was not added to the cart");
    }

    @Test //użytkownik ma możliwość dodania wybranej wycieczki do koszyka ze strony kategorii,
    public void addToCartFromCategoryPage() {
        By category = By.xpath(".//li[@class = 'product-category product']");
        driver.findElement(category).click();
        By product = By.xpath(".//li[contains(@class, 'post-40')]");
        driver.findElement(product).click();
        By button = By.xpath(".//button[@name = 'add-to-cart']");
        driver.findElement(button).click();
        String actualText = driver.findElement(By.xpath(".//div/a[@class='button wc-forward']")).getText();
        String expectedText = "Zobacz koszyk";
        assertEquals(expectedText, actualText, "Product was not added to the cart");
    }

    //
    @Test
    public void addToCartMoreThanTenProducts() throws InterruptedException {
        //użytkownik ma możliwość dodania co najmniej 10 wycieczek do koszyka (w sumie i w dowolnej kombinacji)
        By buttonAddToCart1 = By.xpath(".//section[contains(@class, 'storefront-recent-products')]//a[@data-product_id='4116']");
        for (int i = 0; i < 5; i++) {
            driver.findElement(buttonAddToCart1).click();
        }

        By buttonAddToCart2 = By.xpath("//section/div/ul/li/a[contains(text(), 'Dodaj do koszyka')]");
        for (int i = 0; i <= 5; i++) {
            sleep(5000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElements(buttonAddToCart2).get(2)));
            driver.findElements(buttonAddToCart2).get(2).click();
        }

        By buttonGoToCart = By.xpath(".//section[contains(@class, 'storefront-recent-products')]/div/ul/li[3]/a[@class = 'added_to_cart wc-forward']");
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonGoToCart));
        driver.findElement(buttonGoToCart).click();

        WebElement productQuantityField1 = driver.findElement(By.xpath(".//tbody/tr[1]/td[@class = 'product-quantity']/div/input"));
        String actualQuantity1 = productQuantityField1.getAttribute("value");

        WebElement productQuantityField2 = driver.findElement(By.xpath(".//tbody/tr[2]/td[@class = 'product-quantity']/div/input"));
        String actualQuantity2 = productQuantityField2.getAttribute("value");

        assertAll("testAssertions",
                () -> assertEquals("5", actualQuantity1, "Wrong quantity"),
                () -> assertEquals("5", actualQuantity2, "Wrong quantity")
        );
    }

    @Test
    public void changeProductQuantityOnProductPage() {
        //użytkownik ma możliwość wybrania ilości wycieczek, które chce zakupić, na stronie produktu (np. dla zamówienia dla kilku osób)
        By product = By.xpath(".//li[contains(@class, 'post-61')]");
        driver.findElement(product).click();
        WebElement quantityField = driver.findElement(By.xpath(".//input[@name = 'quantity']"));
        quantityField.clear();
        quantityField.sendKeys("7");
        By button = By.xpath(".//button[@name = 'add-to-cart']");
        driver.findElement(button).click();

        WebElement goToCartButton = driver.findElement(By.xpath(".//div/a[@class='button wc-forward']"));
        goToCartButton.click();

        WebElement quantityFieldInCart = driver.findElement(By.xpath(".//input[starts-with(@id, 'quantity')]"));
        String actualQuantityInFieldInCart = quantityFieldInCart.getAttribute("value");
        Assertions.assertEquals("7", actualQuantityInFieldInCart, "Wrong quantity");
    }

    @Test
    public void addTenDifferentProductsToCart() throws InterruptedException {
        //użytkownik ma możliwość dodania 10 różnych wycieczek do koszyka
        driver.navigate().to("https://fakestore.testelka.pl/shop/");
        WebElement buttonGoToWindsurfing = driver.findElement(By.xpath(".//h2[contains(text(), 'Windsurfing')]"));
        buttonGoToWindsurfing.click();
        By buttonAddToCart = By.xpath(".//a[contains(text(), 'Dodaj do koszyka')]");

        for (int i = 0; i < 5; i++) {
            driver.findElements(buttonAddToCart).get(i).click();
            Thread.sleep(2000);
        }

        driver.navigate().back();
        WebElement buttonGoToYoga = driver.findElement(By.xpath(".//h2[contains(text(), 'Yoga i pilates')]"));
        buttonGoToYoga.click();
        for (int i = 0; i < 5; i++) {
            driver.findElements(buttonAddToCart).get(i).click();
            Thread.sleep(2000);
        }

        Thread.sleep(3000);
        By buttonGoToCart = By.xpath(".//a[contains(text(), 'Zobacz koszyk') and contains(@class, 'added_to_cart')]");
        driver.findElements(buttonGoToCart).get(4).click();

        List<WebElement> myElements = driver.findElements(By.xpath(".//div[@class = 'quantity']"));
        Assertions.assertEquals(10, myElements.size(), "the is less than 10 products");
    }

}
