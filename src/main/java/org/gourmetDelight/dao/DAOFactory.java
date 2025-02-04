package org.gourmetDelight.dao;

import org.gourmetDelight.dao.custom.impl.CustomerDAOImpl;
import org.gourmetDelight.dao.custom.impl.QueryDAOImpl;
import org.gourmetDelight.dao.custom.impl.employee.EmployeeDAOImpl;
import org.gourmetDelight.dao.custom.impl.employee.UsersDAOImpl;
import org.gourmetDelight.dao.custom.impl.home.ReminderDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.InventoryItemsDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.PurchaseDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.PurchaseItemsDAOImpl;
import org.gourmetDelight.dao.custom.impl.inventory.SupplierDAOImpl;
import org.gourmetDelight.dao.custom.impl.menuItems.MenuItemDAOImpl;
import org.gourmetDelight.dao.custom.impl.menuItems.MenuItemIngredientsDAOImpl;
import org.gourmetDelight.dao.custom.impl.orders.OrderItemsDAOImpl;
import org.gourmetDelight.dao.custom.impl.orders.OrdersDAOImpl;
import org.gourmetDelight.dao.custom.impl.orders.PaymentDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.ReservationDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.TableAssignmentsDAOImpl;
import org.gourmetDelight.dao.custom.impl.reservations.TableDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }
    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOType{
        CUSTOMER,EMPLOYEE,USERS,ORDERS,
        PAYMENTS,QUERY,
        INVENTORY,MENU_ITEM,MENU_ITEM_INGREDIENTS,
        RESERVATIONS,STOCK_PURCHASE,SUPPLIER,TABLE,
        REMINDER,ORDER_ITEMS,PURCHASE_ITEMS,TABLE_ASSIGNMENT
    }
    public SuperDAO getDAO(DAOType type) {

        switch (type) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case USERS:
                return new UsersDAOImpl();
            case ORDERS:
                return new OrdersDAOImpl();
            case ORDER_ITEMS:
                return new OrderItemsDAOImpl();
            case PAYMENTS:
                return new PaymentDAOImpl();
            case INVENTORY:
                return new InventoryItemsDAOImpl();
            case MENU_ITEM:
                return new MenuItemDAOImpl();
            case MENU_ITEM_INGREDIENTS:
                return new MenuItemIngredientsDAOImpl();
            case RESERVATIONS:
                return new ReservationDAOImpl();
            case STOCK_PURCHASE:
                return new PurchaseDAOImpl();
            case PURCHASE_ITEMS:
                return new PurchaseItemsDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case TABLE:
                return new TableDAOImpl();
            case TABLE_ASSIGNMENT:
                return new TableAssignmentsDAOImpl();
            case REMINDER:
                return new ReminderDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }

    }

}
