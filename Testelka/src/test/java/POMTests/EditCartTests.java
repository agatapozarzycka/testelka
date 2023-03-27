package POMTests;

import PageObjects.CartPage;
import PageObjects.MainPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EditCartTests extends BaseTest {

    @Test
    //użytkownik ma możliwość zmiany ilości wybranej wycieczki (pojedynczej pozycji) na stronie koszyka
    public void editProductQuantityInCartTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        mainPage.goToProductPage().addToCart().viewCart();

        CartPage cartPage = new CartPage(driver);
        String updatedProductQuantity = cartPage.editProductQuantity(4).updateCart().getProductQuantity();
        assertEquals("4", updatedProductQuantity, "Quantity of product is not what was expected.");
    }

    @Test
    //użytkownik ma możliwość usunięcia wycieczki na stronie koszyka (całej pozycji)
    public void deleteProductFromCart() {
        MainPage mainPage = new MainPage(driver);
        mainPage.demoNotice.close();
        mainPage.goToProductPage().addToCart().viewCart();

        CartPage cartPage = new CartPage(driver);
        boolean emptyShopCart = cartPage.deleteProduct().isProductInCart();
        assertFalse(emptyShopCart, "Product was not deleted");
    }
}
