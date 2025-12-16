package lk.ijse.service;

import lk.ijse.service.custom.Impl.*;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public enum ServiceType {
        CUSTOMER,
        EQUIPMENT,
        BRANCH,
        RENTAL,
        RESERVATION,
        CONFIG
    }

    public SuperService getService(ServiceType type) {

        switch (type) {

            case CUSTOMER:
                return new CustomerServiceImpl();

            case EQUIPMENT:
                return new EquipmentServiceImpl();

            case RENTAL:
                return new RentalServiceImpl();

            case RESERVATION:
                return new ReservationServiceImpl();

            case CONFIG:
                return new ConfigServiceImpl();

            // Uncomment when implemented
            /*
            case BRANCH:
                return new BranchServiceImpl();
            */

            default:
                throw new RuntimeException(
                        "No Service implementation found for type: " + type
                );
        }
    }
}
