package lk.ijse.controller.equipment;



import lk.ijse.service.custom.EquipmentService;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.EquipmentService;
import lk.ijse.dto.EquipmentDTO;

import java.util.List;
import java.util.Scanner;

public class EquipmentController {

    private final EquipmentService equipmentService =
            (EquipmentService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.EQUIPMENT);

    private final Scanner scanner = new Scanner(System.in);

    public void createEquipment() {
        try {
            System.out.print("Equipment Code: ");
            String code = scanner.nextLine();

            System.out.print("Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Model: ");
            String model = scanner.nextLine();

            System.out.print("Purchase Year: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Branch ID: ");
            int branchId = Integer.parseInt(scanner.nextLine());

            System.out.print("Category ID: ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            System.out.print("Daily Price: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Security Deposit: ");
            double deposit = Double.parseDouble(scanner.nextLine());

            // Build DTO matching your constructor
            EquipmentDTO dto = new EquipmentDTO(
                    0,                 // equipmentId (auto increment)
                    categoryId,
                    branchId,
                    code,              // equipment_code
                    brand,
                    model,
                    year,
                    price,
                    deposit,
                    "Available"        // default status
            );

            boolean success = equipmentService.saveEquipment(dto);
            System.out.println(success ? "Equipment Added!" : "Failed to Add Equipment.");

        } catch (Exception e) {
            System.out.println("Error creating equipment: " + e.getMessage());
        }
    }

    public void listEquipment() {
        try {
            List<EquipmentDTO> list = equipmentService.getAllEquipment();
            System.out.println("\n--- EQUIPMENT LIST ---");

            for (EquipmentDTO eq : list) {
                System.out.println(
                        eq.getEquipmentId() + " | " +
                                eq.getEquipmentCode() + " | " +
                                eq.getBrand() + " | " +
                                eq.getModel() + " | " +
                                eq.getStatus()
                );
            }

        } catch (Exception e) {
            System.out.println("Error fetching list: " + e.getMessage());
        }
    }

    public void updateEquipment() {
        try {
            System.out.print("Equipment ID to Update: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("New Brand: ");
            String brand = scanner.nextLine();

            System.out.print("New Model: ");
            String model = scanner.nextLine();

            System.out.print("New Status (Available / Reserved / Rented / Under Maintenance): ");
            String status = scanner.nextLine();

            EquipmentDTO dto = new EquipmentDTO();
            dto.setEquipmentId(id);
            dto.setBrand(brand);
            dto.setModel(model);
            dto.setStatus(status);

            boolean success = equipmentService.updateEquipment(dto);
            System.out.println(success ? "Updated Successfully!" : "Update Failed.");

        } catch (Exception e) {
            System.out.println("Error updating equipment: " + e.getMessage());
        }
    }

    public void deleteEquipment() {
        try {
            System.out.print("Enter Equipment ID: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = equipmentService.deleteEquipment(id);
            System.out.println(success ? "Deleted Successfully!" : "Delete Failed.");

        } catch (Exception e) {
            System.out.println("Error deleting equipment: " + e.getMessage());
        }
    }
}