package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.view.activity.MainActivity;
import com.bigblackboy.doctorappointment.pojos.hospitalpojos.Doctor;

import java.util.List;

public class DoctorRecyclerViewAdapter extends RecyclerView.Adapter<DoctorRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: DoctorsAdapter";
    private List<Doctor> mData;
    private LayoutInflater mInflater;
    private OnDoctorItemClickListener mListener;
    private Context mContext;

    // data is passed into the constructor
    public DoctorRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<Doctor> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public DoctorRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.doctor_recyclerview_row, parent, false);
        return new DoctorRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final DoctorRecyclerViewAdapter.ViewHolder holder, final int position) {
        Doctor doc = mData.get(position);
        holder.tvDoctorNameItem.setText(doc.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDoctorItemClick(holder.getAdapterPosition());
            }
        });
        if (mContext.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
            holder.tvDoctorItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, holder.tvDoctorItemBtn);
                    popup.inflate(R.menu.doctor_popup_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.doctorReviewsPopupItem:
                                    mListener.onDoctorPopupMenuItemClick(menuItem, holder.getAdapterPosition());
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        } else holder.tvDoctorItemBtn.setVisibility(View.INVISIBLE);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public Doctor getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnDoctorItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnDoctorItemClickListener {
        void onDoctorPopupMenuItemClick(MenuItem item, int position);
        void onDoctorItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorNameItem, tvDoctorItemBtn;

        ViewHolder(View itemView) {
            super(itemView);
            tvDoctorNameItem = itemView.findViewById(R.id.tvDoctorNameItem);
            tvDoctorItemBtn = itemView.findViewById(R.id.tvDoctorItemBtn);
        }
    }
}
