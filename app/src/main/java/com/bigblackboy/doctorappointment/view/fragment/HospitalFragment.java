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
import com.bigblackboy.doctorappointment.model.HospitalModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.presenter.HospitalFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.HospitalRecyclerViewAdapter;

import java.util.List;

public class HospitalFragment extends Fragment implements HospitalRecyclerViewAdapter.ItemClickListener, MVPBaseInterface.View {

    private String LOG_TAG = "myLog: HospitalFragment";
    private HospitalRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progBarHospital;
    private String districtId;
    private OnHospitalFragmentDataListener mListener;
    private HospitalFragmentPresenter presenter;

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

        adapter = new HospitalRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);

        HospitalModel hospitalModel = new HospitalModel();
        presenter = new HospitalFragmentPresenter(hospitalModel);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hospital, null);
        recyclerView = v.findViewById(R.id.rvHospital);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progBarHospital = v.findViewById(R.id.progBarHospital);
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
        presenter.getHospitals(districtId);
    }

    public void showHospitals(List<Hospital> hospitals) {
        adapter.setData(hospitals);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onHospitalFragmentDataListener((Hospital)adapter.getItem(position));
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
        progBarHospital.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarHospital.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
