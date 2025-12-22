package lk.ijse.service;

import lk.ijse.service.custom.ReservationService;
import lk.ijse.service.custom.impl.*;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {
    }
ReservationService reservationService = new ReservationServiceImpl();
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
        CONFIG,
        CATEGORY,
        USER,
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
                if (reservationService == null) {
                    reservationService = new ReservationServiceImpl();
                }
                return reservationService;


            case CONFIG:
                return new ConfigServiceImpl();

            // Uncomment when implemented

            case BRANCH:
                return new BranchServiceImpl();

            case CATEGORY:
                return new CategoryServiceImpl();

                case USER:
                    return new UserServiceImpl();
            default:
                throw new RuntimeException(
                        "No Service implementation found for type: " + type
                );
        }
    }
}
