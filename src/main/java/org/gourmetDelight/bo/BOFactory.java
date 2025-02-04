package org.gourmetDelight.bo;

import org.gourmetDelight.bo.custom.impl.*;



public class BOFactory {

    private static BOFactory boFactory;
    private BOFactory() {
    }
    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOType{
        CUSTOMER,EMPLOYEE,USERS,ORDERS,
        INVENTORY,MENU_ITEM,MENU_ITEM_INGREDIENTS,
        RESERVATIONS,PURCHASE,SUPPLIER,TABLE,
        REMINDER,PURCHASE_ITEMS
    }
    public SuperBO getBO(BOFactory.BOType type) {

        switch (type) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case USERS:
                return new UserBOImpl();
            case ORDERS:
                return new OrderBOImpl();
            case INVENTORY:
                return new InventoryItemsBOImpl();
            case MENU_ITEM:
                return new MenuItemBOImpl();
            case MENU_ITEM_INGREDIENTS:
                return new MenuItemIngredientsBOImpl();
            case RESERVATIONS:
                return new ReservationBOImpl();
            case PURCHASE:
                return new PurchaseBOImpl();
            case PURCHASE_ITEMS:
                return new PurchaseItemsBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case TABLE:
                return new TableBOImpl();
            case REMINDER:
                return new ReminderBOImpl();
            default:
                return null;
        }

    }



}
