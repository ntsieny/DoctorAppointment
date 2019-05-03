package com.bigblackboy.doctorappointment.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.model.Hospital;
import com.bigblackboy.doctorappointment.springserver.Response;
import com.bigblackboy.doctorappointment.springserver.springmodel.Review;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class EditReviewFragment extends Fragment {

    private static final String LOG_TAG = "myLog: EditRevFrag";
    private SpringApi springApi;
    private TextView tvFioEditReview, tvHospitalEditReview;
    private RatingBar rBarEditReview;
    private EditText etEditReview;
    private Button btnSendReview;
    private int doctorId;
    private String doctorName;
    private String lpuName;
    private String serviceId;

    public static EditReviewFragment newInstance(Doctor doctor, String hospitalName, String serviceId) {
        EditReviewFragment frag = new EditReviewFragment();
        Bundle args = new Bundle();
        args.putInt("doctor_id", Integer.valueOf(doctor.getIdDoc()));
        args.putString("doctor_name", doctor.getName());
        args.putString("lpu_name", hospitalName);
        args.putString("service_id", serviceId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        springApi = SpringController.getApi();
        if(getArguments() != null) {
            if(getArguments().containsKey("doctor_id"))
                doctorId = getArguments().getInt("doctor_id");
            if(getArguments().containsKey("doctor_name"))
                doctorName = getArguments().getString("doctor_name");
            if(getArguments().containsKey("lpu_name"))
                lpuName = getArguments().getString("lpu_name");
            if(getArguments().containsKey("service_id"))
                serviceId = getArguments().getString("service_id");
        }
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
                sendReview();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFioEditReview.setText(doctorName);
        tvHospitalEditReview.setText(lpuName);
    }

    private void sendReview() {
        springApi.createReview(getReviewData()).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    if(response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Отзыв отправлен", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private Review getReviewData() {
        Review rev = new Review();
        rev.setDoctorId(doctorId);
        rev.setText(etEditReview.getText().toString());
        rev.setMark((int)rBarEditReview.getRating());
        rev.setServiceId(serviceId);
        return rev;
    }
}
