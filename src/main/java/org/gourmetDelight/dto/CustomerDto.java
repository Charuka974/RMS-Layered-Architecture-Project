package org.gourmetDelight.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CustomerDto {

    private String customerID;
    private String cusName;
    private String cusPhone;
    private String cusEmail;
    private String cusAddress;
    private String cusUserID;

    public CustomerDto(String customerID, String name, String phone, String email, String address, String userID, Object o) {
        this.customerID = customerID;
        this.cusName = name;
        this.cusPhone = phone;
        this.cusEmail = email;
        this.cusAddress = address;
        this.cusUserID = userID;
    }
}
