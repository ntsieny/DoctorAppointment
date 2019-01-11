package com.bigblackboy.doctorappointment.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.Controller;
import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.HtmlParser;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivityLOG";
    private static HospitalApi hospitalApi;
    List<Hospital> hospitals;
    Button createAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hospitalApi = Controller.getApi();
        hospitals = new ArrayList<>();
        createAppBtn = findViewById(R.id.createAppBtn);
        createAppBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAppBtn:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                //getHospitals();
                //new AsyncRequest().execute();
                break;
        }
    }

    // список больниц
    private void getHospitals() {
        hospitalApi.getHospitals("1").enqueue(new Callback<HospitalApiResponse>() {
            @Override
            public void onResponse(Call<HospitalApiResponse> call, Response<HospitalApiResponse> response) {
                if(response.body() != null) {
                    HospitalApiResponse res = response.body();
                    hospitals = res.getHospitals();
                    Log.d(LOG_TAG, "--- СПИСОК БОЛЬНИЦ ---\n");
                    for (Hospital hospital : hospitals) {
                        Log.d(LOG_TAG, hospital.toString() + "\n");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Body is null", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<HospitalApiResponse> call, Throwable t) {
                Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    //new AsyncRequest().execute(); - список районов
    private class AsyncRequest extends AsyncTask<Void, Void, ArrayList<District>> {
        @Override
        protected ArrayList<District> doInBackground(Void... voids) {
            return new HtmlParser().getDistricts();
        }
        @Override
        protected void onPostExecute(ArrayList<District> districts) {
            Log.d(LOG_TAG, "--- СПИСОК РАЙОНОВ ---\n");
            for (District district : districts) {
                Log.d(LOG_TAG, district.getId() + " - " + district.getName() + "\n");
            }
        }
    }
}
