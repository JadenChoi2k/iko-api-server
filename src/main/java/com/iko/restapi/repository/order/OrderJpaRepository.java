package com.iko.restapi.repository.order;

import com.iko.restapi.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
}
