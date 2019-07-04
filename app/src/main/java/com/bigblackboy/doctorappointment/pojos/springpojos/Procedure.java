package com.bigblackboy.doctorappointment.pojos.springpojos;

public class Procedure {

    public static final int FIRST_STEP_TYPE = 0;
    public static final int SECOND_STEP_TYPE = 1;
    private String name;
    private int type;

    public Procedure(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
