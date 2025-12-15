package lk.ijse.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DamageRecordDTO {

    private long damageId;
    private long rentalId;
    private long equipmentId;
    private String description;
    private BigDecimal damageCost;
    private Timestamp assessedAt;

    public DamageRecordDTO() {}

    public DamageRecordDTO(long damageId, long rentalId, long equipmentId,
                           String description, BigDecimal damageCost, Timestamp assessedAt) {
        this.damageId = damageId;
        this.rentalId = rentalId;
        this.equipmentId = equipmentId;
        this.description = description;
        this.damageCost = damageCost;
        this.assessedAt = assessedAt;
    }

    public long getDamageId() { return damageId; }
    public long getRentalId() { return rentalId; }
    public long getEquipmentId() { return equipmentId; }
    public String getDescription() { return description; }
    public BigDecimal getDamageCost() { return damageCost; }
    public Timestamp getAssessedAt() { return assessedAt; }
}
