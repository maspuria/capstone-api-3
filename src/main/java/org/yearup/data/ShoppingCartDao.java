package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    // boolean holdProductsInCart(int userId, int productId);
    void addProduct(int userId, int productId);
    void updateQuantityOfProduct(int userId, int productId, int quantity);
    void clearShoppingCart(int userId);
}
