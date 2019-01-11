package com.bigblackboy.doctorappointment.model;

import java.util.ArrayList;

public class District {

    private String name;
    private String id;
    private ArrayList<Hospital> hospitals;

    public District() {

    }

    public District(String name, ArrayList<Hospital> hospitals) {
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
}
