package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.custom.RentalService;
import lk.ijse.db.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO = (RentalDAO) DAOFactory.getInstance()
            .getDAO(DAOFactory.DAOTypes.RENTAL);

    // Convert DTO → Entity
    private Rental toEntity(RentalDTO d) {
        return new Rental(
                d.getRentalId(),
                d.getCustomerId(),
                d.getEquipmentId(),
                d.getRentedFrom(),
                d.getRentedTo(),
                d.getDailyPrice(),
                d.getSecurityDeposit(),
                d.getReservationId(),
                d.getStatus(),
                d.getTotalAmount(),
                d.getDiscount(),
                d.getFinalAmount(),
                d.getPaymentStatus()
        );
    }

    // Convert Entity → DTO
    private RentalDTO toDTO(Rental r) {
        RentalDTO d = new RentalDTO();
        d.setRentalId(r.getRentalId());
        d.setCustomerId(r.getCustomerId());
        d.setEquipmentId(r.getEquipmentId());
        d.setRentedFrom(r.getRentedFrom());
        d.setRentedTo(r.getRentedTo());
        d.setDailyPrice(r.getDailyPrice());
        d.setSecurityDeposit(r.getSecurityDeposit());
        d.setReservationId(r.getReservationId());
        d.setStatus(r.getStatus());
        d.setTotalAmount(r.getTotalAmount());
        d.setDiscount(r.getDiscount());
        d.setFinalAmount(r.getFinalAmount());
        d.setPaymentStatus(r.getPaymentStatus());
        return d;
    }

    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {
        validateDates(dto.getRentedFrom(), dto.getRentedTo());

        // Calculate total days and amount
        long days = ChronoUnit.DAYS.between(dto.getRentedFrom(), dto.getRentedTo()) + 1;
        BigDecimal total = dto.getDailyPrice().multiply(BigDecimal.valueOf(days));

        dto.setTotalAmount(total);
        dto.setDiscount(BigDecimal.ZERO);
        dto.setFinalAmount(total);
        dto.setStatus("Open");           // or "Active" — match your enum in DB
        dto.setPaymentStatus("Unpaid");

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            boolean result = rentalDAO.save(toEntity(dto));
            if (!result) {
                throw new RuntimeException("Failed to save rental");
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    @Override
    public boolean updateRental(RentalDTO dto) throws Exception {
        validateDates(dto.getRentedFrom(), dto.getRentedTo());
        return rentalDAO.update(toEntity(dto));
    }

    // Temporarily disabled — requires columns not in current schema
    // @Override
    // public boolean returnRental(long rentalId, LocalDate actualReturn, BigDecimal damageCharge, String damageNote) throws Exception { ... }

    // @Override
    // public boolean closeRental(long rentalId, RentalDTO returnInfo) throws Exception { ... }

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
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findOverdueRentals(LocalDate.now())) {
            list.add(toDTO(r));
        }
        return list;
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Rental dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Return date cannot be before rental date");
        }
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        if (days > 30) {
            throw new IllegalArgumentException("Maximum rental duration is 30 days");
        }
    }
}