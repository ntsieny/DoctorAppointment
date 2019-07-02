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
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.presenter.DistrictFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.RecyclerViewAdapter;

import java.util.HashMap;
import java.util.List;

public class DistrictFragment extends Fragment implements MVPBaseInterface.View, RecyclerViewAdapter.ItemClickListener {

    private DistrictFragmentPresenter presenter;
    private String LOG_TAG = "myLog: DistrictFragment";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private OnDistrictFragmentDataListener mListener;
    private ProgressBar progBarDistrict;

    public interface OnDistrictFragmentDataListener {
        void onDistrictFragmentDataListener(District district);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDistrictFragmentDataListener) {
            mListener = (OnDistrictFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnDistrictFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DistrictFragmentPresenter();
        presenter.attachView(this);

        adapter = new RecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_district, null);
        recyclerView = v.findViewById(R.id.rvDistrict);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progBarDistrict = v.findViewById(R.id.progBarDistrict);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressBar();
        presenter.getDistricts();
        hideProgressBar();
    }

    public void showDistricts(List<District> districts) {
        adapter.setData(districts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        String districtId = ((District)adapter.getItem(position)).getId();
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("district_id", districtId);
        mListener.onDistrictFragmentDataListener((District) adapter.getItem(position));
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
        progBarDistrict.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarDistrict.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
