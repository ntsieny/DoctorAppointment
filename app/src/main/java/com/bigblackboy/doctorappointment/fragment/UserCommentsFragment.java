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
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.controller.SpringApi;
import com.bigblackboy.doctorappointment.controller.SpringController;
import com.bigblackboy.doctorappointment.recyclerviewadapter.UserCommentsRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.springserver.springmodel.MyCommentsResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.Review;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommentsFragment extends Fragment implements UserCommentsRecyclerViewAdapter.OnUserCommentsItemClickListener {

    private static final String LOG_TAG = "myLog: UserCommentsFrag";
    private static SpringApi springApi;
    private UserCommentsRecyclerViewAdapter adapter;
    private List<MyCommentsResponse> comments;
    private String serviceId;
    private RecyclerView recyclerView;
    private TextView tvUserComments;
    private OnUserCommentsFragmentDataListener mListener;

    @Override
    public void onUserCommentsButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.imBtnDeleteComment:
                deleteComment(adapter.getItem(position).getCommentId());
                break;
        }
    }

    @Override
    public void onUserCommentsItemClick(View v, int position) {
        mListener.onUserCommentsFragmentDataListener(adapter.getItem(position).getReviewId());
    }

    public interface OnUserCommentsFragmentDataListener {
        void onUserCommentsFragmentDataListener(int reviewId);
    }

    public static UserCommentsFragment newInstance(String serviceId) {
        UserCommentsFragment fragment = new UserCommentsFragment();
        Bundle args = new Bundle();
        args.putString("service_id", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserCommentsFragmentDataListener) {
            mListener = (OnUserCommentsFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnUserCommentsFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceId = getArguments().getString("service_id");
        springApi = SpringController.getApi();
        adapter = new UserCommentsRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_comments, container, false);
        recyclerView = v.findViewById(R.id.rvUserComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvUserComments = v.findViewById(R.id.tvUserComments);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserComments(serviceId);
    }

    private void getUserComments(String serviceId) {
        springApi.getComments(serviceId).enqueue(new Callback<List<MyCommentsResponse>>() {
            @Override
            public void onResponse(Call<List<MyCommentsResponse>> call, Response<List<MyCommentsResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        tvUserComments.setText("Мои комментарии");
                        adapter.setData(response.body());
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    try {
                        tvUserComments.setText("Комментариев нет");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(getContext(), error.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, error.getString("message"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyCommentsResponse>> call, Throwable t) {
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
                    if(response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Комментарий удален", Toast.LENGTH_SHORT).show();
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
