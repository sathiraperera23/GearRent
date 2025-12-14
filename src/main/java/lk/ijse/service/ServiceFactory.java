package lk.ijse.service;


import lk.ijse.service.custom.Impl.EquipmentServiceImpl;

public class ServiceFactory {

    private static ServiceFactory instance;

    public static ServiceFactory getInstance() {
        if (instance == null) instance = new ServiceFactory();
        return instance;
    }

    public enum ServiceType {
        EQUIPMENT,
        BRANCH,
        RENTAL
    }

    public SuperService getService(ServiceType type) {
        return switch (type) {
            case EQUIPMENT -> new EquipmentServiceImpl();
        };
    }
}
