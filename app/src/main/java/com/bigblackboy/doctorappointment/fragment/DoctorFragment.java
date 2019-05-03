package com.bigblackboy.doctorappointment.fragment;

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
import android.widget.Toast;

import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.recyclerviewadapter.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.api.DoctorsApiResponse;
import com.bigblackboy.doctorappointment.model.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: DoctorFragment";
    private static HospitalApi hospitalApi;
    RecyclerViewAdapter adapter;
    List<Doctor> doctors;
    RecyclerView recyclerView;
    private String specialityId, hospitalId, patientId;
    private OnDoctorFragmentDataListener mListener;

    public interface OnDoctorFragmentDataListener {
        void onDoctorFragmentDataListener(Doctor doctor);
    }

    public static DoctorFragment newInstance(String hospitalId, String patientId, String specialityId) {
        DoctorFragment doctorFragment = new DoctorFragment();
        Bundle args = new Bundle();
        args.putString("hospital_id", hospitalId);
        args.putString("patient_id", patientId);
        args.putString("speciality_id", specialityId);
        doctorFragment.setArguments(args);
        return doctorFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoctorFragmentDataListener) {
            mListener = (OnDoctorFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDoctorFragmentDataListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalId = getArguments().getString("hospital_id");
        patientId = getArguments().getString("patient_id");
        specialityId = getArguments().getString("speciality_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = HospitalController.getApi();
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
                    } else Toast.makeText(getContext(), "Ошибка: " + response.body().getError().getErrorDescription(), Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DoctorsApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onDoctorFragmentDataListener((Doctor)adapter.getItem(position));
    }
}
