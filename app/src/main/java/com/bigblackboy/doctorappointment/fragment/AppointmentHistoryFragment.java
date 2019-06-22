package com.bigblackboy.doctorappointment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.MainActivity;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.AppointmentHistoryRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.Appointment;

import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentHistoryFragment extends Fragment implements AppointmentHistoryRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: AppHistFragment";
    private static SpringApi springApi;
    private RecyclerView recyclerView;
    private AppointmentHistoryRecyclerViewAdapter adapter;
    private String serviceId;
    private List<Appointment> appointments;
    private ProgressBar progBarAppointmentHistory;

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
        serviceId = getArguments().getString("service_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment_history, null);
        progBarAppointmentHistory = v.findViewById(R.id.progBarAppointmentHistory);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        springApi = SpringController.getApi();
        recyclerView = getView().findViewById(R.id.rvAppointmentHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(recyclerView);
        adapter = new AppointmentHistoryRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAppointments();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_appointment_history);
    }

    @Override
    public void onItemClick(int viewId, int position) {
        Appointment app = adapter.getItem(position);
        switch (viewId) {
            case R.id.removeAppHistItem:
                deleteAppointment(app.getAppId());
                break;
        }
    }

    private void getAppointments() {
        springApi.getAppointments(serviceId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        appointments = response.body();
                        Collections.sort(appointments);
                        Collections.reverse(appointments);
                        adapter.setData(appointments);
                        recyclerView.setAdapter(adapter);
                        progBarAppointmentHistory.setVisibility(View.INVISIBLE);
                    } else Toast.makeText(getContext(), "Записей не обнаружено", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void deleteAppointment(int appointmentId) {
        springApi.deleteAppointment(appointmentId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if(response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Запись отменена", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Запись не отменена", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
