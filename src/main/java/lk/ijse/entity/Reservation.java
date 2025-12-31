package lk.ijse.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {

    private Long reservationId;
    private Long customerId;
    private Long equipmentId;

    private Date reservedFrom;
    private Date reservedTo;

    private double totalPrice;
    private String status;
    private Timestamp createdAt;

    // Default constructor
    public Reservation() {}

    // Full constructor
    public Reservation(Long reservationId, Long customerId, Long equipmentId,
                       Date reservedFrom, Date reservedTo,
                       double totalPrice, String status, Timestamp createdAt) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
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

    public Date getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(Date reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public Date getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(Date reservedTo) {
        this.reservedTo = reservedTo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
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
        return "Reservation{" +
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