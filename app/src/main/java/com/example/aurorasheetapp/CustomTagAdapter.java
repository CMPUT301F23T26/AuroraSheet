package com.example.aurorasheetapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomTagAdapter extends RecyclerView.Adapter<CustomTagAdapter.ViewHolder> {
    private ArrayList<Tag> tags;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Tag tag);
        void onItemLongClick(Tag tag);
    }


    public CustomTagAdapter(ArrayList<Tag> tags, Context context, OnItemClickListener listener) {
        this.tags = tags;
        this.context = context;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);

        }

        public void bind(final Tag tag, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tag);
                }

                public void onLongClick(View v) {
                    listener.onItemLongClick(tag);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_list, parent, false);
        return new CustomTagAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = tags.get(position);
        boolean status = tag.getStatus();
        holder.tagName.setText(tag.getName());
        if (status) {
            holder.tagName.setTextColor(Color.WHITE);
            holder.tagName.setBackgroundColor(Color.GREEN);
        } else {
            holder.tagName.setTextColor(Color.BLACK);
            holder.tagName.setBackgroundColor(Color.WHITE);
        }
        holder.bind(tags.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
