package lk.ijse.dto;

import java.math.BigDecimal;

public class ConfigDTO {
    private int configId;
    private BigDecimal lateFeePerDay;
    private BigDecimal maxDeposit;
    private BigDecimal regularDiscount;
    private BigDecimal silverDiscount;
    private BigDecimal goldDiscount;

    public ConfigDTO() {}

    public ConfigDTO(int configId, BigDecimal lateFeePerDay, BigDecimal maxDeposit,
                     BigDecimal regularDiscount, BigDecimal silverDiscount, BigDecimal goldDiscount) {
        this.configId = configId;
        this.lateFeePerDay = lateFeePerDay;
        this.maxDeposit = maxDeposit;
        this.regularDiscount = regularDiscount;
        this.silverDiscount = silverDiscount;
        this.goldDiscount = goldDiscount;
    }

    // Getters and Setters
    public int getConfigId() { return configId; }
    public void setConfigId(int configId) { this.configId = configId; }

    public BigDecimal getLateFeePerDay() { return lateFeePerDay; }
    public void setLateFeePerDay(BigDecimal lateFeePerDay) { this.lateFeePerDay = lateFeePerDay; }

    public BigDecimal getMaxDeposit() { return maxDeposit; }
    public void setMaxDeposit(BigDecimal maxDeposit) { this.maxDeposit = maxDeposit; }

    public BigDecimal getRegularDiscount() { return regularDiscount; }
    public void setRegularDiscount(BigDecimal regularDiscount) { this.regularDiscount = regularDiscount; }

    public BigDecimal getSilverDiscount() { return silverDiscount; }
    public void setSilverDiscount(BigDecimal silverDiscount) { this.silverDiscount = silverDiscount; }

    public BigDecimal getGoldDiscount() { return goldDiscount; }
    public void setGoldDiscount(BigDecimal goldDiscount) { this.goldDiscount = goldDiscount; }
}
