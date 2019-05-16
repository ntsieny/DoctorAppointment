package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.springserver.springmodel.Appointment;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class AppointmentHistoryRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentHistoryRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: AppHistAdapter";
    private List<Appointment> mData;
    private LayoutInflater mInflater;
    private AppointmentHistoryRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public AppointmentHistoryRecyclerViewAdapter(Context context, List<Appointment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public AppointmentHistoryRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<Appointment> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public AppointmentHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        return new AppointmentHistoryRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final AppointmentHistoryRecyclerViewAdapter.ViewHolder holder, final int position) {
        Appointment app = mData.get(position);
        holder.tvSpecName.setText(app.getSpecName());
        holder.tvDoctorNameAppHist.setText(app.getDocName());
        holder.tvAppDate.setText(DateParser.convertISOtoDateTimeString(app.getDateTime()));
        holder.tvHospitalName.setText(app.getLpuNameShort());
        holder.tvAppHistItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.tvAppHistItemBtn);
                popupMenu.inflate(R.menu.app_hist_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.removeAppHistItem:
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                    .setTitle("Вы уверены?")
                                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (mClickListener != null) mClickListener.onItemClick(menuItem.getItemId(), holder.getAdapterPosition());
                                            removeAt(holder.getAdapterPosition());
                                        }
                                    });
                                builder.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpecName, tvDoctorNameAppHist, tvAppDate, tvHospitalName, tvAppHistItemBtn;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpecName = itemView.findViewById(R.id.tvSpecName);
            tvDoctorNameAppHist = itemView.findViewById(R.id.tvDoctorNameAppHist);
            tvAppDate = itemView.findViewById(R.id.tvAppDate);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvAppHistItemBtn = itemView.findViewById(R.id.tvAppHistItemBtn);
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
        void onItemClick(int viewId, int position);
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }
}
