package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.springserver.springmodel.CommentResponse;
import com.bigblackboy.doctorappointment.springserver.springmodel.ReviewResponse;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: CommentsAdapter";
    private List<CommentResponse> mData;
    private LayoutInflater mInflater;
    private CommentRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private String serviceId;

    // data is passed into the constructor
    public CommentRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }
    public CommentRecyclerViewAdapter(Context context, String serviceId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.serviceId = serviceId;
    }

    public void setData(List<CommentResponse> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.comment_recyclerview_row, parent, false);
        return new CommentRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final CommentRecyclerViewAdapter.ViewHolder holder, final int position) {
        final CommentResponse com = mData.get(position);
        holder.tvAuthorNameComment.setText(String.format("%s %s.", com.getLastname(), com.getName().substring(0,1)));
        holder.tvDateComment.setText(DateParser.convertISOwithMillistoDateTimeString(com.getDateTime()));
        holder.tvLikeCounterComment.setText(String.valueOf(com.getLikes()));
        holder.tvDislikeCounterComment.setText(String.valueOf(com.getDislikes()));
        holder.tvCommentText.setText(com.getText());
        holder.chbLikeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onButtonClick(v, holder.getAdapterPosition());
                if (holder.chbDislikeComment.isChecked()) {
                    holder.chbDislikeComment.setChecked(false);
                    holder.tvDislikeCounterComment.setText(decrementStringVal(holder.tvDislikeCounterComment.getText().toString()));
                }

                int likes = Integer.valueOf(holder.tvLikeCounterComment.getText().toString());
                if (holder.chbLikeComment.isChecked()) {
                    holder.tvLikeCounterComment.setText(String.valueOf(++likes));
                } else holder.tvLikeCounterComment.setText(String.valueOf(--likes));
            }
        });
        holder.chbDislikeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onButtonClick(v, holder.getAdapterPosition());
                if (holder.chbLikeComment.isChecked()) {
                    holder.chbLikeComment.setChecked(false);
                    holder.tvLikeCounterComment.setText(decrementStringVal(holder.tvLikeCounterComment.getText().toString()));
                }

                int dislikes = Integer.valueOf(holder.tvDislikeCounterComment.getText().toString());
                if (holder.chbDislikeComment.isChecked()) {
                    holder.tvDislikeCounterComment.setText(String.valueOf(++dislikes));
                } else holder.tvDislikeCounterComment.setText(String.valueOf(--dislikes));
            }
        });

        holder.imBtnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("Удалить комментарий?")
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(mClickListener != null) mClickListener.onButtonClick(v, holder.getAdapterPosition());
                                removeAt(holder.getAdapterPosition());
                            }
                        });
                builder.show();
            }
        });

        if (com.getLikers() != null && com.getLikers().contains(serviceId)) {
            holder.chbLikeComment.setChecked(true);
        } else if (com.getDislikers() != null && com.getDislikers().contains(serviceId)) {
            holder.chbDislikeComment.setChecked(true);
        }

        if (com.getServiceId().equals(serviceId)) {
            holder.imBtnDeleteComment.setVisibility(View.VISIBLE);
        } else holder.imBtnDeleteComment.setVisibility(View.GONE);
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
    public CommentResponse getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(CommentRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onButtonClick(View v, int position);
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorNameComment, tvDateComment, tvLikeCounterComment, tvDislikeCounterComment, tvCommentText;
        CheckBox chbLikeComment, chbDislikeComment;
        ImageButton imBtnDeleteComment;

        ViewHolder(View itemView) {
            super(itemView);
            tvAuthorNameComment = itemView.findViewById(R.id.tvAuthorNameComment);
            tvDateComment = itemView.findViewById(R.id.tvDateComment);
            tvLikeCounterComment = itemView.findViewById(R.id.tvLikeCounterComment);
            tvDislikeCounterComment = itemView.findViewById(R.id.tvDislikeCounterComment);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            chbLikeComment = itemView.findViewById(R.id.chbLikeComment);
            chbDislikeComment = itemView.findViewById(R.id.chbDislikeComment);
            imBtnDeleteComment = itemView.findViewById(R.id.imBtnDeleteComment);
        }
    }
}
