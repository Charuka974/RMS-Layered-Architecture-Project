package org.gourmetDelight.entity;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockPurchaseItems {
    private String inventoryItemID;
    private String purchaseID;
    private double unitPerPrice;
    private double unitPrice;
    private double unitsBought;
    private String status;

}
