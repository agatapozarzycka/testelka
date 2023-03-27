package POMTests;

import PageObjects.ConfirmationPage;
import PageObjects.MainPage;
import PageObjects.PaymentPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTests extends BaseTest {
    String cardNumber = "4242424242424242";
    String expireDate = "1225";
    String cvc = "123";
    String userName = "test-0022";
    String password = "silneHaslo";
    String firstName = "Test";
    String lastName = "Testowy";
    String address = "Testowa";
    String postcode = "01-123";
    String city = "Warszawa";
    String phone = "+48123456789";
    String generatedEmail = RandomStringUtils.randomAlphabetic(5);
    String email = generatedEmail + "@test.com";

    @Test
    //użytkownik jest informowany o błędach w formularzu na stronie płatności poprzez odpowiednie komunikaty
    public void errorMessagesOnPaymentPageDataForm() throws InterruptedException {
        goToPaymentPage();
        enterCardDetails();
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.order();

        String billingFistNameAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_first_name']")).getText();
        String billingLastNameAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_last_name']")).getText();
        String billingAddress1Alert = driver.findElement(By.xpath(".//li[@data-id = 'billing_address_1']")).getText();
        String billingPostcodeAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_postcode']")).getText();
        String billingCityAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_city']")).getText();
        String billingPhoneAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_phone']")).getText();
        String billingEmailAlert = driver.findElement(By.xpath(".//li[@data-id = 'billing_email']")).getText();
        String termsAndConditionsAlert = driver.findElement(By.xpath(".//ul[@class='woocommerce-error']/li[8]")).getText();

        assertAll("errorMessagesTest",
                () -> assertEquals("Imię płatnika jest wymaganym polem.", billingFistNameAlert),
                () -> assertEquals("Nazwisko płatnika jest wymaganym polem.", billingLastNameAlert),
                () -> assertEquals("Ulica płatnika jest wymaganym polem.", billingAddress1Alert),
                () -> assertEquals("Kod pocztowy płatnika nie jest prawidłowym kodem pocztowym.", billingPostcodeAlert),
                () -> assertEquals("Miasto płatnika jest wymaganym polem.", billingCityAlert),
                () -> assertEquals("Telefon płatnika jest wymaganym polem.", billingPhoneAlert),
                () -> assertEquals("Adres email płatnika jest wymaganym polem.", billingEmailAlert),
                () -> assertEquals("Proszę przeczytać i zaakceptować regulamin sklepu aby móc sfinalizować zamówienie.", termsAndConditionsAlert)
        );
    }

    @Test
    //użytkownik ma możliwość zalogowania się na stronie płatności i dokonać płatności jako zalogowany użytkownik,
    public void logedInUserPaysForProduct() throws InterruptedException {
        goToPaymentPage();
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.showLogin().enterUserName(userName).enterPassword(password).pressEnter();
        enterCardDetails();
        paymentPage.acceptTermsAndConditions().order();
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);

        String isOrderReceived = confirmationPage.getOrderReceivedMessage();
        Assertions.assertEquals("Zamówienie otrzymane", isOrderReceived);
    }

    @Test
    // użytkownik ma możliwość dokonania zakupu bez zakładania konta
    public void userBuysProductWithoutCreatingAnAccount() throws InterruptedException {
        goToPaymentPage();
        PaymentPage paymentPage = new PaymentPage(driver);
        enterPersonalDataIntoForm();
        enterCardDetails();
        paymentPage.acceptTermsAndConditions().order();
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);

        String isOrderReceived = confirmationPage.getOrderReceivedMessage();
        Assertions.assertEquals("Zamówienie otrzymane", isOrderReceived);
    }

    @Test
    //użytkownik ma możliwość założenia konta na stronie płatności i dokonać jednocześnie płatności

    public void userCreatesAccountOnPaymentPage() throws InterruptedException {
        goToPaymentPage();
        PaymentPage paymentPage = new PaymentPage(driver);
        enterPersonalDataIntoForm();
        paymentPage.createUserAccount(password);
        enterCardDetails();
        paymentPage.acceptTermsAndConditions().order().header.goToMyAccount();

        String actualMessage = driver.findElement(By.xpath(".//div[@class ='woocommerce-MyAccount-content']/p")).getText();
        Assertions.assertTrue(actualMessage.contains(firstName + " " + lastName),
                "My Account page does not contain correct name. Expected name: " + firstName + " " + lastName + " was not found in a string: " + actualMessage);
    }

    @Test
    //użytkownik, który posiada konto może zobaczyć swoje zamówienia na swoim koncie
    public void userCanViewTheOrderFromTheAccount() throws InterruptedException {
        goToPaymentPage();
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.showLogin().enterUserName(userName).enterPassword(password).pressEnter();
        enterCardDetails();
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);

        boolean isMyAccountContentTable = confirmationPage.header.goToMyAccount().goToMyOrders().viewOrdersTable();
        assertTrue(isMyAccountContentTable, "Table with orders is not visible.");
    }

    @Test
    //użytkownik po dokonaniu zamówienia może zobaczyć podsumowanie, które zawiera numer zamówienia, poprawną datę, kwotę, metodę płatności, nazwę i ilość zakupionych produktów.
    public void userCanViewOrderDetailsAfterCompletedTheOrder() throws InterruptedException {
        goToPaymentPage();
        PaymentPage paymentPage = new PaymentPage(driver);
        enterPersonalDataIntoForm();
        enterCardDetails();
        paymentPage.acceptTermsAndConditions().order();

        ConfirmationPage confirmationPage = new ConfirmationPage(driver);
        String orderNumber = confirmationPage.getOrderNumber();
        String orderDate = confirmationPage.getOrderDate();
        String currentDate = confirmationPage.getCurrentDate();
        String orderPrice = confirmationPage.getOrderPrice();
        String expectedOrderPrice = "4 299,00 zł";
        String paymentMethod = confirmationPage.getPaymentMethod();
        String expectedPaymentMethod = "Karta debetowa/kredytowa (Stripe)";
        String productName = confirmationPage.getProductName();
        String expectedProductName = "Wakacje z yogą w Kraju Kwitnącej Wiśni";
        String productQuantity = confirmationPage.getProductQuantity();
        String expectedProductQuantity = "× 1";

        assertAll("checkOrerDetails",
                () -> assertFalse(orderNumber.isEmpty(), "Order number is not displayed"),
                () -> assertEquals(currentDate, orderDate, "Date is not correct"),
                () -> assertEquals(expectedOrderPrice, orderPrice, "Price is not correct"),
                () -> assertEquals(expectedProductName, productName, "Product name is not correct"),
                () -> assertEquals(expectedPaymentMethod, paymentMethod, "Payment method is not correct"),
                () -> assertEquals(expectedProductQuantity, productQuantity, "product quantity is not correct")
        );
    }

    private void goToPaymentPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        mainPage.goToProductPage().addToCart().viewCart().checkOutCart();
    }

    private void enterPersonalDataIntoForm() {
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.enterFirstName(firstName)
                .enterLastName(lastName)
                .selectCountry()
                .enterAddress1(address)
                .enterPostcode(postcode)
                .enterCity(city)
                .enterPhone(phone)
                .enterEmail(email);
    }

    private void enterCardDetails() throws InterruptedException {
        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.insertCardNumber(cardNumber)
                .insertExpireDate(expireDate)
                .insertCvc(cvc);
    }
}
