package lk.ijse.dao;


import lk.ijse.dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOTypes type) {
        switch (type) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            // add others later
            case ADMIN:
                // return new AdminDAOImpl();
            case GEAR:
                // return new GearDAOImpl();
            case RENT:
                // return new RentDAOImpl();
            case PAYMENT:
                // return new PaymentDAOImpl();
            default:
                return null;
        }
    }
    public enum DAOTypes {
        CUSTOMER,
        ADMIN,
        DRIVER,
        GEAR,
        RENT,
        PAYMENT,
        EQUIPMENT,
        CATEGORY,
        BRANCH,
        RENTAL,
        RESERVATION,
        DAMAGE,
    }

}
