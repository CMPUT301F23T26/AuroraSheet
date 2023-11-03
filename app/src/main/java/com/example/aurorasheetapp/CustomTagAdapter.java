package com.example.aurorasheetapp;

import android.content.Context;
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

    public CustomTagAdapter(ArrayList<Tag> tags, Context context) {
        this.tags = tags;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // do something when tag is clicked
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
        holder.tagName.setText(tag.getName());

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
