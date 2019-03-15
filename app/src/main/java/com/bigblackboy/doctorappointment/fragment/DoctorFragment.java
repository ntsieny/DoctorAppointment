package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
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
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.model.Doctor;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog";
    private static HospitalApi hospitalApi;
    private OnDataPass mDataPasser;
    RecyclerViewAdapter adapter;
    List<Doctor> doctors;
    RecyclerView recyclerView;
    private String specialityId, hospitalId, patientId;
    private HashMap<String, String> dataHashMap;

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
            specialityId = dataHashMap.get("speciality_id");
            hospitalId = dataHashMap.get("hospital_id");
            patientId = dataHashMap.get("patient_id");
        }
        return inflater.inflate(R.layout.fragment_doctor, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = Controller.getApi();
        recyclerView = getView().findViewById(R.id.rvDoctor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDoctors();
    }

    private void getDoctors() {
        hospitalApi.getDoctors(specialityId, hospitalId, patientId, "").enqueue(new Callback<DoctorsApiResponse>() {
            @Override
            public void onResponse(Call<DoctorsApiResponse> call, Response<DoctorsApiResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess()) {
                        DoctorsApiResponse respObj = response.body();
                        doctors = respObj.getDoctors();
                        adapter.setData(doctors);
                        recyclerView.setAdapter(adapter);
                        /*Log.d(LOG_TAG, "--- СПИСОК ДОКТОРОВ ---\n");
                        for (Doctor doctor : respObj.getDoctors()) {
                            Log.d(LOG_TAG, doctor.getName() + " Участок: " + doctor.getAriaNumber() + " СНИЛС: " + doctor.getSnils() + "\n");
                            Log.d(LOG_TAG, doctor.getIdDoc()); // сохранить для form-data
                        }*/
                    }
                    else Toast.makeText(getContext(), "Ошибка: " + response.body().getError().getErrorDescription(), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getContext(), "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DoctorsApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + ((Doctor)adapter.getItem(position)).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
        dataHashMap.put("doctor_id", String.valueOf(((Doctor)adapter.getItem(position)).getIdDoc()));
        mDataPasser.onDataPass(4, dataHashMap);
        Log.d(LOG_TAG, "DoctorID: " + String.valueOf(((Doctor)adapter.getItem(position)).getIdDoc()));
    }
}
