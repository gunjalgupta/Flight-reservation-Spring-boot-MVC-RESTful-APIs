package com.example.demo.passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.net.URI;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Transactional
@RestController
@RequestMapping(path="/passenger")
public class PassengerController {
    @Autowired
    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }
    @PostMapping(value="", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" } )
    public ResponseEntity<?> addPassenger(@RequestParam Map<String, String> params, Passenger passenger) {
        System.out.println(params.keySet());
        System.out.println(params.values());
        System.out.println(passenger.getFirstname());
        HttpHeaders headers = new HttpHeaders();
        if(params.get("xml").startsWith("false")){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(params.get("xml").startsWith("true")){
            System.out.println(params.get("xml"));
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        try {
            Passenger persistedPerson = passengerService.addPassenger(passenger);
            return ResponseEntity.accepted().headers(headers).body(persistedPerson);
        }
        catch(Exception e) {
            Map<String, String> innerResponse = new HashMap<>();
            innerResponse.put("msg", "Duplicate Phone numbers not allowed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
        }
    }

    @PutMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" } )
    public ResponseEntity<?> updatePassenger(@RequestParam Map<String, String> params, Passenger passenger, @PathVariable("id") String id  ) {
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
            Passenger updatedPerson = passengerService.updatePassenger(passenger,id);
            return ResponseEntity.accepted().headers(headers).body(updatedPerson);
        }
        catch(Exception e) {
            Map<String, String> innerResponse = new HashMap<>();
            innerResponse.put("msg", "Duplicate Phone numbers not allowed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(innerResponse);
        }
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleSQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException exception
    ) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, Map<String, String>> response = new HashMap<>();
        Map<String, String> innerResponse = new HashMap<>();
        innerResponse.put("code", " 400");
        innerResponse.put("msg", " another passenger with the same number already exists");
        response.put("BadRequest", innerResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
    }


    @GetMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" })
    public ResponseEntity<?> getPassenger( @RequestParam(value = "xml") Boolean isXml, @PathVariable("id") String id ){

        Passenger persistedPerson = passengerService.getPassenger(id);
        HttpHeaders headers = new HttpHeaders();
        if(isXml.equals(false)){
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        else if(isXml.equals(true)){
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        if(persistedPerson!=null)
            return ResponseEntity.accepted().headers(headers).body(persistedPerson);
            else
            {
                try {
                    Map<String,Map <String,String>> response = new HashMap<>();
                    Map <String,String> innerResponse = new HashMap<>();
                    innerResponse.put("code", " 404");
                    innerResponse.put("msg", " Sorry, the requested passenger with ID :" + id + " does not exist");

                    response.put("BadRequest", innerResponse);
                    System.out.println("Inside not found");

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(response);
                }
                catch(Exception e){
                System.out.println("generateErrorMessage() catch");
            }
            }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }






//    @PostMapping(value ="/updatePassenger", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger, @RequestParam  String id ) {
//        System.out.println(passenger);
//        Passenger persistedPerson = passengerService.updatePassenger(passenger, id);
//        return ResponseEntity
//                .created(URI
//                        .create(String.format("/passengers/%s", passenger.getFirstname())))
//                .body(persistedPerson);
//    }

    @DeleteMapping(value ="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,"application/xml", "text/xml" } )
    public ResponseEntity<?> deletePassenger( @RequestParam(value = "xml") Boolean isXml, @PathVariable("id") String id ){
        String deletedMessage = passengerService.deletePassenger(id);
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
                innerResponse.put("msg", " Passenger with ID :" + id + " does not exist");
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
                innerResponse.put("msg", "  Passenger with id :" + id + " is deleted successfully ");
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
