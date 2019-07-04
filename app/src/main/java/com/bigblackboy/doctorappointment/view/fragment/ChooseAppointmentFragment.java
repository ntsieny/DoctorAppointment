package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;
import com.bigblackboy.doctorappointment.presenter.ChooseAppointmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.AppointmentDatesRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;

import java.util.List;

public class ChooseAppointmentFragment extends Fragment implements MVPBaseInterface.View, AppointmentDatesRecyclerViewAdapter.OnAppointmentDatesItemClickListener {

    private String LOG_TAG = "myLog: ChooseAppointmentFragment";
    private AppointmentDatesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String doctorId, patientId, hospitalId;
    private OnAppointmentFragmentDataListener mListener;
    private ProgressBar progBarChooseAppointment;
    private ChooseAppointmentPresenter presenter;

    @Override
    public void onAppointmentDatesItemClick(View view, int position) {
        mListener.onAppointmentFragmentDataListener((AppointmentInfo)adapter.getItem(position));
    }

    public interface OnAppointmentFragmentDataListener {
        void onAppointmentFragmentDataListener(AppointmentInfo appointmentInfo);
    }

    public static ChooseAppointmentFragment newInstance(String doctorId, String hospitalId, String patientId) {
        ChooseAppointmentFragment chooseAppointmentFragment = new ChooseAppointmentFragment();
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        args.putString("hospital_id", hospitalId);
        args.putString("patient_id", patientId);
        chooseAppointmentFragment.setArguments(args);
        return chooseAppointmentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppointmentFragmentDataListener) {
            mListener = (OnAppointmentFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnAppointmentFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId = getArguments().getString("doctor_id");
        hospitalId = getArguments().getString("hospital_id");
        patientId = getArguments().getString("patient_id");

        adapter = new AppointmentDatesRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);

        presenter = new ChooseAppointmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_appointment, null);
        recyclerView = v.findViewById(R.id.rvChooseAppointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        progBarChooseAppointment = v.findViewById(R.id.progBarChooseAppointment);
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
        presenter.getAppointmentDates(doctorId, hospitalId, patientId, null, null);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_choose_appointment);
    }

    public void showAppointmentDates(List<AppointmentInfo> appointmentDates) {
        adapter.setData(appointmentDates);
        recyclerView.setAdapter(adapter);
    }

    public void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
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
        progBarChooseAppointment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarChooseAppointment.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
