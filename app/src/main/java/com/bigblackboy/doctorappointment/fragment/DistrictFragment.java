package com.bigblackboy.doctorappointment.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigblackboy.doctorappointment.HtmlParser;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.activity.MainActivity;
import com.bigblackboy.doctorappointment.activity.OnDataPass;
import com.bigblackboy.doctorappointment.model.District;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DistrictFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    ArrayList<District> districts;
    private OnDataPass mDataPasser;
    private String LOG_TAG = "myLog";
    SharedPreferences mSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.district_fragment, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDataPasser = (OnDataPass) activity;
    }

    @Override
    public void onResume() {
        super.onResume();

        AsyncRequest request = new AsyncRequest();
        request.execute();
        try {
            districts = request.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = getView().findViewById(R.id.rvDistrict);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), getResources().getConfiguration().orientation);
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new RecyclerViewAdapter(getContext(), districts);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getContext(), "You clicked " + ((District)adapter.getItem(position)).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
        String districtId = ((District)adapter.getItem(position)).getId();
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("district_id", districtId);
        mDataPasser.onDataPass(1, hashMap);

        mSettings = this.getActivity().getSharedPreferences(MainActivity.APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(MainActivity.APP_SETTINGS_DISTRICT_ID, districtId);
        editor.apply();

        Log.d(LOG_TAG, "DistrictId: " + districtId);
    }

    class AsyncRequest extends AsyncTask<Void, Void, ArrayList<District>> {

        @Override
        protected ArrayList<District> doInBackground(Void... voids) {
            return new HtmlParser().getDistricts();
        }
    }
}
