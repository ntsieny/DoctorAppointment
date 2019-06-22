package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.api.ScheduleApiResponse;

import java.util.List;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private List<ScheduleApiResponse.MResponse> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ScheduleRecyclerViewAdapter(Context context, List<ScheduleApiResponse.MResponse> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public ScheduleRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<ScheduleApiResponse.MResponse> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ScheduleRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ScheduleRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ScheduleRecyclerViewAdapter.ViewHolder holder, int position) {
        ScheduleApiResponse.MResponse resp = mData.get(position);
        holder.tvRow.setGravity(Gravity.CENTER_HORIZONTAL);
        holder.tvRow.setText(resp.toString());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRow;

        ViewHolder(View itemView) {
            super(itemView);
            tvRow = itemView.findViewById(R.id.tvRow);
        }
    }

    // convenience method for getting data at click position
    public ScheduleApiResponse.MResponse getItem(int id) {
        return mData.get(id);
    }
}
