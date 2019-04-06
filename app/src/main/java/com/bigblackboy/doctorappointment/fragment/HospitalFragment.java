package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.bigblackboy.doctorappointment.api.HospitalApiResponse;
import com.bigblackboy.doctorappointment.model.District;
import com.bigblackboy.doctorappointment.model.Hospital;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_HOSPITAL_ID;

public class HospitalFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: HospitalFragment";
    private static HospitalApi hospitalApi;

    RecyclerViewAdapter adapter;
    List<Hospital> hospitals;
    RecyclerView recyclerView;
    private String districtId;
    private HashMap<String, String> dataHashMap;
    SharedPreferences mSettings;
    private OnHospitalFragmentDataListener mListener;
    private String barTitle = "Выбор медучреждения";


    public interface OnHospitalFragmentDataListener {
        void onHospitalFragmentDataListener(Hospital hospital);

        void onHospitalUpdateActionBarTitle(String barTitle);
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
        //((MainMenuActivity)context).districtToFragment = this;
        if(context instanceof OnHospitalFragmentDataListener) {
            mListener = (OnHospitalFragmentDataListener)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnHospitalFragmentDataListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        districtId = getArguments().getString("district_id");
        /*if(getActivity() instanceof MainMenuActivity) {
            ((MainMenuActivity) getActivity()).setDistrictToFragmentListener(this);
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListener.onHospitalUpdateActionBarTitle(barTitle);
        return inflater.inflate(R.layout.fragment_hospital, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = Controller.getApi();
        recyclerView = getView().findViewById(R.id.rvHospital);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext());
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
                    }
                } else {
                    Toast.makeText(getContext(), "Body is null. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HospitalApiResponse> call, Throwable t) {
                //Log.d(LOG_TAG, t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getContext(), "You clicked " + ((Hospital)adapter.getItem(position)).getLpuName() + " on row number " + position, Toast.LENGTH_SHORT).show();
        int hospitalId = ((Hospital)adapter.getItem(position)).getIdLPU();
        //dataHashMap.put("hospital_id", hospitalId);
        //mDataPasser.onDataPass(2, dataHashMap);

        mListener.onHospitalFragmentDataListener((Hospital)adapter.getItem(position));

        mSettings = this.getActivity().getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_SETTINGS_HOSPITAL_ID, hospitalId);
        editor.apply();

        Log.d(LOG_TAG, "HospitalID: " + hospitalId);
    }

    /*@Override
    public void districtToFragment(District district) {
        //districtId = district.getId();
        Toast.makeText(getContext(), districtId + " хаха", Toast.LENGTH_SHORT).show();
    }*/
}
