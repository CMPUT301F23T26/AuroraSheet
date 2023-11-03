package com.example.aurorasheetapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.ViewHolder> {
    private List<Item> listItems;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public CustomArrayAdapter(List<Item> listItems, RecyclerViewInterface recyclerViewInterface) {
        this.listItems = listItems;
        this.recyclerViewInterface  = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.list_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(itemView, recyclerViewInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item listItem = listItems.get(position);
        holder.dateofpurchase.setText(String.valueOf(listItem.getDateOfPurchase()));
        holder.briefdescription.setText(listItem.getBriefDescription());
        holder.serialnumber.setText(String.valueOf(listItem.getSerialNumber()));
        holder.estimatedvalue.setText(String.valueOf(listItem.getEstimatedValue()));
        holder.make.setText(listItem.getMake());
        holder.comment.setText(listItem.getComment());
        holder.model.setText(listItem.getModel());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView dateofpurchase;
        public TextView briefdescription;
        public TextView model;
        public TextView comment;
        public TextView serialnumber;
        public TextView estimatedvalue;
        public TextView make;

        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            dateofpurchase = (TextView) itemView.findViewById(R.id.dateofpurchase);
            briefdescription = (TextView) itemView.findViewById(R.id.briefdescription);
            model = (TextView) itemView.findViewById(R.id.model);
            comment = (TextView) itemView.findViewById(R.id.comment);
            serialnumber = (TextView) itemView.findViewById(R.id.serialnumber);
            estimatedvalue = (TextView) itemView.findViewById(R.id.estimatedvalue);
            make = (TextView) itemView.findViewById(R.id.make);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
