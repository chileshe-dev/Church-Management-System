package com.isaiah.Church.Management.System.dto;

public class ContributionTypeDTO {

    private String type;
    private Long count;

    public ContributionTypeDTO() {
    }

    public ContributionTypeDTO(String type, Long count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}