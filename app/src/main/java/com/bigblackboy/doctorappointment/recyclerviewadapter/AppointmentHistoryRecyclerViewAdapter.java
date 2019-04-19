package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.springserver.springmodel.Appointment;
import com.bigblackboy.doctorappointment.utils.DateParser;
import com.google.gson.internal.bind.util.ISO8601Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class AppointmentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentHistoryRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: AppHistAdapter";
    private List<Appointment> mData;
    private LayoutInflater mInflater;
    private AppointmentHistoryRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public AppointmentHistoryRecyclerViewAdapter(Context context, List<Appointment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public AppointmentHistoryRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Appointment> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public AppointmentHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        return new AppointmentHistoryRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AppointmentHistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        Appointment app = mData.get(position);
        holder.tvSpecName.setText(app.getSpecName());
        holder.tvDoctorName.setText(app.getDocName());
        holder.tvAppDate.setText(DateParser.convertISOtoDateTimeString(app.getDateTime()));
        holder.tvHospitalName.setText(app.getLpuNameShort());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSpecName, tvDoctorName, tvAppDate, tvHospitalName;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpecName = itemView.findViewById(R.id.tvSpecName);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvAppDate = itemView.findViewById(R.id.tvAppDate);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Appointment getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(AppointmentHistoryRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
