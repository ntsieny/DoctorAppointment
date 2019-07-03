package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.presenter.DoctorFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.DoctorRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;
import com.bigblackboy.doctorappointment.view.activity.ReviewActivity;

import java.util.List;

public class DoctorFragment extends Fragment implements MVPBaseInterface.View, DoctorRecyclerViewAdapter.OnDoctorItemClickListener {

    private String LOG_TAG = "myLog: DoctorFragment";
    private DoctorRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private String specialityId, hospitalId, patientId;
    private OnDoctorFragmentDataListener mListener;
    private ProgressBar progBarDoctor;
    private DoctorFragmentPresenter presenter;

    @Override
    public void onDoctorPopupMenuItemClick(MenuItem item, int position) {
        switch (item.getItemId()) {
            case R.id.doctorReviewsPopupItem:
                Intent reviewsIntent = new Intent(getContext(), ReviewActivity.class);
                Bundle args = new Bundle();
                args.putInt("fragToLoad", ReviewActivity.FRAGMENT_DOCTOR_REVIEWS);
                args.putString("doctorId", adapter.getItem(position).getIdDoc());
                args.putString("doctorName", adapter.getItem(position).getName());
                args.putString("specialityId", ((MainActivity)getActivity()).getSpeciality().getIdSpeciality());
                args.putString("specialityName", ((MainActivity)getActivity()).getSpeciality().getNameSpeciality());
                reviewsIntent.putExtras(args);
                startActivity(reviewsIntent);
                break;
        }
    }

    @Override
    public void onDoctorItemClick(int position) {
        mListener.onDoctorFragmentDataListener((Doctor)adapter.getItem(position));
    }

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
        } else throw new RuntimeException(context.toString() + " must implement OnDoctorFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hospitalId = getArguments().getString("hospital_id");
        patientId = getArguments().getString("patient_id");
        specialityId = getArguments().getString("speciality_id");

        adapter = new DoctorRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);

        presenter = new DoctorFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor, null);
        recyclerView = v.findViewById(R.id.rvDoctor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        progBarDoctor = v.findViewById(R.id.progBarDoctor);
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
        presenter.getDoctors(specialityId, hospitalId, patientId, null);
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_choose_doctor);
    }

    public void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void showDoctors(List<Doctor> doctors) {
        adapter.setData(doctors);
        recyclerView.setAdapter(adapter);
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
        progBarDoctor.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarDoctor.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
