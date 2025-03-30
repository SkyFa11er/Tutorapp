package com.example.tutorapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PostItem postItem);
    }

    private List<PostItem> itemList;
    private OnItemClickListener listener;

    public TutorAdapter(List<PostItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tutor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorAdapter.ViewHolder holder, int position) {
        PostItem post = itemList.get(position);

        if (post.getType() == PostItem.TYPE_DO) {
            TutorInfo t = post.getTutorInfo();
            holder.tvName.setText(t.getName());
            holder.tvIntro.setText(t.getIntro());
            holder.tvSalary.setText("薪資 " + t.getSalary() + " 元/小時");

        } else if (post.getType() == PostItem.TYPE_FIND) {
            FindTutorInfo f = post.getFindTutorInfo();
            holder.tvName.setText(f.getChildName());
            holder.tvIntro.setText("需求科目數：" + f.getSubjects().size());
            holder.tvSalary.setText("薪資 " + f.getSalary() + " 元/小時");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIntro, tvSalary;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvIntro = itemView.findViewById(R.id.tv_intro);
            tvSalary = itemView.findViewById(R.id.tv_salary);
        }
    }
}
