package com.bigblackboy.doctorappointment;

import android.util.Log;

import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HtmlParser {

    private static String districts_url = "http://www.kuban-online.ru/signup/free";

    public ArrayList<District> getDistricts() {
        ArrayList<District> districts = new ArrayList<>();
        District district;

        try {
            Document doc = Jsoup.connect(districts_url).get();
            Element district_list = doc.getElementById("district_list");

            Elements names = district_list.select("[data-id]").not("[class]");

            for (int i = 0; i < names.size(); i++) {
                district = new District();
                district.setId(names.get(i).attr("data-id"));
                district.setName(names.get(i).ownText());
                districts.add(district);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return districts;
    }
}
