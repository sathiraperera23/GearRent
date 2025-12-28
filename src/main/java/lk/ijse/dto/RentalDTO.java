package lk.ijse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalDTO {

    private long rentalId;
    private long customerId;
    private long equipmentId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private BigDecimal dailyPrice;
    private BigDecimal securityDeposit;
    private Long reservationId;
    private String status;

    public RentalDTO() {
    }

    public RentalDTO(
            long rentalId,
            long customerId,
            long equipmentId,
            LocalDate rentedFrom,
            LocalDate rentedTo,
            BigDecimal dailyPrice,
            BigDecimal securityDeposit,
            Long reservationId,
            String status
    ) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.dailyPrice = dailyPrice;
        this.securityDeposit = securityDeposit;
        this.reservationId = reservationId;
        this.status = status;
    }

    // ---------- Getters & Setters ----------

    public long getRentalId() {
        return rentalId;
    }

    public void setRentalId(long rentalId) {
        this.rentalId = rentalId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LocalDate getRentedFrom() {
        return rentedFrom;
    }

    public void setRentedFrom(LocalDate rentedFrom) {
        this.rentedFrom = rentedFrom;
    }

    public LocalDate getRentedTo() {
        return rentedTo;
    }

    public void setRentedTo(LocalDate rentedTo) {
        this.rentedTo = rentedTo;
    }

    public BigDecimal getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(BigDecimal dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public BigDecimal getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(BigDecimal securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
