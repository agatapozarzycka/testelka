package POMTests;

import PageObjects.MainPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddToCartTests extends BaseTest {

    @Test
    public void addToCartFromProductPageTest() {
        //użytkownik ma możliwość dodania wybranej wycieczki do koszyka ze strony tej wycieczki
        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        boolean isProductInCart = mainPage.goToProductPage().addToCart().viewCart().isProductInCart();
        assertTrue(isProductInCart,
                "Product was not added to cart");
    }

    @Test
    public void changeProductQuantityOnProductPage() {
        //użytkownik ma możliwość dodania co najmniej 10 wycieczek do koszyka (w sumie i w dowolnej kombinacji)
        //użytkownik ma możliwość wybrania ilości wycieczek, które chce zakupić, na stronie produktu (np. dla zamówienia dla kilku osób)

        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        String productQuantity = mainPage.goToProductPage().changeProductQuantity(10).addToCart().viewCart().getProductQuantity();
        assertEquals("10", productQuantity, "Quantity of product is not what was expected.");
    }

    @Test
    public void addTenDifferentProductsToCart() throws InterruptedException {
        //użytkownik ma możliwość dodania 10 różnych wycieczek do koszyka
        //użytkownik ma możliwość dodania wybranej wycieczki do koszyka ze strony kategorii,

        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        mainPage.goToCategoryPageWindsurfing().addToCart(5);
        driver.navigate().back();
        mainPage = new MainPage(driver);
        List<WebElement> sumOfProductsInCart = mainPage.goToCategoryPageYogaPilates().addToCart(5).header.viewCart().getProductsQuantity();
        assertEquals(10, sumOfProductsInCart.size());
    }
}
