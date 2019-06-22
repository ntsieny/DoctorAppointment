package com.bigblackboy.doctorappointment.view.fragment;

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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.utils.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.ReviewRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorReviewsFragment extends Fragment implements ReviewRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: DocReviewsFrag";
    private static SpringApi springApi;
    private ReviewRecyclerViewAdapter adapter;
    private List<ReviewsResponse> reviews;
    private ReviewsResponse review;
    private String doctorId;
    private String doctorName;
    private String serviceId;
    private TextView tvDoctorNameReview, tvAvgMark;
    private RatingBar rBarAvgMark;
    private Button btnAddReview;
    private TextView tvReviewsEmpty;
    private RecyclerView recyclerView;
    private ProgressBar progBarReviews;
    private RelativeLayout innerLayoutReviews;
    private OnDoctorReviewsFragmentDataListener mListener;
    private boolean guestMode;
    private SharedPreferencesManager prefManager;

    public interface OnDoctorReviewsFragmentDataListener {
        void onDoctorReviewsFragmentBtnClickListener(View v, ReviewsResponse review);
        void onDoctorReviewsFragmentDataListener(ReviewsResponse review);
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
        } else throw new RuntimeException(context.toString() + " must implement OnUserCommentsFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId = getArguments().getString("doctor_id");
        doctorName = getArguments().getString("doctor_name");
        serviceId = getArguments().getString("service_id");
        springApi = SpringController.getApi();
        adapter = new ReviewRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
        prefManager = new SharedPreferencesManager(getActivity().getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE));
        guestMode = prefManager.isGuestMode();
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
        progBarReviews = v.findViewById(R.id.progBarReviews);
        innerLayoutReviews = v.findViewById(R.id.innerLayoutReviews);
        tvReviewsEmpty = v.findViewById(R.id.tvReviewsEmpty);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDoctorNameReview.setText(doctorName);
        getDoctorReviews();
    }

    private void getDoctorReviews() {
        springApi.getReviews(Integer.valueOf(doctorId)).enqueue(new Callback<List<ReviewsResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewsResponse>> call, Response<List<ReviewsResponse>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        reviews = response.body();
                        adapter.setData(reviews);
                        recyclerView.setAdapter(adapter);
                        float avgMark = countAvgMark(reviews);
                        rBarAvgMark.setRating(avgMark);
                        tvAvgMark.append(String.valueOf(Math.round(avgMark * 10.0) / 10.0));
                        progBarReviews.setVisibility(View.INVISIBLE);
                        innerLayoutReviews.setVisibility(View.VISIBLE);
                    } else Toast.makeText(getContext(), "Отзывов не обнаружено", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        progBarReviews.setVisibility(View.INVISIBLE);
                        innerLayoutReviews.setVisibility(View.VISIBLE);
                        tvReviewsEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Отзывы не найдены", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReviewsResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private float countAvgMark(List<ReviewsResponse> list) {
        float sum = 0;
        for (ReviewsResponse rev : list) {
            sum += rev.getMark();
        }
        return sum / list.size();
    }

    @Override
    public void onButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.chbLike:
                if (!guestMode) {
                    if (((CheckBox) v).isChecked()) {
                        sendLike(serviceId, adapter.getItem(position).getReviewId());
                    } else deleteLike(serviceId, adapter.getItem(position).getReviewId());
                }
                break;
            case R.id.chbDislike:
                if (!guestMode) {
                    if (((CheckBox) v).isChecked()) {
                        sendDislike(serviceId, adapter.getItem(position).getReviewId());
                    } else deleteLike(serviceId, adapter.getItem(position).getReviewId());
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
        mListener.onDoctorReviewsFragmentDataListener(adapter.getItem(position));
    }

    private void sendLike(String serviceId, int reviewId) {
        springApi.likeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
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
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void sendDislike(String serviceId, int reviewId) {
        springApi.dislikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
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
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void deleteLike(String serviceId, int reviewId) {
        springApi.deletelikeReview(serviceId, reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.pojos.springpojos.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Response<com.bigblackboy.doctorappointment.pojos.springpojos.Response> response) {
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
            public void onFailure(Call<com.bigblackboy.doctorappointment.pojos.springpojos.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
