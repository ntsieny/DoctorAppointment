package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.hospitalmodel.Hospital;

import java.util.List;

public class HospitalRecyclerViewAdapter extends RecyclerView.Adapter<HospitalRecyclerViewAdapter.ViewHolder> {

    private List<Hospital> mData;
    private LayoutInflater mInflater;
    private HospitalRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public HospitalRecyclerViewAdapter(Context context, List<Hospital> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public HospitalRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Hospital> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public HospitalRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hospital_recyclerview_row, parent, false);
        return new HospitalRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(HospitalRecyclerViewAdapter.ViewHolder holder, int position) {
        Hospital hospital = mData.get(position);
        holder.hospitalName.setText(hospital.getLpuName());
        holder.hospitalAddress.setText(getShortAddress(hospital.getFullAddress()));
    }

    private String getShortAddress(String fullAddress) {
        for (int i = 0; i < fullAddress.length(); i++) {
            if (fullAddress.charAt(i) == ' ') {
                return fullAddress.substring(i + 1);
            }
        }
        return "";
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView hospitalName, hospitalAddress;

        ViewHolder(View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.tvHospitalName);
            hospitalAddress = itemView.findViewById(R.id.tvHospitalAddress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Hospital getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(HospitalRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
