package lk.ijse.controller.rental;


import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class RentalController {

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    private final Scanner scanner = new Scanner(System.in);

    /* ================= CREATE ================= */

    public void createRental() {
        try {
            System.out.print("Customer ID: ");
            long customerId = Long.parseLong(scanner.nextLine());

            System.out.print("Equipment ID: ");
            long equipmentId = Long.parseLong(scanner.nextLine());

            System.out.print("Rented From (yyyy-mm-dd): ");
            Date rentedFrom = Date.valueOf(scanner.nextLine());

            System.out.print("Rented To (yyyy-mm-dd): ");
            Date rentedTo = Date.valueOf(scanner.nextLine());

            System.out.print("Daily Price: ");
            double dailyPrice = Double.parseDouble(scanner.nextLine());

            System.out.print("Security Deposit: ");
            double securityDeposit = Double.parseDouble(scanner.nextLine());

            System.out.print("Reservation ID (optional, press Enter to skip): ");
            String reservationInput = scanner.nextLine();
            Long reservationId = reservationInput.isEmpty()
                    ? null
                    : Long.parseLong(reservationInput);

            RentalDTO dto = new RentalDTO(
                    0,
                    customerId,
                    equipmentId,
                    rentedFrom,
                    rentedTo,
                    dailyPrice,
                    securityDeposit,
                    reservationId,
                    "Open",
                    null
            );

            boolean success = rentalService.saveRental(dto);
            System.out.println(success ? "Rental created successfully!" : "Rental creation failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= READ ALL ================= */

    public void listRentals() {
        try {
            List<RentalDTO> list = rentalService.getAllRentals();

            System.out.println("\n--- RENTALS LIST ---");
            list.forEach(r -> System.out.println(
                    r.getRentalId() + " | Cust:" + r.getCustomerId() +
                            " | Equip:" + r.getEquipmentId() +
                            " | " + r.getRentedFrom() + " → " + r.getRentedTo() +
                            " | Rs." + r.getDailyPrice() +
                            " | Status: " + r.getStatus()
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= READ BY ID ================= */

    public void searchRental() {
        try {
            System.out.print("Enter Rental ID: ");
            long id = Long.parseLong(scanner.nextLine());

            RentalDTO dto = rentalService.searchRental(id);
            if (dto != null) {
                System.out.println("\nRental Found:");
                System.out.println("Rental ID      : " + dto.getRentalId());
                System.out.println("Customer ID    : " + dto.getCustomerId());
                System.out.println("Equipment ID   : " + dto.getEquipmentId());
                System.out.println("Period         : " + dto.getRentedFrom() + " → " + dto.getRentedTo());
                System.out.println("Daily Price    : " + dto.getDailyPrice());
                System.out.println("Deposit        : " + dto.getSecurityDeposit());
                System.out.println("Reservation ID : " + dto.getReservationId());
                System.out.println("Status         : " + dto.getStatus());
                System.out.println("Created At     : " + dto.getCreatedAt());
            } else {
                System.out.println("Rental not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= UPDATE ================= */

    public void updateRental() {
        try {
            System.out.print("Rental ID to update: ");
            long rentalId = Long.parseLong(scanner.nextLine());

            System.out.print("New Rented To (yyyy-mm-dd): ");
            Date rentedTo = Date.valueOf(scanner.nextLine());

            System.out.print("New Daily Price: ");
            double dailyPrice = Double.parseDouble(scanner.nextLine());

            System.out.print("New Security Deposit: ");
            double securityDeposit = Double.parseDouble(scanner.nextLine());

            System.out.print("Status (Open/Closed): ");
            String status = scanner.nextLine();

            RentalDTO dto = new RentalDTO();
            dto.setRentalId(rentalId);
            dto.setRentedTo(rentedTo);
            dto.setDailyPrice(dailyPrice);
            dto.setSecurityDeposit(securityDeposit);
            dto.setStatus(status);

            boolean success = rentalService.updateRental(dto);
            System.out.println(success ? "Rental updated successfully!" : "Rental update failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= DELETE ================= */

    public void deleteRental() {
        try {
            System.out.print("Enter Rental ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = rentalService.deleteRental(id);
            System.out.println(success ? "Rental deleted successfully!" : "Rental deletion failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
