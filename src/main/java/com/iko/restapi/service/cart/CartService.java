package com.iko.restapi.service.cart;

import com.iko.restapi.common.exception.BaseException;
import com.iko.restapi.common.exception.EntityNotFoundException;
import com.iko.restapi.common.exception.InvalidAccessException;
import com.iko.restapi.common.exception.InvalidParameterException;
import com.iko.restapi.domain.cart.CartItem;
import com.iko.restapi.domain.product.ProductOptionItem;
import com.iko.restapi.repository.cart.CartItemJpaRepository;
import com.iko.restapi.repository.product.ProductJpaRepository;
import com.iko.restapi.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemJpaRepository cartItemRepository;
    private final UserJpaRepository userRepository;
    private final ProductJpaRepository productRepository;
    private final EntityManager em;

    @Transactional
    public CartItem createCartItem(Long userId, Long productId, List<Long> optionIdList, Integer count) throws InvalidParameterException {
        log.info("create cart item (userId={}, productId={}, optionIdList={}, count={})", userId, productId, optionIdList, count);
        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다"));
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("제품을 찾을 수 없습니다"));
        // TODO: 이후 Product 리포지토리 계층에서 수정
        List<ProductOptionItem> productOptionItems = fetchAllProductOptionItems(optionIdList);
        // if not valid, throws Exception
        product.validateSelected(productOptionItems);
        // create
        var cartItem = CartItem.create(user, product, productOptionItems, count);
        return cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItem> fetchCartItems(Long userId) {
        log.info("fetch cart items (userId={})", userId);
        return cartItemRepository.findAllItemsByUserId(userId);
    }

    @Transactional
    public CartItem editCartItem(Long userId, Long cartItemId, List<Long> optionIdList, Integer count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카트 아이템입니다"));
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new InvalidAccessException("권한이 없습니다");
        }
        if (optionIdList != null) {
            List<ProductOptionItem> productOptionItems = fetchAllProductOptionItems(optionIdList);
            cartItem.changeOptions(productOptionItems);
        }
        if (count != null) {
            cartItem.setCount(count);
        }
        return cartItem;
    }
    
    @Transactional
    public void removeCartItem(Long itemId, Long userId) throws BaseException {
        log.info("remove cart item (user_id={})", userId);
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 장바구니 아이템입니다"));
        Long ownerId = cartItem.getUser().getId();
        if (!Objects.equals(ownerId, userId)) throw new InvalidAccessException("권한이 없습니다");
        removeCartItem(itemId);
    }

    @Transactional
    public void removeCartItem(Long itemId) throws BaseException {
        log.info("remove cart item (cart_item_id={})", itemId);
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void removeAllCartItems(Long userId) {
        log.info("remove all cart items (user_id={})", userId);
        cartItemRepository.deleteAllItemsByUserId(userId);
    }
    
    // TODO: Product Repository에 반영
    private List<ProductOptionItem> fetchAllProductOptionItems(List<Long> optionIdList) {
        if (optionIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return em.createQuery("select opt from ProductOptionItem opt" +
                        " where opt.id in :optionIdList", ProductOptionItem.class)
                .setParameter("optionIdList", optionIdList)
                .getResultList();
    }
}
