package com.iko.restapi.repository.order;

import com.iko.restapi.domain.order.Order;
import com.iko.restapi.domain.user.User;

import java.util.List;

public interface OrderCustomRepository {
    Order createOrderByUserIdAndCartIdList(Long userId, List<Long> cartIdList);

    Order createOrderByUserAndCartIdList(User user, List<Long> cartIdList);
}
