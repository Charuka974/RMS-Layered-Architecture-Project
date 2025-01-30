package org.gourmetDelight.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


public class Orders {
    private String orderID;
    private String customerID;
    private String userID;
    private LocalDate orderDate;
    private double totalAmount;
    private String status;
    private String orderType;
    private String reservationID;
    private String paymentID;


}
