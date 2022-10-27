package com.iko.restapi.repository.user;

import com.iko.restapi.domain.user.User;
import com.iko.restapi.domain.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, Long> {
    @Query("select addr from UserAddress addr" +
            " where addr.favorite = true and addr.user = :user")
    List<UserAddress> findAllByUser(@Param("user") User user);
}
