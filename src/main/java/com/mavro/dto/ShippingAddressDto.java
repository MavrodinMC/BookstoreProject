package com.mavro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShippingAddressDto {

    private String fullName;
    private String address;
    private String city;
    private String phoneNumber;
    private String state;
    private String zipCode;
    private boolean isDefault;
}
