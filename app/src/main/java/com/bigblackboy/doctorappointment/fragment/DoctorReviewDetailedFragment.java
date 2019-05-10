package com.bigblackboy.doctorappointment.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.CommentRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.Comment;
import com.bigblackboy.doctorappointment.springserver.springmodel.CommentResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;
import com.bigblackboy.doctorappointment.utils.DateParser;
import com.bigblackboy.doctorappointment.utils.ErrorTranslator;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorReviewDetailedFragment extends Fragment implements View.OnClickListener, CommentRecyclerViewAdapter.ItemClickListener {

    private static final String LOG_TAG = "myLog: DocReviewsFrag";
    private static SpringApi springApi;
    private String serviceId;
    private int reviewId;
    private ReviewResponse review;
    private TextView tvAuthorNameReview, tvDateReview, tvLikeCounterReview, tvDislikeCounterReview, tvCommentCounterReview, tvReviewText;
    private EditText etCommentText;
    private RatingBar rBarReview;
    private CheckBox chbLike, chbDislike;
    private ImageButton imBtnEditReview, imBtnSendComment;
    private OnDoctorReviewDetailedFragmentDataListener mListener;
    private CommentRecyclerViewAdapter adapter;
    private RecyclerView rvComments;

    @Override
    public void onButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.chbLikeComment:
                if (((CheckBox)v).isChecked()) {
                    sendCommentLike(serviceId, adapter.getItem(position).getCommentId());
                } else deleteCommentLike(serviceId, adapter.getItem(position).getCommentId());
                break;
            case R.id.chbDislikeComment:
                if(((CheckBox)v).isChecked()) {
                    sendCommentDislike(serviceId, adapter.getItem(position).getCommentId());
                } else deleteCommentLike(serviceId, adapter.getItem(position).getCommentId());
                break;
            case R.id.imBtnDeleteComment:
                deleteComment(adapter.getItem(position).getCommentId());
                break;
        }
    }

    public interface OnDoctorReviewDetailedFragmentDataListener {
        void onDoctorReviewDetailedFragmentBtnClick(View v, ReviewResponse rev);
    }


    public static DoctorReviewDetailedFragment newInstance(int reviewId, String serviceId) {
        DoctorReviewDetailedFragment fragment = new DoctorReviewDetailedFragment();
        Bundle args = new Bundle();
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
        springApi = SpringController.getApi();
        adapter = new CommentRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor_review_detailed, container, false);
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
        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        getReviewById(reviewId);
        getComments(reviewId);
    }

    private void getComments(int reviewId) {
        springApi.getComments(reviewId).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call, Response<List<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        List<CommentResponse> comments = response.body();
                        adapter.setData(comments);
                        rvComments.setAdapter(adapter);
                    } else
                        Toast.makeText(getContext(), ErrorTranslator.getDescription(ErrorTranslator.COMMENTS_NOT_FOUND), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), error.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void sendComment(Comment comment) {
        springApi.createComment(comment).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Комментарий отправлен", Toast.LENGTH_SHORT).show();
                        etCommentText.setText("");
                        getComments(reviewId);
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), error.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
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

    private void sendCommentLike(String serviceId, int commentId) {
        springApi.likeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {

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

    private void deleteCommentLike(String serviceId, int commentId) {
        springApi.deletelikeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
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

    private void sendCommentDislike(String serviceId, int commentId) {
        springApi.dislikeComment(serviceId, commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {

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

    private void getReviewById(int reviewId) {
        springApi.getReview(reviewId).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    review = response.body();
                    showReview(review);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Ошибка загрузки отзыва", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка соединения", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, t.getMessage());
            }
        });
    }

    private void sendReviewLike(String serviceId, int reviewId) {
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

    private void sendReviewDislike(String serviceId, int reviewId) {
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

    private void deleteReviewLike(String serviceId, int reviewId) {
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

    private void deleteComment(int commentId) {
        springApi.deleteComment(commentId).enqueue(new Callback<com.bigblackboy.doctorappointment.springserver.Response>() {
            @Override
            public void onResponse(Call<com.bigblackboy.doctorappointment.springserver.Response> call, Response<com.bigblackboy.doctorappointment.springserver.Response> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        //Toast.makeText(getContext(), "Комментарий удален", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), "Комментарий не удален", Toast.LENGTH_SHORT).show();
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
                if (chbLike.isChecked()) {
                sendReviewLike(serviceId, reviewId);
            } else deleteReviewLike(serviceId, reviewId);

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
                if (chbDislike.isChecked()) {
                    sendReviewDislike(serviceId, reviewId);
                } else deleteReviewLike(serviceId, reviewId);

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
                if (!TextUtils.isEmpty(etCommentText.getText().toString())) {
                    Comment com = new Comment();
                    com.setServiceId(serviceId);
                    com.setReviewId(reviewId);
                    com.setText(etCommentText.getText().toString());
                    sendComment(com);

                } else Toast.makeText(getContext(), "Введите текст комментария", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showReview(ReviewResponse review) {
        tvAuthorNameReview.setText(String.format("%s %s.", review.getLastname(), review.getName().substring(0,1)));
        tvDateReview.setText(DateParser.convertISOwithMillistoDateTimeString(review.getDateTime()));
        tvLikeCounterReview.setText(String.valueOf(review.getLikes()));
        tvDislikeCounterReview.setText(String.valueOf(review.getDislikes()));
        tvCommentCounterReview.setText(String.valueOf(review.getCommentCount()));
        rBarReview.setRating(review.getMark());
        tvReviewText.setText(review.getText());
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
