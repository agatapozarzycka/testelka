package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoryPage extends BasePage {
    public HeaderPage header;
    private final WebDriverWait wait;

    private final By buttonAddToCartLocator = By.xpath(".//a[contains(text(), 'Dodaj do koszyka')]");

    public CategoryPage(WebDriver driver) {
        super(driver);
        header = new HeaderPage(driver);
        wait = new WebDriverWait(driver, 5);
    }

    public CategoryPage addToCart(int quantity) throws InterruptedException {
        for (int i = 0; i < quantity; i++) {
            driver.findElements(buttonAddToCartLocator).get(i).click();
            Thread.sleep(2000);
        }
        return new CategoryPage(driver);
    }
}
