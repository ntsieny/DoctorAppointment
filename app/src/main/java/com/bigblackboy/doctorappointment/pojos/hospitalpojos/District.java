package com.bigblackboy.doctorappointment.pojos.hospitalpojos;

import java.io.Serializable;
import java.util.ArrayList;

public class District implements Serializable {

    private String id;
    private String name;
    private ArrayList<Hospital> hospitals;

    public District() {

    }

    public District(String id, String name, ArrayList<Hospital> hospitals) {
        this.id = id;
        this.name = name;
        this.hospitals = hospitals;
    }

    public ArrayList<Hospital> getHospitals() {
        return hospitals;
    }
    public void setHospitals(ArrayList<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
