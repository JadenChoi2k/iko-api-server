package com.iko.restapi.controller.cart;

import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.utils.SessionUtils;
import com.iko.restapi.domain.cart.CartItem;
import com.iko.restapi.dto.cart.CartDto;
import com.iko.restapi.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CommonResponse<CartDto.Main> createCartItem(HttpServletRequest request, @RequestBody CartDto.CreateRequest createRequest) {
        CartItem cartItem = cartService.createCartItem(
                SessionUtils.getUserId(request),
                createRequest.getProductId(),
                createRequest.getOptionIdList(),
                createRequest.getCount()
        );
        return CommonResponse.success(CartDto.Main.of(cartItem));
    }

    @GetMapping
    public CommonResponse<List<CartDto.Main>> retrieveCartItems(HttpServletRequest request) {
        return CommonResponse.success(
                cartService.fetchCartItems(SessionUtils.getUserId(request)).stream()
                        .map(CartDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{cartItemId}")
    public CommonResponse<String> deleteCartItem(HttpServletRequest request, @PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId, SessionUtils.getUserId(request));
        return CommonResponse.ok();
    }

    @DeleteMapping
    public CommonResponse<String> deleteCartItems(HttpServletRequest request) {
        cartService.removeAllCartItems(SessionUtils.getUserId(request));
        return CommonResponse.ok();
    }
}
