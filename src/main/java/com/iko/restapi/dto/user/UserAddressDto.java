package com.iko.restapi.dto.user;

import com.iko.restapi.domain.user.UserAddress;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserAddressDto {

    @Getter
    public static class CreateRequest {
        @NotEmpty
        private final String recipient;
        @NotEmpty
        private final String address;
        @NotEmpty
        private final String zipCode;
        @NotNull
        private final Boolean favorite;

        public CreateRequest(String recipient, String address, String zipCode, Boolean favorite) {
            this.recipient = recipient;
            this.address = address;
            this.zipCode = zipCode;
            this.favorite = favorite;
        }
    }

    @Data
    public static class Main {
        private Long addressId;
        private String recipient; // 수령인
        private String address; // 주소
        private String zipCode; // 우편번호

        public Main(Long addressId, String recipient, String address, String zipCode) {
            this.addressId = addressId;
            this.recipient = recipient;
            this.address = address;
            this.zipCode = zipCode;
        }

        public static Main of(UserAddress address) {
            return new Main(
                    address.getId(),
                    address.getRecipient(),
                    address.getAddress(),
                    address.getZipCode()
            );
        }
    }
}
