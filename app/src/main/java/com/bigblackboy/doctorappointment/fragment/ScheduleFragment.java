package com.bigblackboy.doctorappointment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.ScheduleActivity;
import com.bigblackboy.doctorappointment.api.ScheduleApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.recyclerviewadapter.ScheduleRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends Fragment {

    private ScheduleRecyclerViewAdapter adapter;
    private RecyclerView rvSchedule;
    private ProgressBar progBarSchedule;
    private HospitalApi hospitalApi;
    private Doctor doctor;
    private String hospitalId;
    private RelativeLayout innerScheduleLayout;
    private TextView tvDoctorName;

    public static ScheduleFragment newInstance(Doctor doctor, String hospitalId) {
        ScheduleFragment frag = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putSerializable("doctor", doctor);
        args.putString("hospitalId", hospitalId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalApi = HospitalController.getApi();
        adapter = new ScheduleRecyclerViewAdapter(getContext());
        if (getArguments() != null) {
            if (getArguments().containsKey("doctor"))
                doctor = (Doctor) getArguments().getSerializable("doctor");
            if (getArguments().containsKey("hospitalId"))
                hospitalId = getArguments().getString("hospitalId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container);
        rvSchedule = v.findViewById(R.id.rvSchedule);
        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        progBarSchedule = v.findViewById(R.id.progBarSchedule);
        innerScheduleLayout = v.findViewById(R.id.innerScheduleLayout);
        tvDoctorName = v.findViewById(R.id.tvDoctorName);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDoctorName.setText(doctor.getName());
        getSchedule();
    }

    private void getSchedule() {
        hospitalApi.getSchedule(doctor.getIdDoc(), hospitalId, "", "", "").enqueue(new Callback<ScheduleApiResponse>() {
            @Override
            public void onResponse(Call<ScheduleApiResponse> call, Response<ScheduleApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResponse().size() <= 1) {
                        adapter.setData(response.body().getResponse().get(0));
                        rvSchedule.setAdapter(adapter);
                    } else {
                        List<ScheduleApiResponse.MResponse> schedule = new ArrayList();
                        schedule.addAll(response.body().getResponse().get(0));
                        for (int i = 1; i < response.body().getResponse().size(); i++) {
                            schedule.addAll(response.body().getResponse().get(i));
                        }
                        adapter.setData(schedule);
                        rvSchedule.setAdapter(adapter);
                    }
                    progBarSchedule.setVisibility(View.INVISIBLE);
                    innerScheduleLayout.setVisibility(View.VISIBLE);
                } else Toast.makeText(getContext(), response.body().getError().getErrorDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ScheduleApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
