package com.paulshantanu.lifesaver;

/**
 * Created by Shantanu Paul on 07-03-2016.
 */
public class User {

    String name;
    String address;
    String bloodgroup;
    String distance;
    String contactNumber;
    String email;


    public User(String name, String address,String bloodgroup, String distance){
        this.name = name;
        this.address = address;
        this.bloodgroup = bloodgroup;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
