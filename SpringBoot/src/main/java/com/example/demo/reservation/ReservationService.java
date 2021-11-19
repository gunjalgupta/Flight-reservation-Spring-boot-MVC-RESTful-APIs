package com.example.demo.reservation;

import com.example.demo.flight.Flight;
import com.example.demo.flight.FlightRepository;
import com.example.demo.passenger.Passenger;
import com.example.demo.passenger.PassengerRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import javax.persistence.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@SqlResultSetMapping(name = "updateReservationDetails", columns = { @ColumnResult(name = "count") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "updateReservationDetails", query = "UPDATE reservation SET price = ?, origin = ?, destination=?, passenger = ?, reservation_number = ? WHERE id = ?", resultSetMapping = "updateResult") })

public class ReservationService {
	@Autowired // This means to get the bean called userRepository
	private ReservationRepository reservationRepository;
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private PassengerRepository passengerRepository;

	@GetMapping
	public @ResponseBody Reservation getReservation(String id) {
		return reservationRepository.findByReservationNumber(id);
	}

	public LocalDateTime findMin(Reservation reservationDetails) {

		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		List<LocalDateTime> flightTimings = new ArrayList<>();
		List<Flight> allFlights = reservationDetails.getFlights();
		for (int i = 0; i < allFlights.size(); i++) {
			LocalDateTime departureTime = LocalDateTime.parse(allFlights.get(i).getDepartureTime().substring(0, 22), f);
			flightTimings.add(departureTime);
		}
		return Collections.min(flightTimings);
	}

	public LocalDateTime findMinFlight(List<Flight> allFlights) {

		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		List<LocalDateTime> flightTimings = new ArrayList<>();
		for (int i = 0; i < allFlights.size(); i++) {
			LocalDateTime departureTime = LocalDateTime.parse(allFlights.get(i).getDepartureTime().substring(0, 22), f);
			flightTimings.add(departureTime);
		}
		return Collections.min(flightTimings);
	}

	public LocalDateTime findMaxFlight(List<Flight> allFlights) {

		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		List<LocalDateTime> flightTimings = new ArrayList<>();
		for (int i = 0; i < allFlights.size(); i++) {
			LocalDateTime departureTime = LocalDateTime.parse(allFlights.get(i).getArrivalTime().substring(0, 22), f);
			flightTimings.add(departureTime);
		}
		return Collections.max(flightTimings);
	}

	public LocalDateTime findMax(Reservation reservationDetails) {

		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		List<LocalDateTime> flightTimings = new ArrayList<>();
		List<Flight> allFlights = reservationDetails.getFlights();
		for (int i = 0; i < allFlights.size(); i++) {
			LocalDateTime arrivalTime = LocalDateTime.parse(allFlights.get(i).getArrivalTime().substring(0, 22), f);
			flightTimings.add(arrivalTime);
		}
		return Collections.max(flightTimings);
	}

