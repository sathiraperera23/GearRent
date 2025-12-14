package lk.ijse.entity;

import java.sql.Timestamp;

public class Branch {
    private int branchId;
    private String code;
    private String name;
    private String address;
    private String contact;
    private Timestamp createdAt;

    public Branch() {}

    public Branch(int branchId, String code, String name, String address, String contact, Timestamp createdAt) {
        this.branchId = branchId;
        this.code = code;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.createdAt = createdAt;
    }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}