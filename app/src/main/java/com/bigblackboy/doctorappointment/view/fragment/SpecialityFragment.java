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

import com.bigblackboy.doctorappointment.view.activity.MainActivity;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.recyclerviewadapter.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.api.SpecialitiesApiResponse;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialityFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: SpecialityFragment";
    private static HospitalApi hospitalApi;
    RecyclerViewAdapter adapter;
    List<Speciality> specialities;
    RecyclerView recyclerView;
    private String patientId;
    private String hospitalId;
    OnSpecialityFragmentDataListener mListener;
    ProgressBar progBarSpeciality;

    public interface OnSpecialityFragmentDataListener {
        void onSpecialityFragmentDataListener(Speciality speciality);
    }

    public static SpecialityFragment newInstance(String hospitalId, String patientId) {
        SpecialityFragment specialityFragment = new SpecialityFragment();
        Bundle args = new Bundle();
        args.putString("hospital_id", hospitalId);
        args.putString("patient_id", patientId);
        specialityFragment.setArguments(args);
        return specialityFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSpecialityFragmentDataListener) {
            mListener = (OnSpecialityFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnSpecialityFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalId = getArguments().getString("hospital_id");
        patientId = getArguments().getString("patient_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speciality, null);
        progBarSpeciality = v.findViewById(R.id.progBarSpeciality);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = HospitalController.getApi();
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
        getSpecialities();
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_choose_speciality);
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
                    } else {
                        Toast.makeText(getContext(), "Ошибка: " + response.body().getError().getErrorDescription(), Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else Toast.makeText(getContext(), "Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
                progBarSpeciality.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<SpecialitiesApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onSpecialityFragmentDataListener((Speciality)adapter.getItem(position));
    }
}
