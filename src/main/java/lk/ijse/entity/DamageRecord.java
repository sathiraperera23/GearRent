package lk.ijse.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DamageRecord {

    private long damageId;
    private long rentalId;
    private long equipmentId;
    private String description;
    private BigDecimal damageCost;
    private Timestamp assessedAt;

    public DamageRecord() {}

    public DamageRecord(long damageId, long rentalId, long equipmentId,
                        String description, BigDecimal damageCost, Timestamp assessedAt) {
        this.damageId = damageId;
        this.rentalId = rentalId;
        this.equipmentId = equipmentId;
        this.description = description;
        this.damageCost = damageCost;
        this.assessedAt = assessedAt;
    }

    public long getDamageId() { return damageId; }
    public void setDamageId(long damageId) { this.damageId = damageId; }

    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(long equipmentId) { this.equipmentId = equipmentId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getDamageCost() { return damageCost; }
    public void setDamageCost(BigDecimal damageCost) { this.damageCost = damageCost; }

    public Timestamp getAssessedAt() { return assessedAt; }
    public void setAssessedAt(Timestamp assessedAt) { this.assessedAt = assessedAt; }
}
