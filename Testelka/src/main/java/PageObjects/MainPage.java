package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage extends BasePage {
    public DemoFooterPage demoNotice;
    private final WebDriverWait wait;
    private final By productLocator = By.xpath(".//li[contains(@class, 'post-61')]");
    private final By productCategoryWindsurfingLocator = By.xpath(".//li[@class = 'product-category product first']");
    private final By productCategoryYogaPilatesLocator = By.xpath(".//li[@class = 'product-category product last']");

    public MainPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 7);
        demoNotice = new DemoFooterPage(driver);
    }

    public ProductPage goToProductPage() {
        driver.findElement(productLocator).click();
        return new ProductPage(driver);
    }

    public CategoryPage goToCategoryPageWindsurfing() {
        driver.findElement(productCategoryWindsurfingLocator).click();
        return new CategoryPage(driver);
    }

    public CategoryPage goToCategoryPageYogaPilates() {
        driver.findElement(productCategoryYogaPilatesLocator).click();
        return new CategoryPage(driver);
    }
}
