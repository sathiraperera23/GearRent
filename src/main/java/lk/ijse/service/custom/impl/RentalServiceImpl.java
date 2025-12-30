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

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance()
                    .getDAO(DAOFactory.DAOTypes.RENTAL);

    private static final BigDecimal LATE_FEE_PER_DAY =
            new BigDecimal("2500");


    private Rental toEntity(RentalDTO d) {
        return new Rental(
                d.getRentalId(),
                d.getCustomerId(),
                d.getEquipmentId(),
                d.getBranchId(),
                d.getRentedFrom(),
                d.getRentedTo(),
                d.getActualReturn(),
                d.getDailyPrice(),
                d.getSecurityDeposit(),
                d.getReservationId(),
                d.getStatus(),
                null,
                d.getTotalAmount(),
                d.getDiscount(),
                d.getFinalAmount(),
                d.getPaymentStatus(),
                d.getDamageCharge(),
                d.getDamageDescription()
        );
    }

    private RentalDTO toDTO(Rental r) {
        RentalDTO d = new RentalDTO();
        d.setRentalId(r.getRentalId());
        d.setCustomerId(r.getCustomerId());
        d.setEquipmentId(r.getEquipmentId());
        d.setBranchId(r.getBranchId());
        d.setRentedFrom(r.getRentedFrom());
        d.setRentedTo(r.getRentedTo());
        d.setActualReturn(r.getActualReturn());
        d.setDailyPrice(r.getDailyPrice());
        d.setSecurityDeposit(r.getSecurityDeposit());
        d.setReservationId(r.getReservationId());
        d.setStatus(r.getStatus());
        d.setTotalAmount(r.getTotalAmount());
        d.setDiscount(r.getDiscount());
        d.setFinalAmount(r.getFinalAmount());
        d.setPaymentStatus(r.getPaymentStatus());
        d.setDamageCharge(r.getDamageCharge());
        d.setDamageDescription(r.getDamageDescription());
        return d;
    }


    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {

        validateDates(dto.getRentedFrom(), dto.getRentedTo());

        long days =
                ChronoUnit.DAYS.between(
                        dto.getRentedFrom(),
                        dto.getRentedTo()
                ) + 1;

        BigDecimal total =
                dto.getDailyPrice()
                        .multiply(BigDecimal.valueOf(days));

        dto.setTotalAmount(total);
        dto.setDiscount(BigDecimal.ZERO);
        dto.setFinalAmount(total);
        dto.setStatus("Active");
        dto.setPaymentStatus("Unpaid");

        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            boolean result =
                    rentalDAO.save(toEntity(dto));

            if (!result)
                throw new RuntimeException("Rental save failed");

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


    @Override
    public boolean returnRental(
            long rentalId,
            LocalDate actualReturn,
            BigDecimal damageCharge,
            String damageNote
    ) throws Exception {

        return processReturn(
                rentalId,
                actualReturn,
                damageCharge,
                damageNote
        );
    }

    @Override
    public boolean closeRental(
            long rentalId,
            RentalDTO returnInfo
    ) throws Exception {

        return processReturn(
                rentalId,
                returnInfo.getActualReturn(),
                returnInfo.getDamageCharge(),
                returnInfo.getDamageDescription()
        );
    }

    /**
     * Core transactional return logic
     */
    private boolean processReturn(
            long rentalId,
            LocalDate actualReturn,
            BigDecimal damageCharge,
            String damageNote
    ) throws Exception {

        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            Rental r = rentalDAO.find(rentalId);
            if (r == null)
                throw new IllegalStateException("Rental not found");

            if (actualReturn == null)
                throw new IllegalArgumentException(
                        "Actual return date required"
                );

            long lateDays =
                    ChronoUnit.DAYS.between(
                            r.getRentedTo(),
                            actualReturn
                    );

            if (lateDays < 0) lateDays = 0;

            BigDecimal lateFee =
                    LATE_FEE_PER_DAY.multiply(
                            BigDecimal.valueOf(lateDays)
                    );

            BigDecimal damage =
                    damageCharge != null
                            ? damageCharge
                            : BigDecimal.ZERO;

            BigDecimal charges = lateFee.add(damage);
            BigDecimal deposit = r.getSecurityDeposit();

            BigDecimal payable =
                    r.getFinalAmount()
                            .add(charges)
                            .subtract(deposit);

            if (payable.compareTo(BigDecimal.ZERO) <= 0) {
                payable = BigDecimal.ZERO;
                r.setPaymentStatus("Paid");
            } else {
                r.setPaymentStatus("Partially Paid");
            }

            r.setActualReturn(actualReturn);
            r.setDamageCharge(damage);
            r.setDamageDescription(damageNote);
            r.setFinalAmount(payable);
            r.setStatus(
                    lateDays > 0 ? "Overdue" : "Returned"
            );

            boolean updated = rentalDAO.update(r);
            if (!updated)
                throw new RuntimeException("Return update failed");

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
        for (Rental r :
                rentalDAO.findOverdueRentals(LocalDate.now())) {
            list.add(toDTO(r));
        }
        return list;
    }


    private void validateDates(LocalDate from, LocalDate to) {

        if (from == null || to == null)
            throw new IllegalArgumentException("Dates required");

        if (to.isBefore(from))
            throw new IllegalArgumentException("Invalid rental period");

        long days =
                ChronoUnit.DAYS.between(from, to) + 1;

        if (days > 30)
            throw new IllegalArgumentException(
                    "Maximum rental duration is 30 days"
            );
    }
}
