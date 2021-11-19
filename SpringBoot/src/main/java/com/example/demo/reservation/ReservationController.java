package com.example.demo.reservation;

import com.example.demo.passenger.Passenger;
import com.example.demo.passenger.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Transactional
@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {
	@Autowired
	private final ReservationService reservationService;
	@Autowired
	private final PassengerService passengerService;

	public ReservationController(ReservationService reservationService, PassengerService passengerService) {
		this.reservationService = reservationService;
		this.passengerService = passengerService;
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, "application/xml", "text/xml" })
	public ResponseEntity<?> getReservation(@RequestParam(value = "xml") Boolean isXml, @PathVariable("id") String id) {
		System.out.println(id);
		System.out.println(isXml);
		Reservation persistedReservation = reservationService.getReservation(id);
		HttpHeaders headers = new HttpHeaders();
		if (isXml.equals(false)) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		} else if (isXml.equals(true)) {
			headers.setContentType(MediaType.APPLICATION_XML);
		}
		if (persistedReservation != null)
			return ResponseEntity.accepted().headers(headers).body(persistedReservation);
		else {
			try {
				Map<String, Map<String, String>> response = new HashMap<>();
				Map<String, String> innerResponse = new HashMap<>();
				innerResponse.put("code", " 404");
				innerResponse.put("msg", " Sorry, the requested reservation with ID :" + id + " does not exist");

				response.put("BadRequest", innerResponse);
				System.out.println("Inside not found");

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
			} catch (Exception e) {
				System.out.println("generateErrorMessage() catch");
			}
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, "application/xml", "text/xml" })
	public ResponseEntity<?> addReservation(@RequestParam Map<String, String> params, Reservation reservation) {
		System.out.println(params.keySet());
		System.out.println(params.values());
		String passengerId = params.get("passengerId");
		Passenger passengerDetails = passengerService.getPassenger(passengerId);
		String[] flights = params.get("flightNumbers").split(",");
		List<String> flightlist = Arrays.asList(flights);
		HttpHeaders headers = new HttpHeaders();
		if (params.get("xml").startsWith("false")) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		} else if (params.get("xml").startsWith("true")) {
			System.out.println(params.get("xml"));
			headers.setContentType(MediaType.APPLICATION_XML);
		}
		if (passengerDetails != null) {
			try {
				Reservation persistedReservation = reservationService.addReservation(reservation, flightlist,
						passengerId);
				return ResponseEntity.accepted().headers(headers).body(persistedReservation);
			} catch (Exception e) {
				System.out.print("****************");
				System.out.print(e.getMessage());
				Map<String, String> innerResponse = new HashMap<>();
				innerResponse.put("code", "400");
				innerResponse.put("msg", e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
			}
		}
		return null;
	}

	@PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, "application/xml", "text/xml" })
	public ResponseEntity<?> updateReservation(@RequestParam Map<String, String> params, Reservation reservation,
			@PathVariable("id") String id) {

		String[] added = params.get("flightsAdded").split(",");
		String[] removed = params.get("flightsRemoved").split(",");

		List<String> flightsAdded = Arrays.asList(added);
		List<String> flightsRemoved = Arrays.asList(removed);

		HttpHeaders headers = new HttpHeaders();
		if (params.get("xml").startsWith("false")) {

			headers.setContentType(MediaType.APPLICATION_JSON);
		} else if (params.get("xml").startsWith("true")) {
			System.out.println(params.get("xml"));
			headers.setContentType(MediaType.APPLICATION_XML);
		}
		try {
			Reservation updatedReservation = reservationService.updateReservation(reservation, id, flightsAdded,
					flightsRemoved);
			return ResponseEntity.accepted().headers(headers).body(updatedReservation);
		} catch (Exception e) {
			Map<String, String> innerResponse = new HashMap<>();
			innerResponse.put("code", "400");
			innerResponse.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
		}
//        return null;

	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleSQLIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException exception) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, Map<String, String>> response = new HashMap<>();
		Map<String, String> innerResponse = new HashMap<>();
		innerResponse.put("code", " 400");
		innerResponse.put("msg", " another passenger with the same number already exists");
		response.put("BadRequest", innerResponse);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
	}

	@DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, "application/xml", "text/xml" })
	public ResponseEntity<?> deleteReservation(@RequestParam(value = "xml") Boolean isXml,
			@PathVariable("id") String id) {

		HttpHeaders headers = new HttpHeaders();
		if (isXml.equals(false)) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		} else if (isXml.equals(true)) {
			headers.setContentType(MediaType.APPLICATION_XML);
		}
			try{
				System.out.println("testing***********8");
				String deletedMessage = reservationService.deleteReservation(id);
				Map<String, String> innerResponse = new HashMap<>();
				innerResponse.put("code", "200");
				innerResponse.put("msg", "Cancelled Successfully");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
				}
			catch (Exception e) {
				Map<String, String> innerResponse = new HashMap<>();
				innerResponse.put("code", " 400");
				innerResponse.put("msg", e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
			}
	}
}
