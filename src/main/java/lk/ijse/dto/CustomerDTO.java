package lk.ijse.dto;

import java.sql.Timestamp;

public class CustomerDTO {
    private long customerId;
    private String name;
    private String nicPassport;
    private String contactNo;
    private String email;
    private String address;
    private String membership; // Regular, Silver, Gold
    private Timestamp createdAt;

    public CustomerDTO() {}

    // FULL constructor
    public CustomerDTO(long customerId, String name, String nicPassport, String contactNo,
                       String email, String address, String membership, Timestamp createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.nicPassport = nicPassport;
        this.contactNo = contactNo;
        this.email = email;
        this.address = address;
        this.membership = membership;
        this.createdAt = createdAt;
    }

    // SHORT constructor (UI → Service → DAO)
    public CustomerDTO(long customerId, String name, String nicPassport, String contactNo,
                       String email, String address, String membership) {
        this.customerId = customerId;
        this.name = name;
        this.nicPassport = nicPassport;
        this.contactNo = contactNo;
        this.email = email;
        this.address = address;
        this.membership = membership;
    }

    // Getters and setters
    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNicPassport() { return nicPassport; }
    public void setNicPassport(String nicPassport) { this.nicPassport = nicPassport; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMembership() { return membership; }
    public void setMembership(String membership) { this.membership = membership; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
