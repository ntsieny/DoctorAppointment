package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class DoctorReviewRecyclerViewAdapter extends RecyclerView.Adapter<DoctorReviewRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: DocReviewAdapter";
    private List<ReviewResponse> mData;
    private LayoutInflater mInflater;
    private DoctorReviewRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private String serviceId;

    // data is passed into the constructor
    public DoctorReviewRecyclerViewAdapter(Context context, List<ReviewResponse> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public DoctorReviewRecyclerViewAdapter(Context context, String serviceId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.serviceId = serviceId;
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
        final ReviewResponse rev = mData.get(position);
        holder.tvAuthorNameReview.setText(String.format("%s %s.", rev.getLastname(), rev.getName().substring(0,1)));
        holder.tvDateReview.setText(DateParser.convertISOwithMillistoDateTimeString(rev.getDateTime()));
        holder.tvLikeCounterReview.setText(String.valueOf(rev.getLikes()));
        holder.tvDislikeCounterReview.setText(String.valueOf(rev.getDislikes()));
        holder.tvCommentCounterReview.setText(String.valueOf(rev.getCommentCount()));
        holder.rBarReview.setRating(rev.getMark());
        holder.tvReviewText.setText(rev.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO открываем fragment подробного просмотра отзыва
                Toast.makeText(mContext, "нажат itemview " + rev.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.chbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, holder.getAdapterPosition());
                if (holder.chbDislike.isChecked()) {
                    holder.chbDislike.setChecked(false);
                    holder.tvDislikeCounterReview.setText(decrementStringVal(holder.tvDislikeCounterReview.getText().toString()));
                }

                int likes = Integer.valueOf(holder.tvLikeCounterReview.getText().toString());
                if (holder.chbLike.isChecked()) {
                    holder.tvLikeCounterReview.setText(String.valueOf(++likes));
                } else holder.tvLikeCounterReview.setText(String.valueOf(--likes));
            }
        });
        holder.chbDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, holder.getAdapterPosition());
                if (holder.chbLike.isChecked()) {
                    holder.chbLike.setChecked(false);
                    holder.tvLikeCounterReview.setText(decrementStringVal(holder.tvLikeCounterReview.getText().toString()));
                }

                int dislikes = Integer.valueOf(holder.tvDislikeCounterReview.getText().toString());
                if (holder.chbDislike.isChecked()) {
                    holder.tvDislikeCounterReview.setText(String.valueOf(++dislikes));
                } else holder.tvDislikeCounterReview.setText(String.valueOf(--dislikes));
            }
        });
        holder.imBtnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });

        if (rev.getLikers() != null && rev.getLikers().contains(serviceId)) {
            holder.chbLike.setChecked(true);
        } else if (rev.getDislikers() != null && rev.getDislikers().contains(serviceId)) {
            holder.chbDislike.setChecked(true);
        }
    }

    private String decrementStringVal(String str) {
        return String.valueOf(Integer.parseInt(str) - 1);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public ReviewResponse getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(DoctorReviewRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorNameReview, tvDateReview, tvLikeCounterReview, tvDislikeCounterReview, tvCommentCounterReview, tvReviewText;
        RatingBar rBarReview;
        ImageButton imBtnComments;
        CheckBox chbLike, chbDislike;

        ViewHolder(View itemView) {
            super(itemView);
            tvAuthorNameReview = itemView.findViewById(R.id.tvAuthorNameReview);
            tvDateReview = itemView.findViewById(R.id.tvDateReview);
            tvLikeCounterReview = itemView.findViewById(R.id.tvLikeCounterReview);
            tvDislikeCounterReview = itemView.findViewById(R.id.tvDislikeCounterReview);
            tvCommentCounterReview = itemView.findViewById(R.id.tvCommentCounterReview);
            rBarReview = itemView.findViewById(R.id.rBarReview);
            tvReviewText = itemView.findViewById(R.id.tvReviewText);
            chbLike = itemView.findViewById(R.id.chbLike);
            chbDislike = itemView.findViewById(R.id.chbDislike);
            imBtnComments = itemView.findViewById(R.id.imBtnComments);
        }
    }
}
