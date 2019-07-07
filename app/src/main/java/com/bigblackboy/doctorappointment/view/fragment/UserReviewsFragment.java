package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.springpojos.Review;
import com.bigblackboy.doctorappointment.presenter.UserReviewsFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.UserReviewsRecyclerViewAdapter;

import java.util.List;

public class UserReviewsFragment extends Fragment implements MVPBaseInterface.View, UserReviewsRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: UserReviewsFrag";
    private UserReviewsRecyclerViewAdapter adapter;
    private String serviceId;
    private RecyclerView recyclerView;
    private ProgressBar progBarUserReviews;
    private TextView tvUserReviews;
    private OnUserReviewsFragmentDataListener mListener;
    private UserReviewsFragmentPresenter presenter;

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
        } else throw new RuntimeException(context.toString() + " must implement OnUserCommentsFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceId = getArguments().getString("service_id");
        adapter = new UserReviewsRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);

        presenter = new UserReviewsFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_reviews, container, false);
        recyclerView = v.findViewById(R.id.rvUserReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvUserReviews = v.findViewById(R.id.tvUserReviews);
        progBarUserReviews = v.findViewById(R.id.progBarUserReviews);
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
        presenter.getUserReviews(serviceId);
    }

    public void showReviews(List<Review> reviews) {
        adapter.setData(reviews);
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
        progBarUserReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarUserReviews.setVisibility(View.INVISIBLE);
    }

    public void setReviewsLabel(String text) {
        tvUserReviews.setText(text);
    }

    @Override
    public void onUserReviewsButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                mListener.onUserReviewsFragmentBtnClick(v, adapter.getItem(position));
                break;
            case R.id.imBtnDeleteReview:
                presenter.deleteReview(adapter.getItem(position).getReviewId());
                break;
        }
    }

    @Override
    public void onUserReviewsItemClick(View v, int position) {
        mListener.onUserReviewsFragmentDataListener(adapter.getItem(position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
