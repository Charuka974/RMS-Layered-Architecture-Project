package org.gourmetDelight.entity;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderItems {
    private String OrderID;
    private String MenuItemID;
    private double Quantity;  // how many units of one food
    private double Price;  // price * units

}
