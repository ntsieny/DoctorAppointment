package com.bigblackboy.doctorappointment.springserver.springmodel;

import java.util.Collections;
import java.util.List;

public class HealthInfo {

    public HealthInfo() {

    }

    public HealthInfo(int userAge) {
        if (userAge >= 18 && userAge <= 39) {
            fromAge = 18;
            toAge = 39;
        } else if (userAge >= 40 && userAge <= 45) {
            fromAge = 40;
            toAge = 45;
        } else if (userAge >= 46 && userAge <= 50) {
            fromAge = 46;
            toAge = 50;
        } else if (userAge >= 51 && userAge <= 74) {
            fromAge = 51;
            toAge = 74;
        } else if (userAge >= 75) {
            fromAge = 75;
        }
    }

    private List<String> firstStepProcedures;
    private List<String> secondStepProcedures;
    private int fromAge;
    private int toAge;

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    public List<String> getFirstStepProcedures() {
        return firstStepProcedures;
    }

    public void setFirstStepProcedures(List<String> firstStepProcedures) {
        this.firstStepProcedures = firstStepProcedures;
    }

    public List<String> getSecondStepProcedures() {
        return secondStepProcedures;
    }

    public void setSecondStepProcedures(List<String> secondStepProcedures) {
        this.secondStepProcedures = secondStepProcedures;
    }
}
