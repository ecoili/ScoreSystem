package com.example.demo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private Context context;
    private List<ScoreItem> scoreList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ScoreAdapter(Context context, List<ScoreItem> scoreList) {
        this.context = context;
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreItem item = scoreList.get(position);

        holder.tvStuId.setText(String.valueOf(item.getStuId()));
        holder.tvStuName.setText(item.getStuName());
        holder.tvGender.setText(item.getGender());
        holder.tvClass.setText(item.getStuClass());
        holder.tvCourse.setText(item.getCourseName());
        holder.tvScore.setText(String.valueOf(item.getScore()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStuId, tvStuName, tvGender, tvClass, tvCourse, tvScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStuId = itemView.findViewById(R.id.tv_stu_id);
            tvStuName = itemView.findViewById(R.id.tv_stu_name);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvCourse = itemView.findViewById(R.id.tv_course);
            tvScore = itemView.findViewById(R.id.tv_score);
        }
    }
}