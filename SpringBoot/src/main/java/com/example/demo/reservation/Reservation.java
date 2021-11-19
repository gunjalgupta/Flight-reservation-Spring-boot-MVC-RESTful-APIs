package com.example.demo.reservation;

import com.example.demo.flight.Flight;
import com.example.demo.passenger.Passenger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
public class Reservation {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String reservationNumber; // primary key
    @OneToOne(targetEntity = Passenger.class, cascade=CascadeType.DETACH)
    @JsonIgnoreProperties({"reservations"})
    private Passenger passenger;     // Full form only
    private String origin;
    private String destination;
    private int price; // sum of each flight’s price.   // Full form only
    @ManyToMany(targetEntity=Flight.class)
    @JsonIgnoreProperties({"passengers"})
    private List<Flight> flights;    // Full form only

    public Reservation(String reservationNumber, Passenger passenger, String origin, String destination, int price, List<Flight> flights) {
        this.reservationNumber = reservationNumber;
        this.passenger = passenger;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.flights = flights;
    }

    public Reservation() {

    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
