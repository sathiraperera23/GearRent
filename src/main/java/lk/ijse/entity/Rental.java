package lk.ijse.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rental {
    private long rentalId;
    private long customerId;
    private long equipmentId;
    private long branchId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private LocalDate actualReturn;
    private BigDecimal dailyPrice;
    private BigDecimal securityDeposit;
    private Long reservationId;
    private String status;                 // Active / Returned / Overdue / Cancelled
    private LocalDateTime createdAt;

    // Payment info
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentStatus;

    // Damage info
    private BigDecimal damageCharge;
    private String damageDescription;

    public Rental() {}

    public Rental(long rentalId, long customerId, long equipmentId, long branchId,
                  LocalDate rentedFrom, LocalDate rentedTo, LocalDate actualReturn,
                  BigDecimal dailyPrice, BigDecimal securityDeposit, Long reservationId,
                  String status, LocalDateTime createdAt, BigDecimal totalAmount,
                  BigDecimal discount, BigDecimal finalAmount, String paymentStatus,
                  BigDecimal damageCharge, String damageDescription) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.branchId = branchId;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.actualReturn = actualReturn;
        this.dailyPrice = dailyPrice;
        this.securityDeposit = securityDeposit;
        this.reservationId = reservationId;
        this.status = status;
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.paymentStatus = paymentStatus;
        this.damageCharge = damageCharge;
        this.damageDescription = damageDescription;
    }

    // ==================== Getters & Setters ====================
    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(long equipmentId) { this.equipmentId = equipmentId; }

    public long getBranchId() { return branchId; }
    public void setBranchId(long branchId) { this.branchId = branchId; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }
}
