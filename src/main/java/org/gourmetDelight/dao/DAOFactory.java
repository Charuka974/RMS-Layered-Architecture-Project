package org.gourmetDelight.dao;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }
    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOType{
        CUSTOMER,EMPLOYEE,USERS,ORDERS,
        PAYMENTS,QUERY,HOME,FORGOT_PASSWORD,
        INVENTORY,LOGIN,MENU_ITEM,MENU_ITEM_INGREDIENTS,
        RESERVATIONS,SIGNUP,STOCK_PURCHASE,SUPPLIER,TABLE
    }
    public SuperDAO getDAO(DAOType type) {

        switch (type) {
            case CUSTOMER:
//                return new CustomerDAOImpl();
//            case EMPLOYEE:
//                return new EmployeeDAOImpl();
//            case USERS:
//                return new UsersDAOImpl();
//            case ORDERS:
//                return new OrdersDAOImpl();
//            case PAYMENTS:
//                return new PaymentDAOImpl();
//            case HOME:
//                return new HomeDAOImpl();
//            case FORGOT_PASSWORD:
//                return new ForgotPasswordDAOImpl();
//            case INVENTORY:
//                return new InventoryDAOImpl();
//            case LOGIN:
//                return new LogInDAOImpl();
//            case MENU_ITEM:
//                return new MenuItemDAOImpl();
//            case MENU_ITEM_INGREDIENTS:
//                return new MenuItemIngredientsDAOImpl();
//            case RESERVATIONS:
//                return new ReservationDAOImpl();
//            case SIGNUP:
//                return new SignUpDAOImpl();
//            case STOCK_PURCHASE:
//                return new StockPurchaseDAOImpl();
//            case SUPPLIER:
//                return new SupplierDAOImpl();
//            case TABLE:
//                return new TableDAOImpl();
//            case QUERY:
//                return new QueryDAOImpl();
            default:
                return null;
        }

    }

}
