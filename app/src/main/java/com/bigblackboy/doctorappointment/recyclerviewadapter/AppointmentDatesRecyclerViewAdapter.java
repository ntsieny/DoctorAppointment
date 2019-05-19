package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.AppointmentInfo;

import java.util.List;

public class AppointmentDatesRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentDatesRecyclerViewAdapter.ViewHolder> {

    private List<AppointmentInfo> mData;
    private LayoutInflater mInflater;
    private OnAppointmentDatesItemClickListener mClickListener;

    // data is passed into the constructor
    public AppointmentDatesRecyclerViewAdapter(Context context, List<AppointmentInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public AppointmentDatesRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<AppointmentInfo> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public AppointmentDatesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.appointment_dates_row, parent, false);
        return new AppointmentDatesRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final AppointmentDatesRecyclerViewAdapter.ViewHolder holder, int position) {
        AppointmentInfo info = mData.get(position);
        if (position > 0 && info.getDateStart().getDay().equals(mData.get(position - 1).getDateStart().getDay())) {
            holder.tvHeaderDate.setVisibility(View.GONE);
        } else {
            holder.tvHeaderDate.setVisibility(View.VISIBLE);
            holder.tvHeaderDate.setText(info.getDateStart().getDay() + " " + info.getDateStart().getMonthVerbose() + ", " + info.getDateStart().getDayVerbose());
        }
        holder.tvItemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) mClickListener.onAppointmentDatesItemClick(v, holder.getAdapterPosition());
            }
        });
        holder.tvItemTime.setText(info.getDateStart().getTime());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderDate, tvItemTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvHeaderDate = itemView.findViewById(R.id.tvHeaderDate);
            tvItemTime = itemView.findViewById(R.id.tvItemTime);
        }

        /*@Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onAppointmentDatesItemClick(view, getAdapterPosition());
        }*/
    }

    // convenience method for getting data at click position
    public AppointmentInfo getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnAppointmentDatesItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnAppointmentDatesItemClickListener {
        void onAppointmentDatesItemClick(View view, int position);
    }
}
