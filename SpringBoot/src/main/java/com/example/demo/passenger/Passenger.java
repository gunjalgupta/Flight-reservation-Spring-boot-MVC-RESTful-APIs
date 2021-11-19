package com.example.demo.passenger;
import com.example.demo.flight.Flight;
import com.example.demo.reservation.Reservation;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name="Passenger")
@EntityScan("com.server.models")
public class Passenger {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String firstname;
    private String lastname;
    private int age;  // Full form only (see definition below)
    private String gender;  // Full form only
    @Column(unique=true)
    private String phone; // Phone numbers must be unique.   Full form only
    @OneToMany(targetEntity= Reservation.class,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"passenger"})
    private List<Reservation> reservations;

    public Passenger() {

    }

    public Passenger(String id, String firstname, String lastname, int age, String gender, String phone, List<Reservation> reservations) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.reservations = reservations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}