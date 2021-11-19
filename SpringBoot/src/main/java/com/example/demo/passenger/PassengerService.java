package com.example.demo.passenger;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;
import javax.persistence.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

@Service
@SqlResultSetMapping(name="updatePassengerDetails", columns = { @ColumnResult(name = "count")})
@NamedNativeQueries({
        @NamedNativeQuery(
                name    =   "updatePassengerDetails",
                query   =   "UPDATE passenger SET firstname = ?, lastname = ?, phone=?, age = ?, gender = ? WHERE id = ?"
                ,resultSetMapping = "updateResult"
        )
})

public class PassengerService {
    @Autowired // This means to get the bean called userRepository
    private PassengerRepository passengerRepository;

    @GetMapping
    public @ResponseBody Passenger  getPassenger(String id){
        return passengerRepository.findById(id);
    }


    @PostMapping
    public Passenger addPassenger(Passenger passengerDetails) throws Exception {
        Passenger newPassenger = null;
        try {
            passengerDetails.setReservations(null);
            newPassenger = passengerRepository.save(passengerDetails);
        }
        catch(DataIntegrityViolationException ex){
            System.out.println("Already exists");
            throw new Exception("constraint violation", ex);
            }
        return newPassenger;
    }




    @PutMapping()
    public Passenger updatePassenger(Passenger passengerDetails, String id) throws Exception {
        Passenger existingPassenger = null;
        try{
            existingPassenger = passengerRepository.findById(id);
            existingPassenger.setFirstname(passengerDetails.getFirstname());
            existingPassenger.setLastname(passengerDetails.getLastname());
            existingPassenger.setAge(passengerDetails.getAge());
            existingPassenger.setGender(passengerDetails.getGender());
            existingPassenger.setPhone(passengerDetails.getPhone());
            passengerRepository.save(existingPassenger);
        }
        catch(DataIntegrityViolationException ex){
            System.out.println("Already exists");
            throw new Exception("constraint violation", ex);
        }
        return existingPassenger;
    }

    @DeleteMapping()
    public @ResponseBody String deletePassenger(String id) {
        Passenger existingPassenger = passengerRepository.findById(id);
        if(existingPassenger!=null){
            passengerRepository.deleteById(id);
            return "Deleted Successfully";
        }
        return "-1";
    }
}