	@PostMapping
	public Reservation addReservation(Reservation reservationDetails, List<String> flightList, String passengerId)
			throws Exception {
		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;
		List<List<LocalDateTime>> ls = new ArrayList<>();
		List<Flight> flightListDB = new ArrayList<>();
		System.out.println(passengerId);
		for (int i = 0; i < flightList.size(); i++) {
			Flight flightDetails = flightRepository.findByFlightNumber(flightList.get(i));
			flightListDB.add(flightDetails);
			LocalDateTime currentFlightArrivalDate = LocalDateTime
					.parse(flightDetails.getArrivalTime().substring(0, 22), f);
			LocalDateTime currentFlightDepartureDate = LocalDateTime
					.parse(flightDetails.getDepartureTime().substring(0, 22), f);
			List<LocalDateTime> arrayListNew = new ArrayList<>();

			arrayListNew.add(currentFlightDepartureDate);
			arrayListNew.add(currentFlightArrivalDate);
			ls.add(arrayListNew);
		}

		Collections.sort(ls, new Comparator<List<LocalDateTime>>() {
			@Override
			public int compare(List<LocalDateTime> arr1, List<LocalDateTime> arr2) {
				return arr1.get(0).compareTo(arr2.get(0));
			}
		});
		for (int i = 0; i < ls.size() - 1; i++) {
			// LocalDate currentFlightArrivalDate=ls.get(i).get(0);
			LocalDateTime currentFlightDepartureDate = ls.get(i).get(1);
			LocalDateTime currentFlight2ArrivalDate = ls.get(i + 1).get(0);
			// LocalDate currentFlight2DepartureDate=ls.get(i+1).get(1);
			if ((currentFlightDepartureDate.compareTo(currentFlight2ArrivalDate) >= 0)) {
				throw new Exception("Current flight times overlap.");
			}
		}

		Passenger pas = passengerRepository.findById(passengerId);
		List<Reservation> passengerReservations = passengerRepository.findById(passengerId).getReservations();

		List<List<LocalDateTime>> minMaxTimes = new ArrayList<>();

		LocalDateTime minTimeFlight = findMinFlight(flightListDB);
		LocalDateTime maxTimeFlight = findMaxFlight(flightListDB);

		for (int i = 0; i < passengerReservations.size(); i++) {
			LocalDateTime minTime = findMin(
					reservationRepository.findByReservationNumber(passengerReservations.get(i).getReservationNumber()));
			LocalDateTime maxTime = findMax(
					reservationRepository.findByReservationNumber(passengerReservations.get(i).getReservationNumber()));
			List<LocalDateTime> arrayListNew = new ArrayList<>();
			arrayListNew.add(minTime);
			arrayListNew.add(maxTime);
			minMaxTimes.add(arrayListNew);
		}

		Collections.sort(minMaxTimes, new Comparator<List<LocalDateTime>>() {
			@Override
			public int compare(List<LocalDateTime> arr1, List<LocalDateTime> arr2) {
				return arr1.get(0).compareTo(arr2.get(0));
			}
		});
		for (int i = 0; i < minMaxTimes.size(); i++) {

			LocalDateTime currentFlightDepartureDate = minMaxTimes.get(i).get(1);
			LocalDateTime currentFlight2ArrivalDate = minMaxTimes.get(i).get(0);

			if (!(currentFlightDepartureDate.compareTo(minTimeFlight) >= 0)
					&& (currentFlight2ArrivalDate.compareTo(minTimeFlight) >= 0)
					|| (currentFlightDepartureDate.compareTo(maxTimeFlight) <= 0)
							&& (currentFlight2ArrivalDate.compareTo(maxTimeFlight) <= 0)) {
				throw new Exception("Flight timings are overlapping with existing reservations");
			}
		}

		for (int i = 0; i < flightList.size(); i++) {
			Flight flightDetails = flightRepository.findByFlightNumber(flightList.get(i));
			if (flightDetails.getSeatsLeft() < 1) {
				throw new Exception("Seats are full");
			}
		}
		int totalPrice = 0;
		for (int i = 0; i < flightList.size(); i++) {
			Flight flightDetails = flightRepository.findByFlightNumber(flightList.get(i));
			flightDetails.setSeatsLeft(flightDetails.getSeatsLeft() - 1);
			totalPrice = totalPrice + flightDetails.getPrice();
		}
		reservationDetails.setPrice(totalPrice);
		Reservation newReservation = null;
		Passenger newPassenger = null;
		try {
			reservationDetails.setPassenger(passengerRepository.findById(passengerId));
			reservationDetails.setFlights(flightListDB);
			Passenger p = passengerRepository.findById(passengerId);

			newReservation = reservationRepository.save(reservationDetails);
			List<Reservation> l = p.getReservations();
			if (l.size() == 0) {
				l = new ArrayList<Reservation>();

			}
			l.add(newReservation);
			p.setReservations(l);
			newPassenger = passengerRepository.save(p);
		} catch (Exception ex) {
			System.out.println("Already exists");
			throw new Exception("constraint violation", ex);
		}
		// Passenger newPassenger2 = passengerRepository.findById(passengerId);
		// newPassenger2.setReservations(null);
		// newReservation.setPassenger(newPassenger2);
		return newReservation;
	}

