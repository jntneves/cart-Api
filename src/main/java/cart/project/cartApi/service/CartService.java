package cart.project.cartApi.service;

import cart.project.cartApi.model.Cart;
import cart.project.cartApi.model.Item;
import cart.project.cartApi.resource.dto.ItemDto;

public interface CartService {
    Item includeItem(ItemDto itemDto);
    Cart seeCart(Long id);
    Cart closeCart(Long id, int payment);
}
