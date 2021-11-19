package com.example.demo.flight;

import com.example.demo.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
     Flight findByFlightNumber(String flightNumber);
     void deleteByFlightNumber(String flightNumber);
}

