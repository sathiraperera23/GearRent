package lk.ijse.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Rental {

    private long rentalId;
    private long customerId;
    private long equipmentId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private BigDecimal dailyPrice;
    private BigDecimal securityDeposit;
    private Long reservationId;   // nullable
    private String status;         // Open | Closed
    private Timestamp createdAt;

    public Rental() {
    }

    public Rental(
            long rentalId,
            long customerId,
            long equipmentId,
            LocalDate rentedFrom,
            LocalDate rentedTo,
            BigDecimal dailyPrice,
            BigDecimal securityDeposit,
            Long reservationId,
            String status,
            Timestamp createdAt
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
        this.createdAt = createdAt;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
