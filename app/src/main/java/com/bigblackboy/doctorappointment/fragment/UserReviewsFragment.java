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
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.UserReviewsRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.Review;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReviewsFragment extends Fragment implements UserReviewsRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: UserReviewsFrag";
    private static SpringApi springApi;
    private UserReviewsRecyclerViewAdapter adapter;
    private List<Review> reviews;
    private String serviceId;
    private RecyclerView recyclerView;
    private OnUserReviewsFragmentDataListener mListener;

    public interface OnUserReviewsFragmentDataListener {
        void onUserReviewsFragmentBtnClick(View v, Review review);
        void onUserReviewsFragmentDataListener(Review review);
    }

    public static UserReviewsFragment newInstance(String serviceId) {
        UserReviewsFragment fragment = new UserReviewsFragment();
        Bundle args = new Bundle();
        args.putString("service_id", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserReviewsFragmentDataListener) {
            mListener = (OnUserReviewsFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnUserReviewsFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceId = getArguments().getString("service_id");
        springApi = SpringController.getApi();
        adapter = new UserReviewsRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        recyclerView = v.findViewById(R.id.rvUserReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserReviews(serviceId);
    }

    private void getUserReviews(String serviceId) {
        springApi.getReviews(serviceId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        List<Review> reviews = response.body();
                        adapter.setData(reviews);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Отзывы не получены", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onUserReviewsButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                mListener.onUserReviewsFragmentBtnClick(v, adapter.getItem(position));
                break;
            case R.id.imBtnDeleteReview:
                deleteReview(adapter.getItem(position).getReviewId());
                break;
        }
    }

    @Override
    public void onUserReviewsItemClick(View v, int position) {

    }

    private void deleteReview(int reviewId) {
        springApi.deleteReview(reviewId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, retrofit2.Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if(response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Отзыв удален", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }
}
