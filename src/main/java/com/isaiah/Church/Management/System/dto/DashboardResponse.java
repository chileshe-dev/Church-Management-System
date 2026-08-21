package com.isaiah.Church.Management.System.dto;

public class DashboardResponse {

    private long totalMembers;
    private long totalContributions;
    private long totalEvents;
    private long totalAttendance;
    private long totalMinistries;
    private long totalUsers;
    private Double totalContributionAmount;

    public DashboardResponse() {
    }

    public DashboardResponse(
            long totalMembers,
            long totalContributions,
            long totalEvents,
            long totalAttendance,
            long totalMinistries,
            long totalUsers) {

        this.totalMembers = totalMembers;
        this.totalContributions = totalContributions;
        this.totalEvents = totalEvents;
        this.totalAttendance = totalAttendance;
        this.totalMinistries = totalMinistries;
        this.totalUsers = totalUsers;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public long getTotalContributions() {
        return totalContributions;
    }

    public void setTotalContributions(long totalContributions) {
        this.totalContributions = totalContributions;
    }

    public long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public long getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(long totalAttendance) {
        this.totalAttendance = totalAttendance;
    }

    public long getTotalMinistries() {
        return totalMinistries;
    }

    public void setTotalMinistries(long totalMinistries) {
        this.totalMinistries = totalMinistries;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Double getTotalContributionAmount() {
    return totalContributionAmount;
}

public void setTotalContributionAmount(Double totalContributionAmount) {
    this.totalContributionAmount = totalContributionAmount;
   }
}



