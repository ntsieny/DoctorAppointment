package com.bigblackboy.doctorappointment.model;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Session {

    private Patient patient;
    private Map cookies;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Map getCookies() {
        return cookies;
    }

    public void setCookies(String cookieString) {
        this.cookies = parseCookies(cookieString);
    }

    private Map<String, String> parseCookies(String cookieString) {
        StringTokenizer st;
        Map<String, String> cookies = new HashMap<>();
        String[] cookieItems = cookieString.split("=?; ");

        for (String item: cookieItems) {
            st = new StringTokenizer(item, "=");
            if (item.contains("=")) {
                while (st.hasMoreTokens()) {
                    String key = st.nextToken();
                    String value = st.nextToken();
                    cookies.put(key, value);
                }
            } else cookies.put(null, st.nextToken());
        }
        return cookies;
    }
}
