package com.bigblackboy.doctorappointment.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigblackboy.doctorappointment.HtmlParser;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.RecyclerViewAdapter;
import com.bigblackboy.doctorappointment.model.District;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS;
import static com.bigblackboy.doctorappointment.SharedPreferencesManager.APP_SETTINGS_DISTRICT_ID;

public class DistrictFragment extends Fragment implements RecyclerViewAdapter.ItemClickListener {

    private String LOG_TAG = "myLog: DistrictFragment";
    RecyclerViewAdapter adapter;
    ArrayList<District> districts;
    SharedPreferences mSettings;
    private OnDistrictFragmentDataListener mListener;
    private String barTitle = "Выбор района";

    public interface OnDistrictFragmentDataListener {
        void onDistrictFragmentDataListener(District district);

        void onDistrictUpdateActionBarTitle(String barTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDistrictFragmentDataListener) {
            mListener = (OnDistrictFragmentDataListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDistrictFragmentDataListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mListener.onDistrictUpdateActionBarTitle(barTitle);
        return inflater.inflate(R.layout.fragment_district, null);
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

        mListener.onDistrictFragmentDataListener((District) adapter.getItem(position));

        mSettings = this.getActivity().getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_SETTINGS_DISTRICT_ID, districtId);
        editor.apply();
    }

    class AsyncRequest extends AsyncTask<Void, Void, ArrayList<District>> {

        @Override
        protected ArrayList<District> doInBackground(Void... voids) {
            return new HtmlParser().getDistricts();
        }
    }
}
