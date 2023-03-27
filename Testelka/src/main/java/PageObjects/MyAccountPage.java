package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyAccountPage extends BasePage {
    private final WebDriverWait wait;
    private final By myOrdersButtonLocator = By.xpath(".//li[contains(@class, 'orders')]");
    private final By myOrdersTableLocator = By.xpath(".//div[contains(@class, 'MyAccount-content')]");
    public MyAccountPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 7);
    }

    public MyAccountPage goToMyOrders() {
        WebElement myOrdersButton = driver.findElement(myOrdersButtonLocator);
        myOrdersButton.click();
        return new MyAccountPage(driver);
    }

    public boolean viewOrdersTable() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(myOrdersTableLocator));
        return true;
    }
}
