package com.example.demo.passenger;

import com.example.demo.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {
    Passenger findById(String id);
    void deleteById(String id);
}