	@PutMapping()
	public Reservation updateReservation(Reservation reservationDetails, String id, List<String> flightsAdded,
			List<String> flightsRemoved) throws Exception {
		Reservation existingReservation = null;

		if (flightsAdded.size() == 0 || flightsRemoved.size() == 0)
			throw new Exception("Flights Added/Removed list is empty.");

		existingReservation = reservationRepository.findByReservationNumber(id);
		if (existingReservation == null) {
			throw new Exception("No reservation with the given id.");
		}

		List<Flight> existingFlights = existingReservation.getFlights();

		for (int i = 0; i < flightsRemoved.size(); i++)
			existingFlights.remove(flightRepository.findByFlightNumber(flightsRemoved.get(i)));

		for (int i = 0; i < flightsAdded.size(); i++)
			existingFlights.add(flightRepository.findByFlightNumber(flightsAdded.get(i)));

		List<List<LocalDateTime>> ls = new ArrayList<>();
		DateTimeFormatter f = DateTimeFormatter.ISO_DATE_TIME;

		for (int i = 0; i < existingFlights.size(); i++) {
			LocalDateTime currentFlightArrivalDate = LocalDateTime
					.parse(existingFlights.get(i).getArrivalTime().substring(0, 22), f);
			LocalDateTime currentFlightDepartureDate = LocalDateTime
					.parse(existingFlights.get(i).getDepartureTime().substring(0, 22), f);
			List<LocalDateTime> arrayListNew = new ArrayList<>();

			arrayListNew.add(currentFlightDepartureDate);
			arrayListNew.add(currentFlightArrivalDate);
			ls.add(arrayListNew);
		}

		Collections.sort(ls, new Comparator<List<LocalDateTime>>() {
			@Override
			public int compare(List<LocalDateTime> arr1, List<LocalDateTime> arr2) {
				return arr1.get(0).compareTo(arr2.get(0));
			}
		});

		for (int i = 0; i < ls.size() - 1; i++) {
			LocalDateTime currentFlightDepartureDate = ls.get(i).get(1);
			LocalDateTime currentFlight2ArrivalDate = ls.get(i + 1).get(0);
			if ((currentFlightDepartureDate.compareTo(currentFlight2ArrivalDate) >= 0)) {
				throw new Exception("Flight times overlap.");
			}
		}

		int price = 0;

		for (int i = 0; i < existingFlights.size(); i++)
			price = price + existingFlights.get(i).getPrice();

		Collections.sort(existingFlights, new Comparator<Flight>() {
			@Override
			public int compare(Flight f1, Flight f2) {
				return f1.getDepartureTime().compareTo(f1.getDepartureTime());
			}
		});

		existingReservation.setDestination(existingFlights.get(existingFlights.size() - 1).getDestination());
		existingReservation.setOrigin(existingFlights.get(0).getOrigin());
		existingReservation.setPrice(price);

		return existingReservation;
	}

	@DeleteMapping()
	public @ResponseBody String deleteReservation(String id) throws Exception {
		Reservation existingReservation = reservationRepository.findByReservationNumber(id);
		if (existingReservation == null) {
			throw new Exception("Reservation doesn't exist");
		}
		for(int i=0;i<existingReservation.getFlights().size();i++)
		{
			Flight flight=existingReservation.getFlights().get(i);
			flight.setSeatsLeft(flight.getSeatsLeft()+1);
		}
		Passenger passenger=existingReservation.getPassenger();
		List<Reservation> resList=passenger.getReservations();
		for(int i=0;i<resList.size();i++)
		{
			Reservation res=resList.get(i);
			if(res.getReservationNumber()==existingReservation.getReservationNumber())
				resList.remove(existingReservation);
		}
		reservationRepository.deleteByReservationNumber(id);
		return "-1";
	}
}
