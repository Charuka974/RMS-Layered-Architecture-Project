package org.gourmetDelight.dto.menuItems;

import lombok.*;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString


public class MenuItemIngredientsDto {

    private String menuItemID;
    private String inventoryItemID;
    private double quantityNeeded;

    public MenuItemIngredientsDto() {
    }

    public MenuItemIngredientsDto(String menuItemID, String inventoryItemID, double quantityNeeded) {
        this.menuItemID = menuItemID;
        this.inventoryItemID = inventoryItemID;
        this.quantityNeeded = quantityNeeded;
    }

    public String getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(String menuItemID) {
        this.menuItemID = menuItemID;
    }

    public String getInventoryItemID() {
        return inventoryItemID;
    }

    public void setInventoryItemID(String inventoryItemID) {
        this.inventoryItemID = inventoryItemID;
    }

    public double getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(double quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }


    @Override
    public String toString() {
        return "MenuItemIngredientsDto{" +
                "menuItemID='" + menuItemID + '\'' +
                ", inventoryItemID='" + inventoryItemID + '\'' +
                ", quantityNeeded=" + quantityNeeded +
                '}';
    }

}
