package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Calendar;

public class ConfirmationPage extends BasePage {
    private final WebDriverWait wait;
    private final By orderReceivedMessageLocator = By.xpath(".//h1[@class = 'entry-title']");
    private final By orderNumberLocator = By.xpath(".//li[contains(@class, '__order')]");
    private final By orderDateLocator = By.xpath(".//li[contains(@class, '__date')]/strong");
    private final By orderPriceLocator = By.xpath(".//li//span[contains(@class, 'Price-amount')]/bdi");
    private final By orderPaymentMethodLocator = By.xpath(".//li[contains(@class, 'payment-method')]/strong");
    private final By orderProductNameLocator = By.xpath("//td[contains(@class, 'product-name')]/a");
    private final By orderProductQuantityLocator = By.xpath(".//strong[contains(@class, 'product-quantity')]");
    public HeaderPage header;


    public ConfirmationPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 7);
        header = new HeaderPage(driver);
    }

    public String getOrderReceivedMessage() {
        String orderReceivedMessage = driver.findElement(orderReceivedMessageLocator).getText();
        return orderReceivedMessage;
    }

    public String getOrderNumber() {
        String orderNumber = driver.findElement(orderNumberLocator).getText();
        return orderNumber;
    }

    public String getOrderDate() {
        String orderDate = driver.findElement(orderDateLocator).getText();
        return orderDate;
    }

    public String getCurrentDate() {
        Calendar date = Calendar.getInstance();
        String fullDate = date.get(Calendar.DAY_OF_MONTH) + " " +
                getPolishMonth(date.get(Calendar.MONTH)) + ", " + date.get(Calendar.YEAR);
        return fullDate;
    }

    private String getPolishMonth(int numberOfMonth) {
        String[] monthNames = {"stycznia", "lutego", "marca", "kwietnia", "maja", "czerwca",
                "lipca", "sierpnia", "września", "października", "listopada", "grudnia"};
        return monthNames[numberOfMonth];
    }

    public String getOrderPrice() {
        String orderPrice = driver.findElement(orderPriceLocator).getText();
        return orderPrice;
    }

    public String getPaymentMethod() {
        String paymentMethod = driver.findElement(orderPaymentMethodLocator).getText();
        return paymentMethod;

    }

    public String getProductName() {
        String productName = driver.findElement(orderProductNameLocator).getText();
        return productName;
    }

    public String getProductQuantity() {
        String productQuantity = driver.findElement(orderProductQuantityLocator).getText();
        return productQuantity;

    }
}
