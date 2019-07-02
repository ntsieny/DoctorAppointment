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
import com.bigblackboy.doctorappointment.model.SpecialityModel;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.presenter.SpecialityFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;

import java.util.List;

public class SpecialityFragment extends Fragment implements MVPBaseInterface.View, RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: SpecialityFragment";
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String patientId;
    private String hospitalId;
    private OnSpecialityFragmentDataListener mListener;
    private ProgressBar progBarSpeciality;
    private SpecialityFragmentPresenter presenter;

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

        adapter = new RecyclerViewAdapter(getContext());
        adapter.setClickListener(this);

        SpecialityModel specialityModel = new SpecialityModel();
        presenter = new SpecialityFragmentPresenter(specialityModel);
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speciality, null);
        recyclerView = v.findViewById(R.id.rvSpeciality);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progBarSpeciality = v.findViewById(R.id.progBarSpeciality);
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
        presenter.getSpecialities(hospitalId, null, null, patientId);
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_choose_speciality);
    }

    public void showSpecialities(List<Speciality> specialities) {
        adapter.setData(specialities);
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
        progBarSpeciality.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarSpeciality.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onSpecialityFragmentDataListener((Speciality)adapter.getItem(position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
