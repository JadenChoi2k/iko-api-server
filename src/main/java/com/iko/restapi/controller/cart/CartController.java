package com.iko.restapi.controller.cart;

import com.iko.restapi.common.config.SwaggerConfig;
import com.iko.restapi.common.response.CommonResponse;
import com.iko.restapi.common.utils.SessionUtils;
import com.iko.restapi.domain.cart.CartItem;
import com.iko.restapi.dto.cart.CartDto;
import com.iko.restapi.service.cart.CartService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = SwaggerConfig.CART_TAG, produces = "application/json")
@RequestMapping("/api/v1/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(summary = "카트에 아이템 추가", description = "카트에 아이템을 추가한다.")
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

    @Operation(summary = "카트 가져오기", description = "카트 아이템 목록을 가져온다")
    @GetMapping
    public CommonResponse<List<CartDto.Main>> retrieveCartItems(HttpServletRequest request) {
        return CommonResponse.success(
                cartService.fetchCartItems(SessionUtils.getUserId(request)).stream()
                        .map(CartDto.Main::of)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "카트 아이템 수정", description = "카트 아이템을 수정한다")
    @PatchMapping("/{cartItemId}")
    public CommonResponse<CartDto.Main> editCartItem(HttpServletRequest request,
                                                     @PathVariable Long cartItemId,
                                                     @RequestBody CartDto.EditRequest editRequest) {
        return CommonResponse.success(
                CartDto.Main.of(
                        cartService.editCartItem(
                                SessionUtils.getUserId(request), cartItemId,
                                editRequest.getOptionIdList(), editRequest.getCount())
                )
        );
    }

    @Operation(summary = "카트 아이템 삭제", description = "카트의 아이템 하나를 삭제한다")
    @DeleteMapping("/{cartItemId}")
    public CommonResponse<String> deleteCartItem(HttpServletRequest request, @PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId, SessionUtils.getUserId(request));
        return CommonResponse.ok();
    }

    @Operation(summary = "카트 비우기", description = "유저의 카트 아이템들을 모두 삭제한다")
    @DeleteMapping
    public CommonResponse<String> deleteCartItems(HttpServletRequest request) {
        cartService.removeAllCartItems(SessionUtils.getUserId(request));
        return CommonResponse.ok();
    }
}
