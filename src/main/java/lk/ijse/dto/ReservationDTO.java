package lk.ijse.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

public class ReservationDTO {
    private long reservationId;
    private long customerId;
    private long equipmentId;
    private LocalDate reservedFrom;
    private LocalDate reservedTo;
    private BigDecimal totalPrice;
    private String status;
    private Timestamp createdAt;

    public ReservationDTO() {}

    // Full constructor
    public ReservationDTO(long reservationId, long customerId, long equipmentId,
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

    // Short constructor for creating new reservation
    public ReservationDTO(long customerId, long equipmentId, LocalDate reservedFrom, LocalDate reservedTo, BigDecimal totalPrice) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.totalPrice = totalPrice;
        this.status = "Pending";
    }

    // Getters and setters
    public long getReservationId() { return reservationId; }
    public void setReservationId(long reservationId) { this.reservationId = reservationId; }
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(long equipmentId) { this.equipmentId = equipmentId; }
    public LocalDate getReservedFrom() { return reservedFrom; }
    public void setReservedFrom(LocalDate reservedFrom) { this.reservedFrom = reservedFrom; }
    public LocalDate getReservedTo() { return reservedTo; }
    public void setReservedTo(LocalDate reservedTo) { this.reservedTo = reservedTo; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
