package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.custom.RentalService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance()
                    .getDAO(DAOFactory.DAOTypes.RENTAL);

    /* ===================== HELPERS ===================== */

    private Rental toEntity(RentalDTO d) {
        return new Rental(
                d.getRentalId(),
                d.getCustomerId(),
                d.getEquipmentId(),
                d.getRentedFrom(),
                d.getRentedTo(),
                d.getActualReturn(),        // NEW
                d.getDailyPrice(),
                d.getSecurityDeposit(),
                d.getReservationId(),
                d.getStatus(),
                null,                       // created_at handled by DB
                d.getTotalAmount(),
                d.getDiscount(),
                d.getFinalAmount(),
                d.getPaymentStatus(),
                d.getDamageCharge()         // NEW
        );
    }

    private RentalDTO toDTO(Rental r) {
        RentalDTO dto = new RentalDTO(
                r.getRentalId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus()
        );

        // Set new fields
        dto.setActualReturn(r.getActualReturn());
        dto.setDamageCharge(r.getDamageCharge());
        dto.setTotalAmount(r.getTotalAmount());
        dto.setDiscount(r.getDiscount());
        dto.setFinalAmount(r.getFinalAmount());
        dto.setPaymentStatus(r.getPaymentStatus());

        return dto;
    }

    /* ===================== BUSINESS ===================== */

    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {
        validateDates(dto.getRentedFrom(), dto.getRentedTo());
        dto.setStatus("Open");
        return rentalDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateRental(RentalDTO dto) throws Exception {
        validateDates(dto.getRentedFrom(), dto.getRentedTo());
        return rentalDAO.update(toEntity(dto));
    }

    @Override
    public boolean closeRental(long rentalId) throws Exception {
        Rental r = rentalDAO.find(rentalId);
        if (r == null)
            throw new IllegalStateException("Rental not found");

        r.setStatus("Closed");
        return rentalDAO.update(r);
    }

    @Override
    public RentalDTO findRental(long rentalId) throws Exception {
        Rental r = rentalDAO.find(rentalId);
        return r != null ? toDTO(r) : null;
    }

    @Override
    public List<RentalDTO> getAllRentals() throws Exception {
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findAll()) {
            list.add(toDTO(r));
        }
        return list;
    }

    @Override
    public List<RentalDTO> getOverdueRentals() throws Exception {
        LocalDate today = LocalDate.now();
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findOverdueRentals(today)) {
            list.add(toDTO(r));
        }
        return list;
    }

    /* ===================== VALIDATION ===================== */

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("Dates are required");

        if (to.isBefore(from))
            throw new IllegalArgumentException("Return date must be after rent date");
    }
}
