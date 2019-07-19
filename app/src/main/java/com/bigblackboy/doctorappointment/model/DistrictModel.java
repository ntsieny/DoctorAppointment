package com.bigblackboy.doctorappointment.model;

import android.os.AsyncTask;

import com.bigblackboy.doctorappointment.pojos.hospitalpojos.District;
import com.bigblackboy.doctorappointment.utils.HtmlParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DistrictModel {

    private ArrayList<District> districts;
    private District district;

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public void getDistricts(OnFinishedListener onFinishedListener) {
        AsyncRequest request = new AsyncRequest();
        request.execute();
        try {
            districts = request.get();
            onFinishedListener.onFinished(districts);
        } catch (ExecutionException e) {
            onFinishedListener.onFailure(new Throwable(e));
            e.printStackTrace();
        } catch (InterruptedException e) {
            onFinishedListener.onFailure(new Throwable(e));
            e.printStackTrace();
        }
    }

    class AsyncRequest extends AsyncTask<Void, Void, ArrayList<District>> {
        @Override
        protected ArrayList<District> doInBackground(Void... voids) {
            return new HtmlParser().getDistricts();
        }
    }

    public interface OnFinishedListener {
        void onFinished(ArrayList<District> districts);

        void onFailure(Throwable t);
    }
}
