package com.isaiah.Church.Management.System.dto;

public class MonthlyContributionDTO {

    private String month;
    private Double amount;

    public MonthlyContributionDTO() {
    }

    public MonthlyContributionDTO(String month, Double amount) {
        this.month = month;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}