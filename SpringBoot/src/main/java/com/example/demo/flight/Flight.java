//package com.example.demo.flight;
//import com.example.demo.passenger.Passenger;
//import com.example.demo.plane.Plane;
//import javax.persistence.*;
//import java.util.*;
//
//@Entity
//public class Flight {
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String flightNumber; // Primary key
//    private int price;    // Full form only
//    private String origin;
//    private String destination;
//    /*  Date format: yy-mm-dd-hh, do not include minutes and seconds.
//     ** Example: 2017-03-22-19
//     **The system only needs to support PST. You can ignore other time zones.
//     */
//    private Date departureTime;
//    private Date arrivalTime;
//    private int seatsLeft;    // Full form only
//    private String description;   // Full form only
//    @Embedded
////    @OneToOne(targetEntity=Plane.class, cascade=CascadeType.ALL)
//    private Plane plane;  // Embedded,    Full form onl
//    @ManyToMany(targetEntity=Passenger.class)
//    @Column(name="passengerList")
//    private List<Passenger> passengers;    // Full form only
//
//    public Flight(String flightNumber, int price, String origin, String destination, Date departureTime, Date arrivalTime, int seatsLeft, String description, Plane plane, List<Passenger> passengers) {
//        this.flightNumber = flightNumber;
//        this.price = price;
//        this.origin = origin;
//        this.destination = destination;
//        this.departureTime = departureTime;
//        this.arrivalTime = arrivalTime;
//        this.seatsLeft = seatsLeft;
//        this.description = description;
//        this.plane = plane;
//        this.passengers = passengers;
//    }
//
//    public Flight() {
//
//    }
//
//    public String getFlightNumber() {
//        return flightNumber;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public String getOrigin() {
//        return origin;
//    }
//
//    public String getDestination() {
//        return destination;
//    }
//
//    public Date getDepartureTime() {
//        return departureTime;
//    }
//
//    public Date getArrivalTime() {
//        return arrivalTime;
//    }
//
//    public int getSeatsLeft() {
//        return seatsLeft;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public Plane getPlane() {
//        return plane;
//    }
//
//    public List<Passenger> getPassengers() {
//        return passengers;
//    }
//
//    public void setFlightNumber(String flightNumber) {
//        this.flightNumber = flightNumber;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public void setOrigin(String origin) {
//        this.origin = origin;
//    }
//
//    public void setDestination(String destination) {
//        this.destination = destination;
//    }
//
//    public void setDepartureTime(Date departureTime) {
//        this.departureTime = departureTime;
//    }
//
//    public void setArrivalTime(Date arrivalTime) {
//        this.arrivalTime = arrivalTime;
//    }
//
//    public void setSeatsLeft(int seatsLeft) {
//        this.seatsLeft = seatsLeft;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setPlane(Plane plane) {
//        this.plane = plane;
//    }
//
//    public void setPassengers(List<Passenger> passengers) {
//        this.passengers = passengers;
//    }
//}
package com.example.demo.flight;
import com.example.demo.passenger.Passenger;
import com.example.demo.plane.Plane;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import javax.persistence.*;


@Entity
public class Flight {

    @Id
    private String flightNumber;
    private int price;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private int seatsLeft;
    private String description;
//    @OneToOne(targetEntity= Plane.class, cascade=CascadeType.ALL)
    @OneToOne(targetEntity=Plane.class, cascade=CascadeType.ALL)
    private Plane plane;
    @ManyToMany(targetEntity= Passenger.class)
    @Column(name = "passlist")
    @JsonIgnoreProperties({"reservations"})
    private List<Passenger> passengers;

    public Flight(){

    }

    public Flight(String flightNumber, int price, String origin,
                  String destination, String departureTime, String arrivalTime,
                  String description, int seatsLeft, Plane plane){
        this.flightNumber = flightNumber;
        this.price = price;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.description = description;
        this.plane = plane;
        this.seatsLeft = seatsLeft;
//        this.passengers = passengers;
    }

    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
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

    public void setDestination(String toDestination) {
        this.destination = toDestination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

//    public List<Passenger> getPassenger() {
//        return passengers;
//    }
//
//    public void setPassenger(List<Passenger> passengers) {
//        this.passengers = passengers;
//    }
}
