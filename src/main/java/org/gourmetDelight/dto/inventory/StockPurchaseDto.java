package org.gourmetDelight.dto.inventory;

import java.time.LocalDate;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockPurchaseDto {
    private String purchaseID;
    private String supplierID;
    private double totalAmount;
    private LocalDate purchaseDate;

}
