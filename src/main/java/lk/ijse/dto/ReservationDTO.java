package lk.ijse.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class ReservationDTO {

    private Long reservationId;
    private Long customerId;
    private Long equipmentId;

    private LocalDate reservedFrom;
    private LocalDate reservedTo;

    private BigDecimal totalPrice;
    private String status;
    private Timestamp createdAt;

    // Default constructor
    public ReservationDTO() {}

    // Full constructor
    public ReservationDTO(Long reservationId, Long customerId, Long equipmentId,
                          LocalDate reservedFrom, LocalDate reservedTo,
                          BigDecimal totalPrice, String status, Timestamp createdAt) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Convenience constructor for creating new reservation (status defaults to Pending)
    public ReservationDTO(Long customerId, Long equipmentId,
                          LocalDate reservedFrom, LocalDate reservedTo,
                          BigDecimal totalPrice) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.totalPrice = totalPrice;
        this.status = "Pending";
    }

    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public LocalDate getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(LocalDate reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public LocalDate getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(LocalDate reservedTo) {
        this.reservedTo = reservedTo;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "reservationId=" + reservationId +
                ", customerId=" + customerId +
                ", equipmentId=" + equipmentId +
                ", reservedFrom=" + reservedFrom +
                ", reservedTo=" + reservedTo +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}