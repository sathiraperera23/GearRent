package lk.ijse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalDTO {

    private Long rentalId;
    private Long customerId;
    private Long equipmentId;

    private LocalDate rentedFrom;
    private LocalDate rentedTo;

    private BigDecimal dailyPrice;
    private BigDecimal securityDeposit;
    private Long reservationId;
    private String status;                  // Open / Closed
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String paymentStatus;           // Paid / Partially Paid / Unpaid

    // Default constructor
    public RentalDTO() {}

    // Constructor matching current rentals table
    public RentalDTO(Long rentalId, Long customerId, Long equipmentId,
                     LocalDate rentedFrom, LocalDate rentedTo,
                     BigDecimal dailyPrice, BigDecimal securityDeposit,
                     Long reservationId, String status,
                     BigDecimal totalAmount, BigDecimal discount,
                     BigDecimal finalAmount, String paymentStatus) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.dailyPrice = dailyPrice;
        this.securityDeposit = securityDeposit;
        this.reservationId = reservationId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.paymentStatus = paymentStatus;
    }

    // ==================== Getters & Setters ====================

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "RentalDTO{" +
                "rentalId=" + rentalId +
                ", customerId=" + customerId +
                ", equipmentId=" + equipmentId +
                ", rentedFrom=" + rentedFrom +
                ", rentedTo=" + rentedTo +
                ", dailyPrice=" + dailyPrice +
                ", securityDeposit=" + securityDeposit +
                ", reservationId=" + reservationId +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", finalAmount=" + finalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}