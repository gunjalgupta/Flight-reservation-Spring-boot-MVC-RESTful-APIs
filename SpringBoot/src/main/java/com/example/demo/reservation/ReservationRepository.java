package com.example.demo.reservation;

import com.example.demo.passenger.Passenger;
import com.example.demo.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Reservation findByReservationNumber(String id);
    void deleteByReservationNumber(String id);
    List<Reservation> findByPassenger(Passenger passenger);
}

