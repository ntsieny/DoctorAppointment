package com.bigblackboy.doctorappointment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import com.bigblackboy.doctorappointment.pojos.springpojos.Procedure;
import com.bigblackboy.doctorappointment.presenter.CheckupFragmentPresenter;
import com.bigblackboy.doctorappointment.recyclerviewadapter.CheckupAdapter;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;

import java.util.List;

public class CheckupFragment extends Fragment implements MVPBaseInterface.View {

    private static final String LOG_TAG = "myLogs: checkupFrag";
    private int age;
    private RecyclerView rvFirstStep;
    private TextView tvHint;
    private CheckupAdapter adapter;
    private CheckupFragmentPresenter presenter;
    private ProgressBar progBarCheckup;

    public static CheckupFragment newInstance(int age) {
        CheckupFragment frag = new CheckupFragment();
        Bundle args = new Bundle();
        args.putInt("age", age);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CheckupAdapter(getContext());

        presenter = new CheckupFragmentPresenter();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkup, container, false);
        rvFirstStep = v.findViewById(R.id.rvFirstStep);
        rvFirstStep.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFirstStep.getContext(), getResources().getConfiguration().orientation);
        rvFirstStep.addItemDecoration(dividerItemDecoration);

        tvHint = v.findViewById(R.id.tvHint);
        progBarCheckup = v.findViewById(R.id.progBarCheckup);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("age"))
            age = getArguments().getInt("age");
        presenter.viewIsReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getHealthInfo(age);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_checkup);
    }

    public void showProcedures(List<Procedure> procedures) {
        adapter.setData(procedures);
        rvFirstStep.setAdapter(adapter);
    }

    public void setTvHint(String text) {
        tvHint.setText(text);
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
        progBarCheckup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progBarCheckup.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
