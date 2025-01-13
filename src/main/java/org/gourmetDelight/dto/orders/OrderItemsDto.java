package org.gourmetDelight.dto.orders;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderItemsDto {
    private String OrderID;
    private String MenuItemID;
    private double Quantity;  // how many units of one food
    private double Price;  // price * units

}
