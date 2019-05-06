package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.activity.ReviewActivity;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
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
    private DoctorReviewRecyclerViewAdapter adapter;
    private List<ReviewResponse> reviews;
    private ReviewResponse review;
    private String doctorId;
    private String doctorName;
    private String serviceId;
    TextView tvDoctorNameReview, tvAvgMark;
    RatingBar rBarAvgMark;
    Button btnAddReview;
    private RecyclerView recyclerView;
    private OnDoctorReviewsFragmentDataListener mListener;

    public interface OnDoctorReviewsFragmentDataListener {
        void onDoctorReviewsFragmentBtnClickListener(View v, ReviewResponse review);
        void onDoctorReviewsFragmentDataListener(ReviewResponse review);
    }

    public static DoctorReviewsFragment newInstance(String doctorId, String doctorName, String serviceId) {
        DoctorReviewsFragment fragment = new DoctorReviewsFragment();
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        args.putString("doctor_name", doctorName);
        args.putString("service_id", serviceId);
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
        serviceId = getArguments().getString("service_id");
        springApi = SpringController.getApi();
        adapter = new DoctorReviewRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_reviews, container, false);
        tvDoctorNameReview = v.findViewById(R.id.tvDoctorNameReview);
        tvAvgMark = v.findViewById(R.id.tvAvgMark);
        rBarAvgMark = v.findViewById(R.id.rBarAvgMark);
        recyclerView = v.findViewById(R.id.rvDoctorReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddReview = v.findViewById(R.id.btnAddReview);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDoctorReviewsFragmentBtnClickListener(v, null);
            }
        });
        return v;
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
    public void onButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.chbLike:
                if (((CheckBox) v).isChecked()) {
                    sendLike(serviceId, adapter.getItem(position).getReviewId());
                } else {
                    deleteLike(serviceId, adapter.getItem(position).getReviewId());
                }
                break;
            case R.id.chbDislike:
                if (((CheckBox) v).isChecked()) {
                    sendDislike(serviceId, adapter.getItem(position).getReviewId());
                } else {
                    deleteLike(serviceId, adapter.getItem(position).getReviewId());
                }
                break;
            case R.id.imBtnComments:
                review = adapter.getItem(position);
                mListener.onDoctorReviewsFragmentBtnClickListener(v, review);
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        mListener.onDoctorReviewsFragmentDataListener(review);
    }

    private void sendLike(String serviceId, int reviewId) {
        springApi.likeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        //Toast.makeText(getContext(), "Лайк отправлен", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Лайк не отправлен", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void sendDislike(String serviceId, int reviewId) {
        springApi.dislikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        //Toast.makeText(getContext(), "Дизлайк отправлен", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Дизлайк не отправлен", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void deleteLike(String serviceId, int reviewId) {
        springApi.deletelikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        //Toast.makeText(getContext(), "Лайк/дизлайк удален", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Лайк/дизлайк не удален", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
