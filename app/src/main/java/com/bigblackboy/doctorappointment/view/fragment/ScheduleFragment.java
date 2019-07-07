package com.bigblackboy.doctorappointment.view.fragment;

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

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.ScheduleApiResponse;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.presenter.ScheduleFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.ScheduleRecyclerViewAdapter;

import java.util.List;

public class ScheduleFragment extends Fragment implements MVPBaseInterface.View {

    private ScheduleRecyclerViewAdapter adapter;
    private RecyclerView rvSchedule;
    private ProgressBar progBarSchedule;
    private Doctor doctor;
    private String hospitalId;
    private RelativeLayout innerScheduleLayout;
    private TextView tvDoctorName;
    private ScheduleFragmentPresenter presenter;

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
        adapter = new ScheduleRecyclerViewAdapter(getContext());
        if (getArguments() != null) {
            if (getArguments().containsKey("doctor"))
                doctor = (Doctor) getArguments().getSerializable("doctor");
            if (getArguments().containsKey("hospitalId"))
                hospitalId = getArguments().getString("hospitalId");
        }
        presenter = new ScheduleFragmentPresenter(doctor);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        rvSchedule = v.findViewById(R.id.rvSchedule);
        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        progBarSchedule = v.findViewById(R.id.progBarSchedule);
        innerScheduleLayout = v.findViewById(R.id.innerScheduleLayout);
        tvDoctorName = v.findViewById(R.id.tvDoctorName);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSchedule(doctor.getIdDoc(), hospitalId, null, null, null);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        progBarSchedule.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarSchedule.setVisibility(View.INVISIBLE);
    }

    public void showInnerLayout() {
        innerScheduleLayout.setVisibility(View.VISIBLE);
    }

    public void hideInnerLayout() {
        innerScheduleLayout.setVisibility(View.INVISIBLE);
    }

    public void showSchedule(List<ScheduleApiResponse.MResponse> schedule) {
        adapter.setData(schedule);
        rvSchedule.setAdapter(adapter);
    }

    public void setDoctorName(String name) {
        tvDoctorName.setText(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
