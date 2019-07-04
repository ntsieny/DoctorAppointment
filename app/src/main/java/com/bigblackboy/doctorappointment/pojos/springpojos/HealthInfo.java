package com.bigblackboy.doctorappointment.pojos.springpojos;

import java.util.ArrayList;
import java.util.List;

public class HealthInfo {

    private List<String> firstStepProcedures;
    private List<String> secondStepProcedures;
    private List<Procedure> procedures;
    private int fromAge;
    private int toAge;

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

    public List<Procedure> getProcedures() {
        procedures = new ArrayList<>();
        for (String item : firstStepProcedures) {
            procedures.add(new Procedure(item, Procedure.FIRST_STEP_TYPE));
        }
        for (String item : secondStepProcedures) {
            procedures.add(new Procedure(item, Procedure.SECOND_STEP_TYPE));
        }
        return procedures;
    }

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
