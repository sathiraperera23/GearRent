package lk.ijse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalDTO {
    private long rentalId;
    private long customerId;
    private long equipmentId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private LocalDate actualReturn;       // NEW
    private BigDecimal dailyPrice;
    private BigDecimal securityDeposit;
    private Long reservationId;
    private String status;

    // Payment info
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentStatus;
    private BigDecimal damageCharge;      // NEW

    public RentalDTO() {}

    public RentalDTO(long rentalId, long customerId, long equipmentId,
                     LocalDate rentedFrom, LocalDate rentedTo,
                     BigDecimal dailyPrice, BigDecimal securityDeposit,
                     Long reservationId, String status) {
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

    // ==================== GETTERS & SETTERS ====================
    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(long equipmentId) { this.equipmentId = equipmentId; }

    public LocalDate getRentedFrom() { return rentedFrom; }
    public void setRentedFrom(LocalDate rentedFrom) { this.rentedFrom = rentedFrom; }

    public LocalDate getRentedTo() { return rentedTo; }
    public void setRentedTo(LocalDate rentedTo) { this.rentedTo = rentedTo; }

    public LocalDate getActualReturn() { return actualReturn; }
    public void setActualReturn(LocalDate actualReturn) { this.actualReturn = actualReturn; }

    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }

    public BigDecimal getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(BigDecimal securityDeposit) { this.securityDeposit = securityDeposit; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public BigDecimal getDamageCharge() { return damageCharge; }
    public void setDamageCharge(BigDecimal damageCharge) { this.damageCharge = damageCharge; }
}
