package com.example.aurorasheetapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagItemsFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;


    private ArrayList<Tag> tags;
    private ArrayList<Item> items;
    private ArrayList<Tag> selected_tags;

    private RecyclerView tagView;
    private CustomTagAdapter tagAdapter;

    private String dialogTitle;


    public interface OnFragmentInteractionListener {
        void onOK_Pressed(ArrayList<Tag> selected_tags);
        void onCancel_Pressed();
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context
                    + "must implement OnFragmentInteractionListener");
        }
    }

    public TagItemsFragment(ArrayList<Tag> tags, ArrayList<Item> items){
        this.tags = tags;
        this.items = items;
    }

    @NonNull
    @Override
    /**
     * This method creates the dialog box that pops up when the user wants to add a new tag
     */
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState){
        dialogTitle = "Select Tags:";

        selected_tags = new ArrayList<>();
        for (Tag tag : tags){
            if (tag.getStatus()) {
                tag.select_tagItem();
                selected_tags.add(tag);
            }
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_items_fragment, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        tagView = view.findViewById(R.id.tag_Item_View);
        tagView.setLayoutManager(layoutManager);
        tagView.setHasFixedSize(true);
        tagAdapter = new CustomTagAdapter(tags, getContext(),
                new CustomTagAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tag tag) {
                        if (selected_tags.contains(tag)){
                            selected_tags.remove(tag);
                            tag.unselect_tagItem();
                        } else {
                            selected_tags.add(tag);
                            tag.select_tagItem();
                        }
                        tagAdapter.notifyDataSetChanged();
                    }
                }, null);
        tagView.setAdapter(tagAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Tag tag : tags){
                            tag.unselect_tagItem();
                        }
                        listener.onCancel_Pressed();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Tag tag : selected_tags){
                            for (Item item : items){
                                tag.tagItem(item);
                            }
                        }
                        for (Tag tag : tags){
                            if (!selected_tags.contains(tag)){
                                for (Item item : items){
                                    tag.untagItem(item);
                                }
                            }
                        }
                        selected_tags.clear();
                        for (Tag tag : tags){
                            tag.unselect_tagItem();
                            if (tag.getStatus()){
                                selected_tags.add(tag);
                            }
                        }
                        listener.onOK_Pressed(selected_tags);
                    }
                })
                .create();

    }
}
