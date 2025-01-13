package org.gourmetDelight.dto.orders;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


public class OrdersDto {
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
