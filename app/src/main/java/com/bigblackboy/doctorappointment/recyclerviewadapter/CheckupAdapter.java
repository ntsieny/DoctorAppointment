package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.springpojos.HealthInfo;
import com.bigblackboy.doctorappointment.pojos.springpojos.Procedure;

import java.util.List;

public class CheckupAdapter extends RecyclerView.Adapter<CheckupAdapter.ViewHolder> {

    private List<Procedure> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public CheckupAdapter(Context context, List<Procedure> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public CheckupAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Procedure> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public CheckupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.procedure_row, parent, false);
        return new CheckupAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(CheckupAdapter.ViewHolder holder, int position) {
        Procedure proc = mData.get(position);
        if (proc.getType() == Procedure.FIRST_STEP_TYPE) {
            if (position == 0) {
                holder.tvProcedureType.setText("1-й этап диспансеризации:");
            } else holder.tvProcedureType.setVisibility(View.GONE);
        } else if (proc.getType() == Procedure.SECOND_STEP_TYPE) {
            if (mData.get(position - 1).getType() == Procedure.FIRST_STEP_TYPE) {
                holder.tvProcedureType.setText("2-й этап диспансеризации:");
            } else holder.tvProcedureType.setVisibility(View.GONE);
        }
        holder.tvProcedureName.setText(proc.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProcedureType, tvProcedureName;

        ViewHolder(View itemView) {
            super(itemView);
            tvProcedureType = itemView.findViewById(R.id.tvProcedureType);
            tvProcedureName = itemView.findViewById(R.id.tvProcedureName);
        }
    }
}
