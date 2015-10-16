package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String clientName;
    private String scheduleDate;
    private String startTime;
    private String endTime;
    private String locationAddress;

    public Schedule(String clientName,String scheduleDate , String startTime, String endTime, String locationAddress) {
        this.clientName = clientName;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationAddress = locationAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

}
