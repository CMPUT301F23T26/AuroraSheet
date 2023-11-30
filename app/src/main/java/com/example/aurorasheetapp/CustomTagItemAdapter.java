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

/**
 * This class is the adapter for the recycler view. It takes in a list of tags and displays them
 * in the recycler view.
 */
public class CustomTagItemAdapter extends RecyclerView.Adapter<CustomTagItemAdapter.ViewHolder> {
    private ArrayList<Tag> tags;
    private Context context;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(Tag tag);
    }

    /**
     * This method creates a view holder for the recycler view. It inflates the tag_list layout
     * and returns a view holder with the inflated layout.
     * @param tags
     * @param context
     * @param onItemClickListener
     */
    public CustomTagItemAdapter(ArrayList<Tag> tags, Context context,
                            CustomTagItemAdapter.OnItemClickListener onItemClickListener){
        this.context = context;
        this.tags = tags;
        this.onItemClickListener = onItemClickListener;;
    }

    /**
     * This class is the view holder for the recycler view. It holds the tag name.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tag_Name);
        }
    }

    @NonNull
    @Override
    /**
     * This method creates a view holder for the recycler view. It inflates the tag_list layout
     * and returns a view holder with the inflated layout.
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_list, parent, false);
        return new CustomTagItemAdapter.ViewHolder(v);

    }

    @Override
    /**
     * This method binds the data to the view holder. It takes in the view holder and the position
     * of the tag in the list and binds the data to the view holder.
     */
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tag tag = tags.get(position);
        boolean status = tag.getSelect_tagItem();

        holder.tagName.setText(tag.getName());
        if (status) {
            holder.tagName.setTextColor(Color.WHITE);
            holder.tagName.setBackgroundColor(Color.GREEN);
        } else {
            holder.tagName.setTextColor(Color.BLACK);
            holder.tagName.setBackgroundColor(Color.WHITE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(tag);
            }
        });
    }

    @Override
    /**
     * This method returns the number of tags in the list.
     */
    public int getItemCount() {
        return tags.size();
    }

}
