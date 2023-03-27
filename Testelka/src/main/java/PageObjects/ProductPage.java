package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage extends BasePage {
    private final WebDriverWait wait;
    private final By addToCartButtonLocator = By.xpath(".//button[@name = 'add-to-cart']");
    private final By viewCartButtonLocator = By.xpath(".//div/a[@class='button wc-forward']");
    private final By productQuantityFieldLocator = By.xpath(".//input[@name = 'quantity']");

    public ProductPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 5);
    }

    public ProductPage addToCart() {
        driver.findElement(addToCartButtonLocator).click();
        return new ProductPage(driver);
    }

    public CartPage viewCart() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCartButtonLocator)).click();
        return new CartPage(driver);
    }

    public ProductPage changeProductQuantity(int productQuantity) {
        WebElement quantityField = driver.findElement(productQuantityFieldLocator);
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(productQuantity));
        return new ProductPage(driver);
    }
}
