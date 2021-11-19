//package com.example.demo.plane;
//import javax.persistence.Embeddable;
//
//
//
//@Embeddable
//public class Plane {
//    //    @Id
////    private String number;
//    private String model;
//    private int capacity;
//    private String manufacturer;
//    private int yearOfManufacture;
//
//    public Plane(String model, int capacity, String manufacturer, int yearOfManufacture) {
//        this.model = model;
//        this.capacity = capacity;
//        this.manufacturer = manufacturer;
//        this.yearOfManufacture = yearOfManufacture;
//    }
//
//    public Plane() {
//
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    public String getManufacturer() {
//        return manufacturer;
//    }
//
//    public int getYearOfManufacture() {
//        return yearOfManufacture;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public void setManufacturer(String manufacturer) {
//        this.manufacturer = manufacturer;
//    }
//
//    public void setYearOfManufacture(int yearOfManufacture) {
//        this.yearOfManufacture = yearOfManufacture;
//    }
//}
package com.example.demo.plane;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * The Plane program implements the Plane Entity that
 * makes schema and dependency for Plane.
 *
 * @author  Sparsh Sidana
 * @version 1.0
 * @since   2017-04-18
 */


@Entity
public class Plane {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private int capacity;
    private String model;
    private String manufacturer;
    private int yearOfManufacture;

    public Plane(){

    }

    public Plane(int capacity, String model,
                 String manufacturer, int yearOfManufacture){
        this.capacity = capacity;
        this.model = model;
        this.manufacturer = manufacturer;
        this.yearOfManufacture = yearOfManufacture;
    }


    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public int getYearOfManufacture() {
        return yearOfManufacture;
    }
    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
}
