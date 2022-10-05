package cart.project.cartApi.resource;

import cart.project.cartApi.model.Cart;
import cart.project.cartApi.model.Item;
import cart.project.cartApi.resource.dto.ItemDto;
import cart.project.cartApi.service.CartService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(value="/cart-api/cart")
@RestController
@RequestMapping("/cart-api/cart")
@RequiredArgsConstructor
public class CartResource {
    private final CartService cartService;

    @PostMapping
    public Item includeItem(@RequestBody ItemDto itemDto) {
        return cartService.includeItem(itemDto);
    }

    @GetMapping("/{id}")
    public Cart seeCart(@PathVariable Long id) {
        return cartService.seeCart(id);
    }

    @PatchMapping("/closeCart/{cartId}")
    public Cart closeCart(@PathVariable("cartId") Long cartId,
                          @RequestParam("payment") int payment) {
        return cartService.closeCart(cartId, payment);
    }

}
