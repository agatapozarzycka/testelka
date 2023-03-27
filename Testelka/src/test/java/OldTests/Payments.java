package OldTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class Payments {
    WebDriver driver;
    WebDriverWait wait;
    String firstNameInput = "Test";
    String lastNameInput = "Testowy";
    String billingAddress1Input = "Testowa";
    String billingPostcodeInput = "01-123";
    String billingCityInput = "Warszawa";
    String billingPhoneInput = "+48123456789";
    String billingEmailInput = "test.testowy@test.com";
    By myAccountMenuItem = By.xpath(".//li[@id ='menu-item-201']");
    String testPassword = "TestPassword";

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
        By goToPaymentButton = By.xpath(".//a[@class = 'checkout-button button alt wc-forward']");
        driver.findElement(goToPaymentButton).click();
    }

    @AfterEach
    public void driverQuit() {
        driver.quit();
    }

    @Test //użytkownik jest informowany o błędach w formularzu na stronie płatności poprzez odpowiednie komunikaty
    public void errorMessagesOnPaymentPageCreditCardNumber() throws InterruptedException {

        By buyAndPayButton = By.xpath(".//button[@id = 'place_order']");
        Thread.sleep(3000);
        driver.findElement(buyAndPayButton).click();
        Thread.sleep(3000);
        String actualCreditCardErrorMessage = driver.findElement(By.xpath(".//ul[@class = 'woocommerce_error woocommerce-error wc-stripe-error']")).getText();
        String expectedCreditCardErrorMessage = "Numer karty jest niekompletny.";
        Assertions.assertEquals(expectedCreditCardErrorMessage, actualCreditCardErrorMessage, "Error message is not displayed or has not expected text.");
    }

    @Test  //użytkownik jest informowany o błędach w formularzu na stronie płatności poprzez odpowiednie komunikaty
    public void errorMessagesOnPaymentPageDataFormular() throws InterruptedException {
        Thread.sleep(3000);
        completeCreditCardDetails();

        By buyAndPayButton = By.xpath(".//button[@id = 'place_order']");
        driver.findElement(buyAndPayButton).click();

        WebElement spinner = driver.findElement(By.xpath(".//div[@class ='blockUI blockOverlay']"));
        wait.until(ExpectedConditions.invisibilityOf(spinner));

        String billingFistName = driver.findElement(By.xpath(".//li[@data-id = 'billing_first_name']")).getText();
        String billingLastName = driver.findElement(By.xpath(".//li[@data-id = 'billing_last_name']")).getText();
        String billingAddress1 = driver.findElement(By.xpath(".//li[@data-id = 'billing_address_1']")).getText();
        String billingPostcode = driver.findElement(By.xpath(".//li[@data-id = 'billing_postcode']")).getText();
        String billingCity = driver.findElement(By.xpath(".//li[@data-id = 'billing_city']")).getText();
        String billingPhone = driver.findElement(By.xpath(".//li[@data-id = 'billing_phone']")).getText();
        String billingEmail = driver.findElement(By.xpath(".//li[@data-id = 'billing_email']")).getText();

        assertAll("testAssertions",
                () -> assertEquals("Imię płatnika jest wymaganym polem.", billingFistName),
                () -> assertEquals("Nazwisko płatnika jest wymaganym polem.", billingLastName),
                () -> assertEquals("Ulica płatnika jest wymaganym polem.", billingAddress1),
                () -> assertEquals("Kod pocztowy płatnika nie jest prawidłowym kodem pocztowym.", billingPostcode),
                () -> assertEquals("Miasto płatnika jest wymaganym polem.", billingCity),
                () -> assertEquals("Telefon płatnika jest wymaganym polem.", billingPhone),
                () -> assertEquals("Adres email płatnika jest wymaganym polem.", billingEmail)
        );
    }

    @Test //użytkownik ma możliwość zalogowania się na stronie płatności i dokonać płatności jako zalogowany użytkownik,
    public void logedInUserPaysForProduct() {
        By userLogInButton = By.xpath(".//a[@class = 'showlogin']");
        driver.findElement(userLogInButton).click();

        By userNameField = By.xpath(".//input[@id = 'username']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(userNameField));
        driver.findElement(userNameField).sendKeys("test-0022");

        By passwordField = By.xpath("//input[@id = 'password']");
        driver.findElement(passwordField).sendKeys("silneHaslo");
        driver.findElement(By.xpath(".//button[@name = 'login']")).sendKeys(Keys.chord(Keys.ENTER));

        completeForm();
        completeCreditCardDetails();
        acceptTermsAndClickBuyAndPayButton();
        assertionForSuccessfulOrderTitle();
    }

    @Test //użytkownik ma możliwość założenia konta na stronie płatności i dokonać jednocześnie płatności

    public void userCreatesAccountOnPaymentPage() {
        completeForm();
        createAnAccountOnPaymentPage();
        completeCreditCardDetails();
        acceptTermsAndClickBuyAndPayButton();

        driver.findElement(myAccountMenuItem).click();
        wait.until(ExpectedConditions.titleContains("Moje konto – FakeStore"));

        String actualMessage = driver.findElement(By.xpath(".//div[@class ='woocommerce-MyAccount-content']/p")).getText();
        Assertions.assertTrue(actualMessage.contains(firstNameInput + " " + lastNameInput),
                "My Account page does not contain correct name. Expected name: " + firstNameInput + " " + lastNameInput + " was not found in a string: " + actualMessage);

        deleteExistingAccount();
    }

    @Test // użytkownik ma możliwość dokonania zakupu bez zakładania konta
    public void userBuysProductWithoutCreatingAnAccount() {
        completeForm();
        completeCreditCardDetails();
        acceptTermsAndClickBuyAndPayButton();
        assertionForSuccessfulOrderTitle();
    }

    @Test //użytkownik, który posiada konto może zobaczyć swoje zamówienia na swoim koncie
    public void userCanViewTheOrderFromTheAccount() {
        completeForm();
        createAnAccountOnPaymentPage();
        completeCreditCardDetails();
        acceptTermsAndClickBuyAndPayButton();

        driver.findElement(myAccountMenuItem).click();
        wait.until(ExpectedConditions.titleContains("Moje konto – FakeStore"));

        driver.findElement(By.xpath(".//li[contains(@class, 'orders')]")).click();

        By viewButton = By.xpath(".//a[contains(@class, 'button view')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(viewButton));
        driver.findElement(viewButton).click();

        String actualOrderDetailsTitle = driver.findElement(By.xpath(".//h2[contains(@class, 'title')]")).getText();
        String expectedOrderDetailsTitle = "Szczegóły zamówienia";
        WebElement productName = driver.findElement(By.xpath(".//td[contains(@class, 'product-name')]"));
        WebElement priceAmount = driver.findElement(By.xpath(".//tr//span[contains(@class, 'amount')]/bdi"));

        assertAll("testAssertions",
                () -> assertEquals(expectedOrderDetailsTitle, actualOrderDetailsTitle),
                () -> assertTrue(productName.isDisplayed()),
                () -> assertTrue(priceAmount.isDisplayed())
        );

        driver.findElement(myAccountMenuItem).click();
        wait.until(ExpectedConditions.titleContains("Moje konto – FakeStore"));
        deleteExistingAccount();

    }

    @Test
//użytkownik po dokonaniu zamówienia może zobaczyć podsumowanie, które zawiera numer zamówienia, poprawną datę, kwotę, metodę płatności, nazwę i ilość zakupionych produktów.
    public void userCanViewOrderDetailsAfterCompletedTheOrder() {
        completeForm();
        completeCreditCardDetails();
        acceptTermsAndClickBuyAndPayButton();
        assertionForSuccessfulOrderTitle();

        WebElement orderNumber = driver.findElement(By.xpath(".//li[contains(@class, '__order')]"));
        WebElement orderDate = driver.findElement(By.xpath(".//li[contains(@class, '__date')]"));
        WebElement orderTotalAmount = driver.findElement(By.xpath(".//li//span[contains(@class, 'Price-amount')]/bdi"));
        WebElement orderPaymentMethod = driver.findElement(By.xpath(".//li[contains(@class, 'payment-method')]"));
        WebElement orderProductName = driver.findElement(By.xpath(".//td[contains(@class, 'product-name')]"));
        WebElement orderProductQuantity = driver.findElement(By.xpath(".//strong[contains(@class, 'product-quantity')]"));

        assertAll("productDetailsTest",
                () -> assertTrue(orderNumber.isDisplayed(), "Order number is not displayed"),
                () -> assertTrue(orderDate.isDisplayed(), "Order date is not displayed"),
                () -> assertTrue(orderTotalAmount.isDisplayed(), "Order total amount is not displayed"),
                () -> assertTrue(orderPaymentMethod.isDisplayed(), "Order payment method is not displayed"),
                () -> assertTrue(orderProductName.isDisplayed(), "Order product name is not displayed"),
                () -> assertTrue(orderProductQuantity.isDisplayed(), "Order product quantity is not displayed")
        );
    }


    void completeForm() {


        By firstNameField = By.xpath(".//input[@id= 'billing_first_name']");
        By lastNameField = By.xpath(".//input[@id= 'billing_last_name']");
        By address1Field = By.xpath(".//input[@id= 'billing_address_1']");
        By postcodeField = By.xpath(".//input[@id= 'billing_postcode']");
        By billingCityField = By.xpath(".//input[@id= 'billing_city']");
        By billingPhoneField = By.xpath(".//input[@id= 'billing_phone']");
        By billingEmailField = By.xpath(".//input[@id= 'billing_email']");
        driver.findElement(firstNameField).clear();
        driver.findElement(firstNameField).sendKeys(firstNameInput);
        driver.findElement(lastNameField).clear();
        driver.findElement(lastNameField).sendKeys(lastNameInput);
        driver.findElement(address1Field).clear();
        driver.findElement(address1Field).sendKeys(billingAddress1Input);
        driver.findElement(postcodeField).clear();
        driver.findElement(postcodeField).sendKeys(billingPostcodeInput);
        driver.findElement(billingCityField).clear();
        driver.findElement(billingCityField).sendKeys(billingCityInput);
        driver.findElement(billingPhoneField).clear();
        driver.findElement(billingPhoneField).sendKeys(billingPhoneInput);
        driver.findElement(billingEmailField).clear();
        driver.findElement(billingEmailField).sendKeys(billingEmailInput);
    }

    void completeCreditCardDetails() {
        driver.switchTo().frame(driver.findElement(By.xpath(".//div[@id = 'stripe-card-element']//iframe")));
        By cardNumberField = By.xpath(".//input[@name = 'cardnumber']");
        driver.findElement(cardNumberField).click();
        driver.findElement(cardNumberField).clear();
        driver.findElement(cardNumberField).sendKeys("4242424242424242");
        driver.switchTo().defaultContent();

        driver.switchTo().frame(driver.findElement(By.xpath(".//div[@id = 'stripe-exp-element']//iframe")));
        By cardExpireField = By.xpath(".//input[@name = 'exp-date']");
        driver.findElement(cardExpireField).click();
        driver.findElement(cardExpireField).clear();
        driver.findElement(cardExpireField).sendKeys("1225");
        driver.switchTo().defaultContent();

        driver.switchTo().frame(driver.findElement(By.xpath(".//div[@id = 'stripe-cvc-element']//iframe")));
        By cvcField = By.xpath(".//input[@name = 'cvc']");
        driver.findElement(cvcField).click();
        driver.findElement(cvcField).clear();
        driver.findElement(cvcField).sendKeys("122");
        driver.switchTo().defaultContent();
    }

    void acceptTermsAndClickBuyAndPayButton() {
        By termsCheckBox = By.xpath(".//input[@id = 'terms']");
        driver.findElement(termsCheckBox).click();

        By buyAndPayButton = By.xpath(".//button[@id = 'place_order']");
        driver.findElement(buyAndPayButton).click();
        WebElement spinner = driver.findElement(By.xpath(".//div[@class ='blockUI blockOverlay']"));
        wait.until(invisibilityOf(spinner));
    }

    void deleteExistingAccount() {
        driver.findElement(By.xpath(".//a[@class = 'delete-me']")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        driver.findElement(myAccountMenuItem).click();
        wait.until(ExpectedConditions.titleContains("Moje konto – FakeStore"));
        driver.findElement(By.xpath("//input[@id = 'username']")).sendKeys(billingEmailInput);
        driver.findElement(By.xpath("//input[@id = 'password']")).sendKeys(testPassword);
        driver.findElement(By.xpath(".//button[@name = 'login']")).click();

        String actualErrorMessage = driver.findElement(By.xpath(".//ul[@class = 'woocommerce-error']/li")).getText();
        String expectedErrorMessage = "Nieznany adres e-mail. Proszę sprawdzić ponownie lub wypróbować swoją nazwę użytkownika.";
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "Account was not deleted");
    }

    void createAnAccountOnPaymentPage() {
        By createAccountCheckbox = By.xpath(".//input[@id ='createaccount']");
        driver.findElement(createAccountCheckbox).click();

        By enterAccountPasswordField = By.xpath(".//input[@id ='account_password']");
        wait.until(visibilityOfElementLocated(enterAccountPasswordField));
        driver.findElement(enterAccountPasswordField).sendKeys(testPassword);
    }

    void assertionForSuccessfulOrderTitle() {
        String orderReceivedMessage = driver.findElement(By.xpath(".//header[@class = 'entry-header']")).getText();
        Assertions.assertEquals("Zamówienie otrzymane", orderReceivedMessage);
    }
}


