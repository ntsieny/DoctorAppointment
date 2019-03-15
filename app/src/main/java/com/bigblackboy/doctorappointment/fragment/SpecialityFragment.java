package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
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
import com.bigblackboy.doctorappointment.activity.OnDataPass;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.model.Patient;
import com.bigblackboy.doctorappointment.model.Speciality;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialityFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog";
    private static HospitalApi hospitalApi;
    RecyclerViewAdapter adapter;
    List<Speciality> specialities;
    RecyclerView recyclerView;
    private final String patientId = "037600000794946";
    private String hospitalId;
    Patient patient;
    private OnDataPass mDataPasser;
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
            hospitalId = dataHashMap.get("hospital_id");
        }
        return inflater.inflate(R.layout.fragment_speciality, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = Controller.getApi();
        recyclerView = getView().findViewById(R.id.rvSpeciality);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //checkPatient();
        getSpecialities();
    }




    private void getSpecialities() {
        hospitalApi.getSpecialities(hospitalId, "", "", patientId).enqueue(new Callback<SpecialitiesApiResponse>() {
            @Override
            public void onResponse(Call<SpecialitiesApiResponse> call, Response<SpecialitiesApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        SpecialitiesApiResponse respObj = response.body();
                        specialities = respObj.getSpecialities();
                        adapter.setData(specialities);
                        recyclerView.setAdapter(adapter);

                        /*for(Speciality speciality : respObj.getSpecialities()) {
                            Log.d(LOG_TAG, speciality.getNameSpeciality() + "\n" +
                                    speciality.getCountFreeParticipants() + "\n" + // доступно номерков
                                    speciality.getCountFreeTickets() + "\n" + // доступно номерков
                                    speciality.getIdSpeciality() + "\n"); // передается в form-data при выборе доктора
                        }*/
                    }
                    else Toast.makeText(getContext(), "Ошибка: " + response.body().getError().getErrorDescription(), Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(), "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<SpecialitiesApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getContext(), "You clicked " + ((Speciality)adapter.getItem(position)).getNameSpeciality() + " on row number " + position, Toast.LENGTH_SHORT).show();
        dataHashMap.put("speciality_id", String.valueOf(((Speciality)adapter.getItem(position)).getIdSpeciality()));
        mDataPasser.onDataPass(3, dataHashMap);
        Log.d(LOG_TAG, "SpecialityID: " + String.valueOf(((Speciality)adapter.getItem(position)).getIdSpeciality()));
    }
}
