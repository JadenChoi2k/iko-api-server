package com.iko.restapi.domain.user;

import com.iko.restapi.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 63)
    private String recipient; // 수령인
    private String address; // 주소
    @Column(length = 15)
    private String zipCode; // 우편번호
    private Boolean favorite;

    public UserAddress(User user, String recipient, String address, String zipCode, Boolean favorite) {
        this.user = user;
        this.recipient = recipient;
        this.address = address;
        this.zipCode = zipCode;
        this.favorite = favorite;
    }
}
