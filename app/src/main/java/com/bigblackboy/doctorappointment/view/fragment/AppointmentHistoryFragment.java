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
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.springpojos.Appointment;
import com.bigblackboy.doctorappointment.presenter.AppointmentHistoryPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.AppointmentHistoryRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;

import java.util.List;

public class AppointmentHistoryFragment extends Fragment implements MVPBaseInterface.View, AppointmentHistoryRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: AppHistFragment";
    private RecyclerView recyclerView;
    private AppointmentHistoryRecyclerViewAdapter adapter;
    private String serviceId;
    private ProgressBar progBarAppointmentHistory;
    private AppointmentHistoryPresenter presenter;

    public static AppointmentHistoryFragment newInstance(String serviceId) {
        AppointmentHistoryFragment fragment = new AppointmentHistoryFragment();
        Bundle args = new Bundle();
        args.putString("service_id", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey("service_id"))
            serviceId = getArguments().getString("service_id");

        adapter = new AppointmentHistoryRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);

        presenter = new AppointmentHistoryPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment_history, null);
        recyclerView = v.findViewById(R.id.rvAppointmentHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(recyclerView);
        progBarAppointmentHistory = v.findViewById(R.id.progBarAppointmentHistory);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter.viewIsReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getUserAppointments(serviceId);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_appointment_history);
    }

    @Override
    public void onItemClick(int viewId, int position) {
        Appointment app = adapter.getItem(position);
        switch (viewId) {
            case R.id.removeAppHistItem:
                presenter.deleteAppointment(app.getAppId());
                break;
        }
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
        progBarAppointmentHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarAppointmentHistory.setVisibility(View.INVISIBLE);
    }

    public void showAppointments(List<Appointment> appointments) {
        adapter.setData(appointments);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
