package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.MVPBaseInterface;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.SharedPreferencesManager;
import com.bigblackboy.doctorappointment.pojos.springpojos.Comment;
import com.bigblackboy.doctorappointment.pojos.springpojos.CommentResponse;
import com.bigblackboy.doctorappointment.pojos.springpojos.ReviewsResponse;
import com.bigblackboy.doctorappointment.presenter.DoctorReviewDetailedFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.CommentRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class DoctorReviewDetailedFragment extends Fragment implements MVPBaseInterface.View, View.OnClickListener, CommentRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: DocReviewsFrag";
    private DoctorReviewDetailedFragmentPresenter presenter;
    private String serviceId;
    private ReviewsResponse review;
    private int reviewId;
    private SharedPreferencesManager prefManager;
    private boolean guestMode;
    private TextView tvAuthorNameReview, tvDateReview, tvLikeCounterReview, tvDislikeCounterReview, tvCommentCounterReview, tvReviewText, tvDoctorName, tvComments;
    private EditText etCommentText;
    private RatingBar rBarReview;
    private CheckBox chbLike, chbDislike;
    private ImageButton imBtnEditReview, imBtnSendComment;
    private OnDoctorReviewDetailedFragmentDataListener mListener;
    private CommentRecyclerViewAdapter adapter;
    private RecyclerView rvComments;
    private ProgressBar progBarReviewDetailed;
    private RelativeLayout innerLayoutDetailedReview;

    public interface OnDoctorReviewDetailedFragmentDataListener {
        void onDoctorReviewDetailedFragmentBtnClick(View v, ReviewsResponse rev);
    }

    public static DoctorReviewDetailedFragment newInstance(int reviewId, String serviceId) {
        DoctorReviewDetailedFragment fragment = new DoctorReviewDetailedFragment();
        Bundle args = new Bundle();
        if(serviceId != null)
            args.putString("service_id", serviceId);
        args.putInt("review_id", reviewId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoctorReviewDetailedFragmentDataListener) {
            mListener = (OnDoctorReviewDetailedFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnDoctorReviewDetailedFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reviewId = getArguments().getInt("review_id");
        serviceId = getArguments().getString("service_id");
        adapter = new CommentRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
        prefManager = new SharedPreferencesManager(getActivity().getSharedPreferences(SharedPreferencesManager.APP_SETTINGS, Context.MODE_PRIVATE));
        guestMode = prefManager.isGuestMode();

        presenter = new DoctorReviewDetailedFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_review_detailed, container, false);
        tvDoctorName = v.findViewById(R.id.tvDoctorName);
        tvAuthorNameReview = v.findViewById(R.id.tvAuthorNameReview);
        tvDateReview = v.findViewById(R.id.tvDateReview);
        tvLikeCounterReview = v.findViewById(R.id.tvLikeCounterReview);
        tvDislikeCounterReview = v.findViewById(R.id.tvDislikeCounterReview);
        tvCommentCounterReview = v.findViewById(R.id.tvCommentCounterReview);
        rBarReview = v.findViewById(R.id.rBarReview);
        tvReviewText = v.findViewById(R.id.tvReviewText);
        imBtnEditReview = v.findViewById(R.id.imBtnEditReview);
        imBtnEditReview.setOnClickListener(this);
        chbLike = v.findViewById(R.id.chbLike);
        chbLike.setOnClickListener(this);
        chbDislike = v.findViewById(R.id.chbDislike);
        chbDislike.setOnClickListener(this);
        rvComments = v.findViewById(R.id.rvComments);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        etCommentText = v.findViewById(R.id.etCommentText);
        imBtnSendComment = v.findViewById(R.id.imBtnSendComment);
        imBtnSendComment.setOnClickListener(this);
        progBarReviewDetailed = v.findViewById(R.id.progBarReviewDetailed);
        innerLayoutDetailedReview = v.findViewById(R.id.innerLayoutDetailedReview);
        tvComments = v.findViewById(R.id.tvComments);
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
        presenter.getReviewById(reviewId);
        presenter.getComments(reviewId);
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
        progBarReviewDetailed.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarReviewDetailed.setVisibility(View.INVISIBLE);
    }

    public void showComments(List<CommentResponse> comments) {
        adapter.setData(comments);
        rvComments.setAdapter(adapter);
    }

    public void showInnerLayout() {
        innerLayoutDetailedReview.setVisibility(View.VISIBLE);
    }

    public void hideInnerLayout() {
        innerLayoutDetailedReview.setVisibility(View.INVISIBLE);
    }

    public void setCommentLabelText(String text) {
        tvComments.setText(text);
    }

    public void setEtCommentText(String text) {
        etCommentText.setText(text);
    }

    public void updateCommentList() {
        presenter.getComments(reviewId);
    }

    public void setDoctorName(String name) {
        tvDoctorName.setText(name);
    }

    public void showReview(ReviewsResponse review) {
        this.review = review;
        tvAuthorNameReview.setText(String.format("%s %s.", review.getLastname(), review.getName().substring(0,1)));
        tvDateReview.setText(DateParser.convertISOwithMillistoDateTimeString(review.getDateTime()));
        tvLikeCounterReview.setText(String.valueOf(review.getLikes()));
        tvDislikeCounterReview.setText(String.valueOf(review.getDislikes()));
        tvCommentCounterReview.setText(String.valueOf(review.getCommentCount()));
        rBarReview.setRating(review.getMark());
        tvReviewText.setText(review.getText());
        if (serviceId != null) {
            if (review.getLikers() != null && review.getLikers().contains(serviceId)) {
                chbLike.setChecked(true);
            } else if (review.getDislikers() != null && review.getDislikers().contains(serviceId)) {
                chbDislike.setChecked(true);
            }
            if (serviceId.equals(review.getServiceId())) {
                imBtnEditReview.setVisibility(View.VISIBLE);
            }
        }
    }

    private String decrementStringVal(String str) {
        return String.valueOf(Integer.parseInt(str) - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imBtnEditReview:
                mListener.onDoctorReviewDetailedFragmentBtnClick(v, review);
                break;
            case R.id.chbLike:
                if (!guestMode) {
                    if (chbLike.isChecked()) {
                        presenter.sendReviewLike(serviceId, reviewId);
                    } else presenter.deleteReviewLike(serviceId, reviewId);
                }

                if (chbDislike.isChecked()) {
                    chbDislike.setChecked(false);
                    tvDislikeCounterReview.setText(decrementStringVal(tvDislikeCounterReview.getText().toString()));
                }

                int likes = Integer.valueOf(tvLikeCounterReview.getText().toString());
                if (chbLike.isChecked()) {
                    tvLikeCounterReview.setText(String.valueOf(++likes));
                } else tvLikeCounterReview.setText(String.valueOf(--likes));
                break;
            case R.id.chbDislike:
                if (!guestMode) {
                    if (chbDislike.isChecked()) {
                        presenter.sendReviewDislike(serviceId, reviewId);
                    } else presenter.deleteReviewLike(serviceId, reviewId);
                }

                if (chbLike.isChecked()) {
                    chbLike.setChecked(false);
                    tvLikeCounterReview.setText(decrementStringVal(tvLikeCounterReview.getText().toString()));
                }

                int dislikes = Integer.valueOf(tvDislikeCounterReview.getText().toString());
                if (chbDislike.isChecked()) {
                    tvDislikeCounterReview.setText(String.valueOf(++dislikes));
                } else tvDislikeCounterReview.setText(String.valueOf(--dislikes));
                break;
            case R.id.imBtnSendComment:
                if (!guestMode) {
                    if (!TextUtils.isEmpty(etCommentText.getText().toString())) {
                        Comment com = new Comment();
                        com.setServiceId(serviceId);
                        com.setReviewId(reviewId);
                        com.setText(etCommentText.getText().toString());
                        presenter.sendComment(com);
                    } else Toast.makeText(getContext(), "Введите текст комментария", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), R.string.toast_you_have_to_login_for_action, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.chbLikeComment:
                if (!guestMode) {
                    if (((CheckBox)v).isChecked()) {
                        presenter.sendCommentLike(serviceId, adapter.getItem(position).getCommentId());
                    } else presenter.deleteCommentLike(serviceId, adapter.getItem(position).getCommentId());
                }
                break;
            case R.id.chbDislikeComment:
                if(!guestMode) {
                    if(((CheckBox)v).isChecked()) {
                        presenter.sendCommentDislike(serviceId, adapter.getItem(position).getCommentId());
                    } else presenter.deleteCommentLike(serviceId, adapter.getItem(position).getCommentId());
                }
                break;
            case R.id.imBtnDeleteComment:
                presenter.deleteComment(adapter.getItem(position).getCommentId());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
