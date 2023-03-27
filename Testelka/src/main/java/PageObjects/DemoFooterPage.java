package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DemoFooterPage extends BasePage {
    By demoNoticeLocator = By.xpath(".//a[@class = 'woocommerce-store-notice__dismiss-link']");

    protected DemoFooterPage(WebDriver driver) {
        super(driver);
    }

    public void close() {
        driver.findElement(demoNoticeLocator).click();
    }

}
