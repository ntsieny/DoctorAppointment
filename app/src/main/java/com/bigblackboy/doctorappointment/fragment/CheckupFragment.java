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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.HealthInfo;

import org.joda.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckupFragment extends Fragment {

    private static final String LOG_TAG = "myLogs: checkupFrag";
    private int age;
    private SpringApi springApi;
    private RecyclerView rvFirstStep;
    private RecyclerView rvSecondStep;
    private TextView tvHint;
    private RecyclerViewAdapter adapter;

    public static CheckupFragment newInstance(int age) {
        CheckupFragment frag = new CheckupFragment();
        Bundle args = new Bundle();
        args.putInt("age", age);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkup, container, false);
        rvFirstStep = v.findViewById(R.id.rvFirstStep);
        rvSecondStep = v.findViewById(R.id.rvSecondStep);
        tvHint = v.findViewById(R.id.tvHint);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("age"))
            age = getArguments().getInt("age");
        springApi = SpringController.getApi();
        if (age < 40 && age % 3 == 0) {
            tvHint.setText(String.format("В этом году вы можете пройти бесплатную диспансеризацию."));
        } else if (age >= 40) {
            tvHint.setText(String.format("В этом году вы можете пройти бесплатную диспансеризацию."));
        } else {
            int nextAge = age;
            do { nextAge++; }
            while (nextAge % 3 != 0);
            tvHint.setText(String.format("Вы сможете пройти диспансеризацию в " + (new LocalDate().getYear() + (nextAge - age)) + " году."));
        }
        rvFirstStep.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFirstStep.getContext(), getResources().getConfiguration().orientation);
        rvFirstStep.addItemDecoration(dividerItemDecoration);
        rvSecondStep.setLayoutManager(new LinearLayoutManager(getContext()));
        dividerItemDecoration = new DividerItemDecoration(rvSecondStep.getContext(), getResources().getConfiguration().orientation);
        rvSecondStep.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getHealthInfo(age);
    }

    private void getHealthInfo(int age) {
        springApi.getHealthInfo(age).enqueue(new Callback<HealthInfo>() {
            @Override
            public void onResponse(Call<HealthInfo> call, Response<HealthInfo> response) {
                if (response.body() != null) {
                    HealthInfo info = response.body();
                    adapter.setData(info.getFirstStepProcedures());
                    rvFirstStep.setAdapter(adapter);
                    adapter.setData(info.getSecondStepProcedures());
                    rvSecondStep.setAdapter(adapter);
                } else Toast.makeText(getContext(), "Ошибка получения данных", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<HealthInfo> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
