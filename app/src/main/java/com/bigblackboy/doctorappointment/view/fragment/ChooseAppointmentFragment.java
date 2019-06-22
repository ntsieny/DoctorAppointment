package com.bigblackboy.doctorappointment.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.view.activity.MainActivity;
import com.bigblackboy.doctorappointment.controller.HospitalController;
import com.bigblackboy.doctorappointment.controller.HospitalApi;
import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.recyclerviewadapter.AppointmentDatesRecyclerViewAdapter;
import com.bigblackboy.doctorappointment.api.AppointmentListApiResponse;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.AppointmentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAppointmentFragment extends Fragment implements AppointmentDatesRecyclerViewAdapter.OnAppointmentDatesItemClickListener {

    private String LOG_TAG = "myLog: ChooseAppointmentFragment";
    private static HospitalApi hospitalApi;
    AppointmentDatesRecyclerViewAdapter adapter;
    List<AppointmentInfo> appointments;
    RecyclerView recyclerView;
    private String doctorId, patientId, hospitalId;
    private OnAppointmentFragmentDataListener mListener;
    private ProgressBar progBarChooseAppointment;

    @Override
    public void onAppointmentDatesItemClick(View view, int position) {
        mListener.onAppointmentFragmentDataListener((AppointmentInfo)adapter.getItem(position));
    }

    public interface OnAppointmentFragmentDataListener {
        void onAppointmentFragmentDataListener(AppointmentInfo appointmentInfo);
    }

    public static ChooseAppointmentFragment newInstance(String doctorId, String hospitalId, String patientId) {
        ChooseAppointmentFragment chooseAppointmentFragment = new ChooseAppointmentFragment();
        Bundle args = new Bundle();
        args.putString("doctor_id", doctorId);
        args.putString("hospital_id", hospitalId);
        args.putString("patient_id", patientId);
        chooseAppointmentFragment.setArguments(args);
        return chooseAppointmentFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppointmentFragmentDataListener) {
            mListener = (OnAppointmentFragmentDataListener) context;
        } else throw new RuntimeException(context.toString() + " must implement OnAppointmentFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorId = getArguments().getString("doctor_id");
        hospitalId = getArguments().getString("hospital_id");
        patientId = getArguments().getString("patient_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_appointment, null);
        progBarChooseAppointment = v.findViewById(R.id.progBarChooseAppointment);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hospitalApi = HospitalController.getApi();
        recyclerView = getView().findViewById(R.id.rvChooseAppointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new AppointmentDatesRecyclerViewAdapter(getContext());
        adapter.setClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAppointmentDates();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_choose_appointment);
    }


    private void getAppointmentDates() {
        hospitalApi.getAppointments(doctorId, hospitalId, patientId, "", "").enqueue(new Callback<AppointmentListApiResponse>() {
            @Override
            public void onResponse(Call<AppointmentListApiResponse> call, Response<AppointmentListApiResponse> response) {
                if (response.isSuccessful()) {
                    AppointmentListApiResponse respObj = response.body();
                    List<AppointmentInfo> infoList;
                    List<AppointmentInfo> appoints = new ArrayList<>();
                    Map<String, List<AppointmentInfo>> resp = respObj.getResponse();
                    if(respObj.getSuccess()) {
                        for (Map.Entry entry : resp.entrySet()) {
                            //Log.d(LOG_TAG, "Дата: " + entry.getKey() + "\n");
                            infoList = (List<AppointmentInfo>) entry.getValue();
                            for (AppointmentInfo info : infoList) {
                                appoints.add(info);
                                //Log.d(LOG_TAG, info.toString() + "\n");
                            }
                        }
                        Collections.sort(appoints);
                        adapter.setData(appoints);
                        recyclerView.setAdapter(adapter);
                        progBarChooseAppointment.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(LOG_TAG, "Ошибка " + respObj.getError().getIdError() + ". " + respObj.getError().getErrorDescription());
                        Toast.makeText(getContext(), respObj.getError().getErrorDescription(), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                    }

                    /*Log.w("Full JSON:\n", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    Gson gson = new Gson();
                    String json = new GsonBuilder().create().toJson(response.body());
                    AppointmentListApiResponse apire = gson.fromJson(json, AppointmentListApiResponse.class);
                    Error error = apire.getError();
                    boolean success = apire.getSuccess();
                    Map<String, List<AppointmentInfo>> responseMap = apire.getResponse();

                    List<AppointmentInfo> infoList;
                    for (Map.Entry entry : responseMap.entrySet()) {
                        infoTV.append("Дата: " + entry.getKey() + "\n");
                        infoList = (List<AppointmentInfo>) entry.getValue();
                        for (AppointmentInfo info : infoList) {
                            infoTV.append(info.toString());
                        }
                        infoTV.append("\n");
                    }*/


                    /*// и это тоже работает!
                    Type tp = new TypeToken<AppointmentListApiResponse>(){}.getType();
                    AppointmentListApiResponse rrr = gson.fromJson(json, tp);

                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap = (Map<String, Object>) gson.fromJson(json, responseMap.getClass());
                    Map datesTreeMap = (LinkedTreeMap) responseMap.get("response");

                    *//*Set set = datesTreeMap.entrySet();
                    Iterator it = set.iterator();
                    Map datesHashMap = new HashMap();
                    while (it.hasNext()) {
                        Map.Entry me = (Map.Entry) it.next();
                        datesHashMap.put(me.getKey(), me.getValue());
                    }*//*

                    //JsonElement jsonElement = gson.toJsonTree(datesHashMap);
                    JsonElement jsonElement = gson.toJsonTree(datesTreeMap);

                    //магия
                    Type type = new TypeToken<Map<String, List<AppointmentInfo>>>(){}.getType();
                    AppointmentInfo obj = gson.fromJson(jsonElement, type);

                    LinkedTreeMap successMap = (LinkedTreeMap) responseMap.get("success");
                    LinkedTreeMap errorMap = (LinkedTreeMap) responseMap.get("error");*/
                }
                else {
                    Toast.makeText(getContext(),"Запрос не прошел (" + response.code() + ")", Toast.LENGTH_LONG).show();
                    //Log.w("Full JSON:\n", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<AppointmentListApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
