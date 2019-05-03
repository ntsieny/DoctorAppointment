package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.ReviewActivity;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.model.Doctor;
import com.bigblackboy.doctorappointment.recyclerviewadapter.DoctorReviewRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorReviewsFragment extends Fragment implements DoctorReviewRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: DocReviewsFrag";
    private static SpringApi springApi;
    private RecyclerView recyclerView;
    private DoctorReviewRecyclerViewAdapter adapter;
    private List<ReviewResponse> reviews;
    private String doctorId;
    private String doctorName;
    TextView tvDoctorNameReview, tvAvgMark;
    RatingBar rBarAvgMark;
    Button btnAddReview;
    private OnDoctorReviewsFragmentDataListener mListener;

    public interface OnDoctorReviewsFragmentDataListener {
        void onDoctorReviewsFragmentDataListener(int viewId);
    }


    public static DoctorReviewsFragment newInstance(String doctorId, String doctorName) {
        DoctorReviewsFragment fragment = new DoctorReviewsFragment();
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        args.putString("doctor_name", doctorName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoctorReviewsFragmentDataListener) {
            mListener = (OnDoctorReviewsFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDoctorReviewsFragmentDataListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId = getArguments().getString("doctor_id");
        doctorName = getArguments().getString("doctor_name");
        springApi = SpringController.getApi();
        adapter = new DoctorReviewRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_reviews, container, false);
        tvDoctorNameReview = v.findViewById(R.id.tvDoctorNameReview);
        tvAvgMark = v.findViewById(R.id.tvAvgMark);
        rBarAvgMark = v.findViewById(R.id.rBarAvgMark);
        btnAddReview = v.findViewById(R.id.btnAddReview);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDoctorReviewsFragmentDataListener(v.getId());
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getView().findViewById(R.id.rvDoctorReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        /*adapter = new DoctorReviewRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDoctorNameReview.setText(doctorName);
        getDoctorReviews();
    }

    private void getDoctorReviews() {
        springApi.getReviews(Integer.valueOf(doctorId)).enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        reviews = response.body();
                        adapter.setData(reviews);
                        recyclerView.setAdapter(adapter);
                        float avgMark = countAvgMark(reviews);
                        rBarAvgMark.setRating(avgMark);
                        tvAvgMark.append(String.valueOf(Math.round(avgMark * 10.0) / 10.0));
                    } else Toast.makeText(getContext(), "Отзывов не обнаружено", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Отзывы не найдены", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private float countAvgMark(List<ReviewResponse> list) {
        float sum = 0;
        for (ReviewResponse rev : list) {
            sum += rev.getMark();
        }
        return sum / list.size();
    }

    @Override
    public void onItemClick(int viewId, int position) {

    }
}
