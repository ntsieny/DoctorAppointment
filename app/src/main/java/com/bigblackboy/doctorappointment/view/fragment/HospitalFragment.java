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

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.model.hospitalmodel.District;
import com.bigblackboy.doctorappointment.model.hospitalmodel.Hospital;
import com.bigblackboy.doctorappointment.recyclerviewadapter.HospitalRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalFragment extends Fragment implements HospitalRecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: HospitalFragment";
    private static HospitalApi hospitalApi;

    private HospitalRecyclerViewAdapter adapter;
    private List<Hospital> hospitals;
    private RecyclerView recyclerView;
    private ProgressBar progBarHospital;
    private String districtId;
    private OnHospitalFragmentDataListener mListener;


    public interface OnHospitalFragmentDataListener {
        void onHospitalFragmentDataListener(Hospital hospital);
    }

    public static HospitalFragment newInstance(District district) {
        HospitalFragment hospitalFragment = new HospitalFragment();
        Bundle args = new Bundle();
        args.putString("district_id", district.getId());
        hospitalFragment.setArguments(args);
        return hospitalFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnHospitalFragmentDataListener) {
            mListener = (OnHospitalFragmentDataListener)context;
        } else throw new RuntimeException(context.toString() + " must implement OnHospitalFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        districtId = getArguments().getString("district_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hospital, null);
        progBarHospital = v.findViewById(R.id.progBarHospital);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = HospitalController.getApi();
        recyclerView = getView().findViewById(R.id.rvHospital);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new HospitalRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getHospitals();
    }

    private void getHospitals() {
        hospitalApi.getHospitals(districtId).enqueue(new Callback<HospitalApiResponse>() {
            @Override
            public void onResponse(Call<HospitalApiResponse> call, Response<HospitalApiResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        HospitalApiResponse res = response.body();
                        hospitals = res.getHospitals();
                        adapter.setData(hospitals);
                        recyclerView.setAdapter(adapter);
                        progBarHospital.setVisibility(View.INVISIBLE);
                    }
                } else Toast.makeText(getContext(), "Body is null. Code: " + response.code(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<HospitalApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onHospitalFragmentDataListener((Hospital)adapter.getItem(position));
    }
}
