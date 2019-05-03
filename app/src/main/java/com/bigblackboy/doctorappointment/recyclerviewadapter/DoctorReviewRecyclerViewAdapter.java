package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class DoctorReviewRecyclerViewAdapter extends RecyclerView.Adapter<DoctorReviewRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private static final String LOG_TAG = "myLog: DocReviewAdapter";
    private List<ReviewResponse> mData;
    private LayoutInflater mInflater;
    private DoctorReviewRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public DoctorReviewRecyclerViewAdapter(Context context, List<ReviewResponse> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public DoctorReviewRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<ReviewResponse> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public DoctorReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.doctor_review_recyclerview_row, parent, false);
        return new DoctorReviewRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final DoctorReviewRecyclerViewAdapter.ViewHolder holder, final int position) {
        ReviewResponse rev = mData.get(position);
        holder.tvAuthorNameReview.setText(String.format("%s %s.", rev.getLastname(), rev.getName().substring(0,1)));
        holder.tvDateReview.setText(DateParser.convertISOwithMillistoDateTimeString(rev.getDateTime()));
        holder.tvLikeCounterReview.setText(String.valueOf(rev.getLikes()));
        holder.tvDislikeCounterReview.setText(String.valueOf(rev.getDislikes()));
        holder.tvCommentCounterReview.setText(String.valueOf(rev.getCommentCount()));
        holder.rBarReview.setRating(rev.getMark());
        holder.tvReviewText.setText(rev.getText());
        holder.imBtnLike.setOnClickListener(this);
        holder.imBtnDislike.setOnClickListener(this);
        holder.imBtnComments.setOnClickListener(this);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imBtnLike:

                break;
            case R.id.imBtnDislike:

                break;
            case R.id.imBtnComments:

                break;
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorNameReview, tvDateReview, tvLikeCounterReview, tvDislikeCounterReview, tvCommentCounterReview, tvReviewText;
        RatingBar rBarReview;
        ImageButton imBtnLike, imBtnDislike, imBtnComments;

        ViewHolder(View itemView) {
            super(itemView);
            tvAuthorNameReview = itemView.findViewById(R.id.tvAuthorNameReview);
            tvDateReview = itemView.findViewById(R.id.tvDateReview);
            tvLikeCounterReview = itemView.findViewById(R.id.tvLikeCounterReview);
            tvDislikeCounterReview = itemView.findViewById(R.id.tvDislikeCounterReview);
            tvCommentCounterReview = itemView.findViewById(R.id.tvCommentCounterReview);
            rBarReview = itemView.findViewById(R.id.rBarReview);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            imBtnLike = itemView.findViewById(R.id.imBtnLike);
            imBtnDislike = itemView.findViewById(R.id.imBtnDislike);
            imBtnComments = itemView.findViewById(R.id.imBtnComments);
        }
    }

    // convenience method for getting data at click position
    public ReviewResponse getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(DoctorReviewRecyclerViewAdapter.ItemClickListener itemClickListener) {
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
