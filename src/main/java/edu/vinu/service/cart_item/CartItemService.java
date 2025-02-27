package edu.vinu.service.cart_item;

import edu.vinu.model.CartItem;

public interface CartItemService {
    void addItemToCart(Long cartId,Long productId,int quantity);
    void updateItemToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);

    CartItem getCartItem(Long cartId, Long productId);
}
