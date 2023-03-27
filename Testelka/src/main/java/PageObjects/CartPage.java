package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage extends BasePage {
    private final WebDriverWait wait;
    private final By shopTableLocator = By.xpath(".//article[@id='post-6']");
    private final By cartItemLocator = By.xpath("//tr[contains(@class, 'cart_item')]");
    private final By quantityFieldInCartLocator = By.xpath(".//input[starts-with(@id, 'quantity')]");
    private final By updateCartButtonLocator = By.xpath(".//button[@name = 'update_cart']");
    private final By spinnerLocator = By.xpath(".//div[@class ='blockUI blockOverlay']");
    private final By deleteButtonLocator = By.xpath(".//a[@class = 'remove']");
    private final By checkOutCartButtonLocator = By.xpath(".//a[@class = 'checkout-button button alt wc-forward']");

    public CartPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 5);
    }

    public boolean isProductInCart() {
        waitForShopTable();
        int cartItem = driver.findElements(cartItemLocator).size();
        return (cartItem == 1);
    }

    public String getProductQuantity() {
        waitForShopTable();
        String quantityInField = driver.findElement(quantityFieldInCartLocator).getAttribute("value");
        return quantityInField;
    }

    public List<WebElement> getProductsQuantity() {
        List<WebElement> sumOfProductsinCart = driver.findElements(quantityFieldInCartLocator);
        return sumOfProductsinCart;
    }

    public CartPage editProductQuantity(int quantity) {
        waitForShopTable();
        WebElement quantityField = driver.findElement(quantityFieldInCartLocator);
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        return new CartPage(driver);
    }

    private void waitForShopTable() {
        wait.until(ExpectedConditions.presenceOfElementLocated(shopTableLocator));
    }

    private void waitForSpinnerFinish() {
        WebElement spinner = driver.findElement(spinnerLocator);
        wait.until(ExpectedConditions.invisibilityOf(spinner));
    }

    public CartPage updateCart() {
        WebElement updateCartButton = driver.findElement(updateCartButtonLocator);
        updateCartButton.click();

        waitForSpinnerFinish();
        return new CartPage(driver);
    }

    public CartPage deleteProduct() {
        WebElement deleteButton = driver.findElement(deleteButtonLocator);
        deleteButton.click();
        waitForSpinnerFinish();
        return new CartPage(driver);
    }

    public PaymentPage checkOutCart() {
        WebElement goToPaymentPageButton = driver.findElement(checkOutCartButtonLocator);
        goToPaymentPageButton.click();
        return new PaymentPage(driver);
    }
}
