package com.example.aurorasheetapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This class is the adapter for the recycler view. It takes in a list of items and
 * displays them in the recycler view.
 */
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
    /**
     * This method creates a view holder for the recycler view. It inflates the list_item layout
     * and returns a view holder with the inflated layout.
     */
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.list_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(itemView, recyclerViewInterface);
        return viewHolder;
    }

    @Override
    /**
     * This method binds the data to the view holder. It takes in the view holder and the position
     * of the item in the list and binds the data to the view holder.
     */
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

        //Log.d("customArrayAdapter","the binding thing got activated");
        if (listItem.getSelection()) {
            holder.background.setBackgroundColor(Color.argb(255, 255, 240, 255));
        } else {
            holder.background.setBackgroundColor(Color.argb(255,225,240,255));
        }

    }

    @Override
    /**
     * This method returns the number of items in the list.
     */
    public int getItemCount() {
        return listItems.size();
    }

    /**
     * This class is the view holder for the recycler view. It holds the views for the list_item
     * layout.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView dateofpurchase;
        public TextView briefdescription;
        public TextView model;
        public TextView comment;
        public TextView serialnumber;
        public TextView estimatedvalue;
        public TextView make;

        public LinearLayout background;

        /**
         * This constructor takes in a view and a recycler view interface. It sets the views to the
         * views in the list_item layout.
         * @param itemView
         * @param recyclerViewInterface
         */
        public ViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            dateofpurchase = (TextView) itemView.findViewById(R.id.dateofpurchase);
            briefdescription = (TextView) itemView.findViewById(R.id.briefdescription);
            model = (TextView) itemView.findViewById(R.id.model);
            comment = (TextView) itemView.findViewById(R.id.comment);
            serialnumber = (TextView) itemView.findViewById(R.id.serialnumber);
            estimatedvalue = (TextView) itemView.findViewById(R.id.estimatedvalue);
            make = (TextView) itemView.findViewById(R.id.make);
            background = (LinearLayout) itemView.findViewById(R.id.Background);

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
