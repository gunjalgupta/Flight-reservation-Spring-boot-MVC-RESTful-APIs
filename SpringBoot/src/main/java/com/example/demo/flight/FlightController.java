package com.example.demo.flight;

import com.example.demo.passenger.Passenger;
import com.example.demo.passenger.PassengerService;
import com.example.demo.plane.Plane;
import com.example.demo.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;


@Transactional
@RestController
public class FlightController {

    @Autowired
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    
    @RequestMapping(value="/flight/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" },method=RequestMethod.GET )
    public ResponseEntity<?> getPassenger( @RequestParam(value = "xml") Boolean isXml, @PathVariable("id") String id ){

        
        HttpHeaders headers = new HttpHeaders();
        if(isXml.equals(false)){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(isXml.equals(true)){
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        
        try {
        	Flight persistedFlight=flightService.getFlight(id);
        	return ResponseEntity.accepted().headers(headers).body(persistedFlight);
        }
        catch(Exception e) {
            e.printStackTrace();
            Map<String, String> innerResponse = new HashMap<>();
            innerResponse.put("msg", "Duplicate Phone numbers not allowed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
        }
    }


    @RequestMapping(value="flight/{flightNumber}",  produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" },method=RequestMethod.POST )
    public ResponseEntity<?> addFlight(@RequestParam Map<String, String> params, Flight flight,  @PathVariable("flightNumber") String flightNumber) {
        System.out.println(params.keySet());
        System.out.println(params.values());
        HttpHeaders headers = new HttpHeaders();
        if(params.get("xml").startsWith("false")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(params.get("xml").startsWith("true")){
            System.out.println(params.get("xml"));
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        try {
            Plane plane = new Plane(Integer.parseInt(params.get("capacity")),params.get("model"),params.get("manufacturer"),Integer.parseInt(params.get("yearOfManufacture")));
            System.out.println(flight);
            Flight persistedFlight = flightService.addFlight(flight,plane,flightNumber);
            return ResponseEntity.accepted().headers(headers).body(persistedFlight);
        }
        catch(Exception e) {
            e.printStackTrace();
            Map<String, String> innerResponse = new HashMap<>();
            innerResponse.put("msg", "Duplicate Phone numbers not allowed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
        }
    }

    @RequestMapping(value ="/airlines/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" },method=RequestMethod.DELETE  )
    public ResponseEntity<?> deleteFlight( @RequestParam(value = "xml") Boolean isXml, @PathVariable("id") String id ){
        String deletedMessage = flightService.deleteFlight(id);
        System.out.print(deletedMessage);
        HttpHeaders headers = new HttpHeaders();
        if(isXml.equals(false)){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(isXml.equals(true)){
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        if(deletedMessage=="-1")
            try {
                Map<String,Map <String,String>> response = new HashMap<>();
                Map <String,String> innerResponse = new HashMap<>();
                innerResponse.put("code", " 404");
                innerResponse.put("msg", "There are existing reservations");
                response.put("BadRequest", innerResponse);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
            }
            catch(Exception e) {
                System.out.println("generateErrorMessage() catch");
            }
        else if(deletedMessage=="-2")
            try {
                Map<String,Map <String,String>> response = new HashMap<>();
                Map <String,String> innerResponse = new HashMap<>();
                innerResponse.put("code", " 400");
                innerResponse.put("msg", "Flight not found");
                response.put("BadRequest", innerResponse);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
            }
            catch(Exception e) {
                System.out.println("generateErrorMessage() catch");
            }
        else
        {
            try {
                Map<String,Map <String,String>> response = new HashMap<>();
                Map <String,String> innerResponse = new HashMap<>();
                innerResponse.put("code", " 200");
                innerResponse.put("msg", "  Flight  with number :" + id + " is deleted successfully ");
                response.put("Response", innerResponse);
                System.out.println("Inside not found");
                return ResponseEntity.status(HttpStatus.ACCEPTED).headers(headers).body(response);
            }
            catch(Exception e){
                System.out.println("generateErrorMessage() catch");
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
