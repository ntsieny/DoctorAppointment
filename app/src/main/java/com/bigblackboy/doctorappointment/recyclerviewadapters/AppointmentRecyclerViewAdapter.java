package com.bigblackboy.doctorappointment.recyclerviewadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;

import java.util.List;

public class AppointmentRecyclerViewAdapter<T> extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder> {

    private List<T> mData;
    private LayoutInflater mInflater;
    private AppointmentRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public AppointmentRecyclerViewAdapter(Context context, List<T> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public AppointmentRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public AppointmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new AppointmentRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AppointmentRecyclerViewAdapter.ViewHolder holder, int position) {
        String itemText = mData.get(position).toString();
        holder.myTextView.setText(itemText);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public T getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(AppointmentRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
