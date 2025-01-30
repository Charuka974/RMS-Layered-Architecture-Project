package org.gourmetDelight.entity;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString

public class MenuItem {

    private String menuItemID;
    private String name;
    private String description;
    private double price;
    private String category;
    private String kitchenSection;

    public MenuItem() {
    }


    public MenuItem(String menuItemID, String name, String description, double price, String category, String kitchenSection) {
        this.menuItemID = menuItemID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.kitchenSection = kitchenSection;
    }


    public String getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(String menuItemID) {
        this.menuItemID = menuItemID;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKitchenSection() {
        return kitchenSection;
    }

    public void setKitchenSection(String kitchenSection) {
        this.kitchenSection = kitchenSection;
    }


    @Override
    public String toString() {
        return "MenuItemDto{" +
                "menuItemID='" + menuItemID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", kitchenSection='" + kitchenSection + '\'' +
                '}';
    }



}
