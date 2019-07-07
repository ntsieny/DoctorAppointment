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
import com.bigblackboy.doctorappointment.pojos.springpojos.MyCommentsResponse;
import com.bigblackboy.doctorappointment.presenter.UserCommentsFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.UserCommentsRecyclerViewAdapter;

import java.util.List;

public class UserCommentsFragment extends Fragment implements MVPBaseInterface.View, UserCommentsRecyclerViewAdapter.OnUserCommentsItemClickListener {

    private static final String LOG_TAG = "myLog: UserCommentsFrag";
    private UserCommentsRecyclerViewAdapter adapter;
    private String serviceId;
    private RecyclerView recyclerView;
    private ProgressBar progBarUserComments;
    private TextView tvUserComments;
    private OnUserCommentsFragmentDataListener mListener;
    private UserCommentsFragmentPresenter presenter;

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
        adapter = new UserCommentsRecyclerViewAdapter(getContext(), serviceId);
        adapter.setClickListener(this);

        presenter = new UserCommentsFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_comments, container, false);
        recyclerView = v.findViewById(R.id.rvUserComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvUserComments = v.findViewById(R.id.tvUserComments);
        progBarUserComments = v.findViewById(R.id.progBarUserComments);
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
        presenter.getUserComments(serviceId);
    }

    public void setCommentsLabel(String text) {
        tvUserComments.setText(text);
    }

    public void showComments(List<MyCommentsResponse> comments) {
        adapter.setData(comments);
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
        progBarUserComments.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarUserComments.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUserCommentsButtonClick(View v, int position) {
        switch (v.getId()) {
            case R.id.imBtnDeleteComment:
                presenter.deleteComment(adapter.getItem(position).getCommentId());
                break;
        }
    }

    @Override
    public void onUserCommentsItemClick(View v, int position) {
        mListener.onUserCommentsFragmentDataListener(adapter.getItem(position).getReviewId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
