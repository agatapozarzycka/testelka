package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HeaderPage extends BasePage {
    private final WebDriverWait wait;
    private final By totalPriceLocator = By.xpath(".//a[@class='cart-contents']");
    private final By menuItemMyAccoutLocator = By.xpath(".//li[@id= 'menu-item-201']");

    protected HeaderPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 7);
    }

    public CartPage viewCart() {
        driver.findElement(totalPriceLocator).click();
        return new CartPage(driver);
    }

    public MyAccountPage goToMyAccount() {
        driver.findElement(menuItemMyAccoutLocator).click();

        wait.until(ExpectedConditions.titleContains("Moje konto – FakeStore"));
        return new MyAccountPage(driver);
    }
}
