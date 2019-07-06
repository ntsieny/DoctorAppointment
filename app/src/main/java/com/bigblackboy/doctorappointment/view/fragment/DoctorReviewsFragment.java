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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.presenter.DoctorReviewsFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.ReviewRecyclerViewAdapter;

import java.util.List;

public class DoctorReviewsFragment extends Fragment implements MVPBaseInterface.View, ReviewRecyclerViewAdapter.ItemClickListener {

    private boolean guestMode;
    private SharedPreferencesManager prefManager;
    private static final String LOG_TAG = "myLog: DocReviewsFrag";
    private ReviewsResponse review;
    private String doctorId;
    private String doctorName;
    private String serviceId;
    private DoctorReviewsFragmentPresenter presenter;
    private ReviewRecyclerViewAdapter adapter;
    private TextView tvDoctorNameReview, tvAvgMark;
    private RatingBar rBarAvgMark;
    private Button btnAddReview;
    private TextView tvReviewsEmpty;
    private RecyclerView recyclerView;
    private ProgressBar progBarReviews;
    private RelativeLayout innerLayoutReviews;
    private OnDoctorReviewsFragmentDataListener mListener;

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
        adapter = new ReviewRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
        prefManager = new SharedPreferencesManager(getActivity().getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE));
        guestMode = prefManager.isGuestMode();

        presenter = new DoctorReviewsFragmentPresenter();
        presenter.attachView(this);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewIsReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvDoctorNameReview.setText(doctorName);
        presenter.getDoctorReviews(Integer.valueOf(doctorId));
    }

    public void showDoctorReviews(List<ReviewsResponse> reviews) {
        adapter.setData(reviews);
        recyclerView.setAdapter(adapter);
    }

    public void setDoctorStarRating(float rating) {
        rBarAvgMark.setRating(rating);
    }

    public void setDoctorTextRating(float avgMark) {
        tvAvgMark.append(String.valueOf(Math.round(avgMark * 10.0) / 10.0));
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
        progBarReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarReviews.setVisibility(View.INVISIBLE);
    }

    public void showInnerLayout() {
        innerLayoutReviews.setVisibility(View.VISIBLE);
    }

    public void hideInnerLayout() {
        innerLayoutReviews.setVisibility(View.INVISIBLE);
    }

    public void showReviewsEmptyLabel() {
        tvReviewsEmpty.setVisibility(View.VISIBLE);
    }

    public void hideReviewsEmptyLabel() {
        tvReviewsEmpty.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(View v, int position) {
        mListener.onDoctorReviewsFragmentDataListener(adapter.getItem(position));
    }

    @Override
    public void onButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.chbLike:
                if (!guestMode) {
                    if (((CheckBox) v).isChecked()) {
                        presenter.sendReviewLike(serviceId, adapter.getItem(position).getReviewId());
                    } else presenter.deleteLike(serviceId, adapter.getItem(position).getReviewId());
                }
                break;
            case R.id.chbDislike:
                if (!guestMode) {
                    if (((CheckBox) v).isChecked()) {
                        presenter.sendReviewDislike(serviceId, adapter.getItem(position).getReviewId());
                    } else presenter.deleteLike(serviceId, adapter.getItem(position).getReviewId());
                }
                break;
            case R.id.imBtnComments:
                review = adapter.getItem(position);
                mListener.onDoctorReviewsFragmentBtnClickListener(v, review);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
