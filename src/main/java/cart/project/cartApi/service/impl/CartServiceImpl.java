package cart.project.cartApi.service.impl;

import cart.project.cartApi.enumeration.Payment;
import cart.project.cartApi.model.Cart;
import cart.project.cartApi.model.Item;
import cart.project.cartApi.model.Restaurant;
import cart.project.cartApi.repository.CartRepository;
import cart.project.cartApi.repository.ItemRepository;
import cart.project.cartApi.repository.ProductRepository;
import cart.project.cartApi.resource.dto.ItemDto;
import cart.project.cartApi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item includeItem(ItemDto itemDto) {
        Cart cart = seeCart(itemDto.getIdCart());

        if (cart.isClosed()) {
            throw new RuntimeException("Cart is closed!");
        }

        Item itemInclude = Item.builder()
                .quantity(itemDto.getQuantity())
                .cart(cart)
                .product(productRepository.findById(itemDto.getProductId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Product not found!");
                        }
                ))
                .build();

        List<Item> cartItems =  cart.getItems();
        if(cartItems.isEmpty()) {
            cartItems.add(itemInclude);
        } else {
            Restaurant restaurant =  cartItems.get(0).getProduct().getRestaurant();
            Restaurant restaurantInclude = itemInclude.getProduct().getRestaurant();
            if (restaurant.equals(restaurantInclude)) {
                cartItems.add(itemInclude);
            } else {
                throw new RuntimeException("Cannot add products from different restaurants!");
            }
        }

        List<Double> itemsValue = new ArrayList<>();
        for (Item cartItem: cartItems) {
            double totalValue = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            itemsValue.add(totalValue);
        }

        double totalCartValue = itemsValue.stream()
                .mapToDouble(totalItemsValue -> totalItemsValue)
                .sum();

        cart.setTotalValue(totalCartValue);
        cartRepository.save(cart);
        return itemInclude;
    }

    @Override
    public Cart seeCart(Long id) {
        return cartRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Cart not found");
                }
        );
    }

    @Override
    public Cart closeCart(Long id, int numpayment) {
        Cart cart = seeCart(id);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Payment payment =
                numpayment == 0 ? Payment.CASH : Payment.CARD;

        cart.setPayment(payment);
        cart.setClosed(true);
        return cartRepository.save(cart);
    }
}
