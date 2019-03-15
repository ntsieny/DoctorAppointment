package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.Controller;
import com.bigblackboy.doctorappointment.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.activity.MainMenuActivity;
import com.bigblackboy.doctorappointment.activity.OnDataPass;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.model.Hospital;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog";
    private static HospitalApi hospitalApi;
    private OnDataPass mDataPasser;

    RecyclerViewAdapter adapter;
    List<Hospital> hospitals;
    RecyclerView recyclerView;
    private String districtId;
    private HashMap<String, String> dataHashMap;
    SharedPreferences mSettings;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null) {
            dataHashMap = (HashMap) this.getArguments().getSerializable("hashmap");
            districtId = dataHashMap.get("district_id");
        }
        return inflater.inflate(R.layout.fragment_hospital, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = Controller.getApi();
        recyclerView = getView().findViewById(R.id.rvHospital);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getHospitals();
    }

    private void getHospitals() {
        hospitalApi.getHospitals(districtId).enqueue(new Callback<HospitalApiResponse>() {
            @Override
            public void onResponse(Call<HospitalApiResponse> call, Response<HospitalApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        HospitalApiResponse res = response.body();
                        hospitals = res.getHospitals();
                        adapter.setData(hospitals);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Body is null. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HospitalApiResponse> call, Throwable t) {
                //Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getContext(), "You clicked " + ((Hospital)adapter.getItem(position)).getLpuName() + " on row number " + position, Toast.LENGTH_SHORT).show();
        String hospitalId = String.valueOf(((Hospital)adapter.getItem(position)).getIdLPU());
        dataHashMap.put("hospital_id", hospitalId);
        mDataPasser.onDataPass(2, dataHashMap);

        mSettings = this.getActivity().getSharedPreferences(MainMenuActivity.APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(MainMenuActivity.APP_SETTINGS_HOSPITAL_ID, hospitalId);
        editor.apply();

        Log.d(LOG_TAG, "HospitalID: " + hospitalId);
    }
}
