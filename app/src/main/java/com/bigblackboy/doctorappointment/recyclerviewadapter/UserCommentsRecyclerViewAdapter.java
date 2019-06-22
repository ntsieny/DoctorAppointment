package com.bigblackboy.doctorappointment.recyclerviewadapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bigblackboy.doctorappointment.R;
import com.bigblackboy.doctorappointment.pojos.springpojos.MyCommentsResponse;
import com.bigblackboy.doctorappointment.utils.DateParser;

import java.util.List;

public class UserCommentsRecyclerViewAdapter extends RecyclerView.Adapter<UserCommentsRecyclerViewAdapter.ViewHolder> {

    private static final String LOG_TAG = "myLog: UserReviewAdapter";
    private List<MyCommentsResponse> mData;
    private LayoutInflater mInflater;
    private OnUserCommentsItemClickListener mClickListener;
    private Context mContext;
    private String serviceId;

    // data is passed into the constructor
    public UserCommentsRecyclerViewAdapter(Context context, List<MyCommentsResponse> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    public UserCommentsRecyclerViewAdapter(Context context, String serviceId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.serviceId = serviceId;
    }

    public void setData(List<MyCommentsResponse> data) {
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public UserCommentsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // в чем разница?
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_history_recyclerview_row, parent, false);
        View view = mInflater.inflate(R.layout.user_comment_row, parent, false);
        return new UserCommentsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final UserCommentsRecyclerViewAdapter.ViewHolder holder, final int position) {
        final MyCommentsResponse com = mData.get(position);
        holder.tvAuthorNameComment.append(String.format("%s %s.", com.getRevAuthorLastname(), com.getRevAuthorName().substring(0,1)));
        holder.tvDoctorName.setText(com.getDoctorName());
        holder.tvDateComment.setText(DateParser.convertISOwithMillistoDateTimeString(com.getDateTime()));
        holder.tvCommentText.setText(com.getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onUserCommentsItemClick(v, holder.getAdapterPosition());
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
                                mClickListener.onUserCommentsButtonClick(v, holder.getAdapterPosition());
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
    public MyCommentsResponse getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnUserCommentsItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnUserCommentsItemClickListener {
        void onUserCommentsButtonClick(View v, int position);
        void onUserCommentsItemClick(View v, int position);
    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorNameComment, tvDoctorName, tvDateComment, tvCommentText;
        ImageButton imBtnDeleteComment;

        ViewHolder(View itemView) {
            super(itemView);
            tvAuthorNameComment = itemView.findViewById(R.id.tvAuthorNameComment);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDateComment = itemView.findViewById(R.id.tvDateComment);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            imBtnDeleteComment = itemView.findViewById(R.id.imBtnDeleteComment);
        }
    }
}
