package com.appointmentBooking.appointmentBooking.Entity;


import com.appointmentBooking.appointmentBooking.Enum.AppointmentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //many appointment -one user
    @ManyToOne
    private User user;

    //one slot-one appointment---thats how we are preventing double booking
    @OneToOne
    private Slot slot;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public Appointment() {
    }

    public Appointment(Long id, User user, Slot slot, AppointmentStatus status) {
        this.id = id;
        this.user = user;
        this.slot = slot;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
