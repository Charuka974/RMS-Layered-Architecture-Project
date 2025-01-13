package org.gourmetDelight.dto.inventory;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockPurchaseItemsDto {
    private String inventoryItemID;
    private String purchaseID;
    private double unitPerPrice;
    private double unitPrice;
    private double unitsBought;
    private String status;

}
