package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.model.springmodel.Review;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class UserReviewsRecyclerViewAdapter extends RecyclerView.Adapter<UserReviewsRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: UserReviewAdapter";
    private List<Review> mData;
    private LayoutInflater mInflater;
    private UserReviewsRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private String serviceId;

    // data is passed into the constructor
    public UserReviewsRecyclerViewAdapter(Context context, List<Review> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public UserReviewsRecyclerViewAdapter(Context context, String serviceId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.serviceId = serviceId;
    }

    public void setData(List<Review> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public UserReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.user_review_row, parent, false);
        return new UserReviewsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final UserReviewsRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Review rev = mData.get(position);
        holder.tvDoctorNameReview.setText(rev.getDoctorName());
        holder.tvDateReview.setText(DateParser.convertISOwithMillistoDateTimeString(rev.getDateTime()));
        holder.tvLpuNameReview.setText(rev.getLpuName());
        holder.rBarReview.setRating(rev.getMark());
        holder.tvReviewText.setText(rev.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onUserReviewsItemClick(v, holder.getAdapterPosition());
            }
        });
        holder.imBtnEditReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onUserReviewsButtonClick(v, holder.getAdapterPosition());
            }
        });
        holder.imBtnDeleteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("Удалить отзыв?")
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mClickListener.onUserReviewsButtonClick(v, holder.getAdapterPosition());
                                removeAt(holder.getAdapterPosition());
                            }
                        });
                builder.show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public Review getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(UserReviewsRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onUserReviewsButtonClick(View v, int position);
        void onUserReviewsItemClick(View v, int position);
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctorNameReview, tvLpuNameReview, tvDateReview, tvReviewText;
        RatingBar rBarReview;
        ImageButton imBtnEditReview, imBtnDeleteReview;

        ViewHolder(View itemView) {
            super(itemView);
            tvDoctorNameReview = itemView.findViewById(R.id.tvDoctorNameReview);
            tvDateReview = itemView.findViewById(R.id.tvDateReview);
            tvLpuNameReview = itemView.findViewById(R.id.tvLpuNameReview);
            rBarReview = itemView.findViewById(R.id.rBarReview);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            imBtnEditReview = itemView.findViewById(R.id.imBtnEditReview);
            imBtnDeleteReview = itemView.findViewById(R.id.imBtnDeleteReview);
        }
    }
}
