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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagItemsFragment extends DialogFragment {
    private TagFragment.OnFragmentInteractionListener listener;


    private ArrayList<Tag> tags;
    private ArrayList<Item> items;
    private Tag tag;
    private Item item;
    private String name;
    private ArrayList<Tag> selected_tags;
    private ArrayList<Tag> original_selected_tags;

    private RecyclerView tagView;
    private CustomTagAdapter tagAdapter;

    private String dialogTitle;


    public interface OnFragmentInteractionListener {
        void onOK_Pressed(ArrayList<Tag> original_selected_tags);
        void onCancel_Pressed();
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (TagFragment.OnFragmentInteractionListener) context;
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
        dialogTitle = "Tag Items";

        selected_tags = new ArrayList<>();
        for (Tag tag : tags){
            if (tag.getStatus()) {
                selected_tags.add(tag);
                original_selected_tags.add(tag);
            }
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tag_items_fragment, null);
        tagView = tagView.findViewById(R.id.tag_View);
        tagAdapter = new CustomTagAdapter(tags, getContext(),
                new CustomTagAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tag tag) {
                        if (selected_tags.contains(tag)){
                            selected_tags.remove(tag);
                            tag.unselect_tag();
                        } else {
                            selected_tags.add(tag);
                            tag.select_tag();
                        }
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
                        listener.onCancel_Pressed();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        }
                        listener.onOk_Pressed();
                    }
                );
    }
}
