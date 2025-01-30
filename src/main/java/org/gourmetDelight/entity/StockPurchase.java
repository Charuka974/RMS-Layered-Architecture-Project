package org.gourmetDelight.entity;

import java.time.LocalDate;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class StockPurchase {
    private String purchaseID;
    private String supplierID;
    private double totalAmount;
    private LocalDate purchaseDate;

}
