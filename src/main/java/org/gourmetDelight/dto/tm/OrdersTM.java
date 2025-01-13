package org.gourmetDelight.dto.tm;

import javafx.scene.control.Button;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrdersTM {
    private String menuItemID;
    private String menuItem;
    private Double unitPrice;
    private Double quantity;
    private Double price;  // price for all the items from 1 item
    private String extra;


}
