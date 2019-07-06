package com.bigblackboy.doctorappointment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Hospital;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Speciality;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.presenter.EditReviewFragmentPresenter;

public class EditReviewFragment extends Fragment implements MVPBaseInterface.View {

    private static final String LOG_TAG = "myLog: EditRevFrag";
    private EditReviewFragmentPresenter presenter;
    private TextView tvFioEditReview, tvHospitalEditReview;
    private RatingBar rBarEditReview;
    private EditText etEditReview;
    private Button btnSendReview, btnDeleteReview;
    private District district;
    private Hospital hospital;
    private Speciality speciality;
    private Doctor doctor;
    private String serviceId;
    private Review review;

    public static EditReviewFragment newInstance(District district, Speciality speciality, Doctor doctor, Hospital hospital, String serviceId) {
        EditReviewFragment frag = new EditReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("district", district);
        args.putSerializable("speciality", speciality);
        args.putSerializable("doctor", doctor);
        args.putSerializable("hospital", hospital);
        args.putString("service_id", serviceId);
        frag.setArguments(args);
        return frag;
    }

    public static EditReviewFragment newInstance(String serviceId, Review review) {
        District district = new District(String.valueOf(review.getDistrictId()), review.getDistrictName(), null);
        Speciality speciality = new Speciality(String.valueOf(review.getSpecialityId()), review.getSpecialityName());
        Doctor doctor = new Doctor(String.valueOf(review.getDoctorId()), review.getDoctorName());
        Hospital hospital = new Hospital(review.getLpuId(), review.getLpuName());

        EditReviewFragment frag = new EditReviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("district", district);
        args.putSerializable("speciality", speciality);
        args.putSerializable("doctor", doctor);
        args.putSerializable("hospital", hospital);
        args.putString("service_id", serviceId);
        args.putSerializable("review", review);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            if(getArguments().containsKey("district"))
                district = (District) getArguments().getSerializable("district");
            if(getArguments().containsKey("speciality"))
                speciality = (Speciality) getArguments().getSerializable("speciality");
            if(getArguments().containsKey("doctor"))
                doctor = (Doctor) getArguments().getSerializable("doctor");
            if(getArguments().containsKey("hospital"))
                hospital = (Hospital) getArguments().getSerializable("hospital");
            if(getArguments().containsKey("service_id"))
                serviceId = getArguments().getString("service_id");
            if(getArguments().containsKey("review"))
                review = (Review) getArguments().getSerializable("review");
        }
        presenter = new EditReviewFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review_edit, container, false);
        tvFioEditReview = v.findViewById(R.id.tvFioEditReview);
        tvHospitalEditReview = v.findViewById(R.id.tvHospitalEditReview);
        rBarEditReview = v.findViewById(R.id.rBarEditReview);
        etEditReview = v.findViewById(R.id.etEditReview);
        btnSendReview = v.findViewById(R.id.btnSendReview);
        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review == null)
                    presenter.sendReview(getFilledData());
                else presenter.editReview(getFilledData());
            }
        });
        btnDeleteReview = v.findViewById(R.id.btnDeleteReview);
        btnDeleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteReview(review.getReviewId());
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFioEditReview.setText(doctor.getName());
        tvHospitalEditReview.setText(hospital.getLPUShortName());
        if (review != null) {
            setFilledData(review);
            btnDeleteReview.setVisibility(View.VISIBLE);
        }
        presenter.viewIsReady();
    }

    public void popBackStack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private Review getFilledData() {
        Review rev = new Review();
        if (review != null)
            rev.setReviewId(review.getReviewId());
        rev.setDoctorId(Integer.valueOf(doctor.getIdDoc()));
        rev.setDoctorName(doctor.getName());
        rev.setText(etEditReview.getText().toString());
        rev.setMark((int)rBarEditReview.getRating());
        rev.setServiceId(serviceId);
        rev.setSpecialityId(Integer.valueOf(speciality.getIdSpeciality()));
        rev.setSpecialityName(speciality.getNameSpeciality());
        rev.setLpuId(hospital.getIdLPU());
        rev.setLpuName(hospital.getLPUShortName());
        rev.setDistrictId(Integer.valueOf(district.getId()));
        rev.setDistrictName(district.getName());
        return rev;
    }

    private void setFilledData(Review rev) {
        tvFioEditReview.setText(doctor.getName());
        tvHospitalEditReview.setText(hospital.getLPUShortName());
        rBarEditReview.setRating(rev.getMark());
        etEditReview.setText(rev.getText());
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
