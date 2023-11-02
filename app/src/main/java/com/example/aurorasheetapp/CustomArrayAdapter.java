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

    public CustomArrayAdapter(List<Item> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item listItem = listItems.get(position);
        holder.name.setText(String.valueOf(listItem.getName()));
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView dateofpurchase;
        public TextView briefdescription;
        public TextView model;
        public TextView comment;
        public TextView serialnumber;
        public TextView estimatedvalue;
        public TextView make;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            dateofpurchase = (TextView) itemView.findViewById(R.id.dateofpurchase);
            briefdescription = (TextView) itemView.findViewById(R.id.briefdescription);
            model = (TextView) itemView.findViewById(R.id.model);
            comment = (TextView) itemView.findViewById(R.id.comment);
            serialnumber = (TextView) itemView.findViewById(R.id.serialnumber);
            estimatedvalue = (TextView) itemView.findViewById(R.id.estimatedvalue);
            make = (TextView) itemView.findViewById(R.id.make);
        }
    }

}
