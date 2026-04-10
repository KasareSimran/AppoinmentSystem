package com.appointmentBooking.appointmentBooking.DTO;

public class AppointmentResponse {
    private Long appointmentId;
    private String userName;
    private String phone;
    private String slotTime;
    private String status;

    public AppointmentResponse() {}

    public AppointmentResponse(Long appointmentId, String userName, String phone, String slotTime, String status) {
        this.appointmentId = appointmentId;
        this.userName = userName;
        this.phone = phone;
        this.slotTime = slotTime;
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


