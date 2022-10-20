package com.iko.restapi.repository.order;

import com.iko.restapi.domain.order.Order;

import java.util.List;

public interface OrderCustomRepository {
    Order createOrderByUserIdAndCartIdList(Long userId, List<Long> cartIdList);
}
