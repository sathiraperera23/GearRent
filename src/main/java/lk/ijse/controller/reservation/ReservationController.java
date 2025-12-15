package lk.ijse.controller.reservation;

import lk.ijse.dto.ReservationDTO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReservationController {

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.RESERVATION);

    private final Scanner scanner = new Scanner(System.in);

    public void createReservation() {
        try {
            System.out.print("Customer ID: ");
            long customerId = Long.parseLong(scanner.nextLine());

            System.out.print("Equipment ID: ");
            long equipmentId = Long.parseLong(scanner.nextLine());

            System.out.print("Reserved From (YYYY-MM-DD): ");
            LocalDate from = LocalDate.parse(scanner.nextLine());

            System.out.print("Reserved To (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(scanner.nextLine());

            System.out.print("Total Price: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());

            ReservationDTO dto = new ReservationDTO(customerId, equipmentId, from, to, price);
            boolean success = reservationService.saveReservation(dto);
            System.out.println(success ? "Reservation Added!" : "Failed to add reservation.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listReservations() {
        try {
            List<ReservationDTO> list = reservationService.getAllReservations();
            System.out.println("ID | Customer | Equipment | From | To | Total | Status");
            list.forEach(r -> System.out.println(
                    r.getReservationId() + " | " + r.getCustomerId() + " | " + r.getEquipmentId() +
                            " | " + r.getReservedFrom() + " | " + r.getReservedTo() +
                            " | " + r.getTotalPrice() + " | " + r.getStatus()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateReservation() {
        try {
            System.out.print("Reservation ID to update: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("New Reserved To (YYYY-MM-DD): ");
            LocalDate to = LocalDate.parse(scanner.nextLine());

            System.out.print("New Total Price: ");
            BigDecimal price = new BigDecimal(scanner.nextLine());

            System.out.print("New Status (Pending/Confirmed/Cancelled): ");
            String status = scanner.nextLine();

            ReservationDTO dto = new ReservationDTO();
            dto.setReservationId(id);
            dto.setReservedTo(to);
            dto.setTotalPrice(price);
            dto.setStatus(status);

            boolean success = reservationService.updateReservation(dto);
            System.out.println(success ? "Reservation Updated!" : "Update Failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteReservation() {
        try {
            System.out.print("Enter Reservation ID: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = reservationService.deleteReservation(id);
            System.out.println(success ? "Reservation Deleted!" : "Delete Failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchReservation() {
        try {
            System.out.print("Enter Reservation ID: ");
            long id = Long.parseLong(scanner.nextLine());

            ReservationDTO dto = reservationService.searchReservation(id);
            if (dto != null) {
                System.out.println("Reservation Found: " + dto.getCustomerId() + " | " +
                        dto.getEquipmentId() + " | " + dto.getReservedFrom() + " | " +
                        dto.getReservedTo() + " | " + dto.getTotalPrice() + " | " + dto.getStatus());
            } else {
                System.out.println("Reservation not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Business Methods ---

    public void confirmReservation() {
        try {
            System.out.print("Reservation ID to confirm: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = reservationService.confirmReservation(id);
            System.out.println(success ? "Reservation Confirmed!" : "Confirmation Failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelReservation() {
        try {
            System.out.print("Reservation ID to cancel: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = reservationService.cancelReservation(id);
            System.out.println(success ? "Reservation Cancelled!" : "Cancellation Failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRentalFromReservation() {
        try {
            System.out.print("Reservation ID to create rental from: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = reservationService.createRentalFromReservation(id);
            System.out.println(success ? "Rental Created!" : "Rental Creation Failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
