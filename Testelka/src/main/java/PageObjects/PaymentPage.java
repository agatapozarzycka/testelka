package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PaymentPage extends BasePage {
    private final WebDriverWait wait;
    private final By spinnerLocator = By.xpath(".//div[@class ='blockUI blockOverlay']");
    private final By cardNumberFrameLocator = By.xpath(".//div[@id = 'stripe-card-element']//iframe");
    private final By cardNumberFieldLocator = By.xpath(".//input[@name = 'cardnumber']");
    private final By cardExpireFrameLocator = By.xpath(".//div[@id = 'stripe-exp-element']//iframe");
    private final By cardExpireFieldLocator = By.xpath(".//input[@name = 'exp-date']");
    private final By cvcFrameLocator = By.xpath(".//div[@id = 'stripe-cvc-element']//iframe");
    private final By cvcFieldLocator = By.xpath(".//input[@name = 'cvc']");
    private final By buyAndPayButtonLocator = By.xpath(".//button[@id = 'place_order']");
    private final By userLogInButtonLocator = By.xpath(".//a[@class = 'showlogin']");
    private final By userNameFieldLocator = By.xpath(".//input[@id = 'username']");
    private final By passwordFieldLocator = By.xpath("//input[@id = 'password']");
    private final By logInButtonLocator = By.xpath(".//button[@name = 'login']");
    private final By termsAndConditionsCheckBoxLocator = By.xpath(".//input[@id = 'terms']");
    private final By firstNameFieldLocator = By.xpath(".//input[@id= 'billing_first_name']");
    private final By lastNameFieldLocator = By.xpath(".//input[@id= 'billing_last_name']");
    private final By openCountrySelectionLocator = By.xpath(".//span[@class='selection']");
    private final By selectCountryLocator = By.xpath(".//span[@title='Polska']");
    private final By address1FieldLocator = By.xpath(".//input[@id= 'billing_address_1']");
    private final By postcodeFieldLocator = By.xpath(".//input[@id= 'billing_postcode']");
    private final By billingCityFieldLocator = By.xpath(".//input[@id= 'billing_city']");
    private final By billingPhoneFieldLocator = By.xpath(".//input[@id= 'billing_phone']");
    private final By billingEmailFieldLocator = By.xpath(".//input[@id= 'billing_email']");
    private final By createAccountCheckboxLocator = By.xpath(".//input[@id ='createaccount']");
    private final By enterAccountPasswordFieldLocator = By.xpath(".//input[@id ='account_password']");

    public PaymentPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, 10);
    }

    public PaymentPage insertCardNumber(String cardNumber) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(cardNumberFrameLocator));
        driver.switchTo().frame(driver.findElement(cardNumberFrameLocator));
        WebElement cardNumberField = driver.findElement(cardNumberFieldLocator);
        cardNumberField.click();
        cardNumberField.clear();
        cardNumberField.sendKeys(cardNumber);
        driver.switchTo().defaultContent();

        return new PaymentPage(driver);
    }

    public PaymentPage insertExpireDate(String expireDate) {
        driver.switchTo().frame(driver.findElement(cardExpireFrameLocator));
        WebElement cardExpireField = driver.findElement(cardExpireFieldLocator);
        cardExpireField.click();
        cardExpireField.clear();
        cardExpireField.sendKeys(expireDate);
        driver.switchTo().defaultContent();
        return new PaymentPage(driver);
    }

    public PaymentPage insertCvc(String cvcNumber) {
        driver.switchTo().frame(driver.findElement(cvcFrameLocator));
        WebElement cvcField = driver.findElement(cvcFieldLocator);
        cvcField.click();
        cvcField.clear();
        cvcField.sendKeys(cvcNumber);
        driver.switchTo().defaultContent();
        return new PaymentPage(driver);
    }

    public PaymentPage acceptTermsAndConditions() {
        WebElement termsAndConditionsCheckBox = driver.findElement(termsAndConditionsCheckBoxLocator);
        termsAndConditionsCheckBox.click();
        return new PaymentPage(driver);
    }

    public ConfirmationPage order() {
        WebElement buyAndPayButton = driver.findElement(buyAndPayButtonLocator);
        buyAndPayButton.click();
        waitForSpinnerFinish();
        return new ConfirmationPage(driver);
    }

    public PaymentPage showLogin() {
        WebElement userLogInButton = driver.findElement(userLogInButtonLocator);
        userLogInButton.click();
        return new PaymentPage(driver);
    }

    public PaymentPage enterUserName(String userName) {
        WebElement userNameField = driver.findElement(userNameFieldLocator);
        wait.until(ExpectedConditions.visibilityOf(userNameField));
        userNameField.sendKeys(userName);
        return new PaymentPage(driver);
    }

    public PaymentPage enterPassword(String password) {
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        passwordField.sendKeys(password);
        return new PaymentPage(driver);
    }

    public void pressEnter() {

        driver.findElement(By.xpath(".//button[@name = 'login']")).sendKeys(Keys.chord(Keys.ENTER));
    }

    private void waitForSpinnerFinish() {
        WebElement spinner = driver.findElement(spinnerLocator);
        wait.until(ExpectedConditions.invisibilityOf(spinner));
    }

    public PaymentPage enterFirstName(String firstName) {
        driver.findElement(firstNameFieldLocator).clear();
        driver.findElement(firstNameFieldLocator).sendKeys(firstName);
        return new PaymentPage(driver);
    }

    public PaymentPage enterLastName(String lastName) {
        driver.findElement(lastNameFieldLocator).clear();
        driver.findElement(lastNameFieldLocator).sendKeys(lastName);
        return new PaymentPage(driver);
    }

    public PaymentPage selectCountry() {
        driver.findElement(openCountrySelectionLocator).click();
        driver.findElement(selectCountryLocator).click();
        return new PaymentPage(driver);
    }

    public PaymentPage enterAddress1(String address) {
        driver.findElement(address1FieldLocator).clear();
        driver.findElement(address1FieldLocator).sendKeys(address);
        return new PaymentPage(driver);
    }

    public PaymentPage enterPostcode(String postcode) {
        driver.findElement(postcodeFieldLocator).clear();
        driver.findElement(postcodeFieldLocator).sendKeys(postcode);
        return new PaymentPage(driver);
    }

    public PaymentPage enterCity(String city) {
        driver.findElement(billingCityFieldLocator).clear();
        driver.findElement(billingCityFieldLocator).sendKeys(city);
        return new PaymentPage(driver);
    }

    public PaymentPage enterPhone(String phone) {
        driver.findElement(billingPhoneFieldLocator).clear();
        driver.findElement(billingPhoneFieldLocator).sendKeys(phone);
        return new PaymentPage(driver);
    }

    public PaymentPage enterEmail(String email) {
        driver.findElement(billingEmailFieldLocator).clear();
        driver.findElement(billingEmailFieldLocator).sendKeys(email);
        return new PaymentPage(driver);
    }

    public PaymentPage createUserAccount(String password) {
        driver.findElement(createAccountCheckboxLocator).click();
        WebElement accountPasswordField = driver.findElement(enterAccountPasswordFieldLocator);
        wait.until(ExpectedConditions.visibilityOf(accountPasswordField));
        accountPasswordField.sendKeys(password);
        return new PaymentPage(driver);
    }
}
