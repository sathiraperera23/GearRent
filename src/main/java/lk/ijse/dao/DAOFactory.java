package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public SuperDAO getDAO(DAOTypes type) {

        switch (type) {

            case CUSTOMER:
                return new CustomerDAOImpl();

            case RENTAL:
                return new RentalDAOImpl();

            case RESERVATION:
                return new ReservationDAOImpl();

            case CONFIG:
                return new ConfigDAOImpl();

            // Uncomment when implemented

            case EQUIPMENT:
                return new EquipmentDAOImpl();

            case CATEGORY:
                return new CategoryDAOImpl();

            case DAMAGE:
                return new DamageRecordDAOImpl();

            case BRANCH:
                return new BranchDAOImpl();


            default:
                throw new RuntimeException(
                        "No DAO implementation found for type: " + type
                );
        }
    }

    public enum DAOTypes {
        CUSTOMER,
        RENTAL,
        RESERVATION,
        CONFIG,
        EQUIPMENT,
        CATEGORY,
        DAMAGE,
        BRANCH
    }
}
