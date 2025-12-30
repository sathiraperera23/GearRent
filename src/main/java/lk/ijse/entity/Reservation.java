package lk.ijse.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Reservation {
    private long reservationId;
    private long customerId;
    private long equipmentId;
    private Date reservedFrom;
    private Date reservedTo;
    private double totalPrice;
    private String status;
    private Timestamp createdAt;

    public Reservation() {}

    public Reservation(long reservationId, long customerId, long equipmentId, Date reservedFrom, Date reservedTo,
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

    public long getReservationId() { return reservationId; }
    public void setReservationId(long reservationId) { this.reservationId = reservationId; }
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(long equipmentId) { this.equipmentId = equipmentId; }
    public Date getReservedFrom() { return reservedFrom; }
    public void setReservedFrom(Date reservedFrom) { this.reservedFrom = reservedFrom; }
    public Date getReservedTo() { return reservedTo; }
    public void setReservedTo(Date reservedTo) { this.reservedTo = reservedTo; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
