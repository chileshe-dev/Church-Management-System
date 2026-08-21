package com.isaiah.Church.Management.System.dto;

public class AttendanceSummaryDTO {

    private String status;
    private Long total;

    public AttendanceSummaryDTO() {
    }

    public AttendanceSummaryDTO(String status, Long total) {
        this.status = status;
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}