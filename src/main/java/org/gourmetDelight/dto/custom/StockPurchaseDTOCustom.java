package org.gourmetDelight.dto.custom;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockPurchaseDTOCustom {
    private String purchaseID;
    private String itemID;
    private String itemName;
    private double unitPrice;
    private double quantity;
    private double totalAmount;
    private LocalDate purchaseDate;
    private String supplierID;
    private String status;

    private String supplierName;
    private double unit;
    private String unitsMeasured;

}
