package org.gourmetDelight.dto.inventory;
import lombok.*;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString

public class InventoryItemDto {
    private String inventoryItemId;
    private String name;
    private String description;
    private double quantity;
    private String unit;


    public InventoryItemDto() {
    }


    public InventoryItemDto(String inventoryItemId, String name, String description, double quantity, String unit) {
        this.inventoryItemId = inventoryItemId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
    }


    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Override
    public String toString() {
        return "InventoryItemDto{" +
                "inventoryItemId='" + inventoryItemId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }


}
