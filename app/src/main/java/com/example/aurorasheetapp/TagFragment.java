package com.example.aurorasheetapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class is the fragment for the dialog box that pops up when the user wants to add a new tag
 */
public class TagFragment extends DialogFragment {
    private EditText tagNameInput;
    private OnFragmentInteractionListener listener;

    private String tagName;
    private Boolean editing = false;
    private Tag old_tag;
    private Tag tag;
    private String name;

    private String dialogTitle;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Tag newTag, Tag old_tag, Boolean editing);
        void onCancelPressed();
        void onDeletePressed(Tag tag);
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

    public TagFragment(Tag tag){
        this.old_tag = tag;
    }

    @NonNull
    @Override
    /**
     * This method creates the dialog box that pops up when the user wants to add a new tag
     */
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState){
        tagName = "default";
        name = null;
        dialogTitle = "Add New Tag";

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_tag_fragment, null);
        tagNameInput = view.findViewById(R.id.tagName_input);

        if (old_tag != null){
            name = old_tag.getName();
            tagName = old_tag.getName();
            dialogTitle = "Edit Tag";
            editing = true;
            tagNameInput.setHint(tagName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelPressed();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        while (true){
                            name = tagNameInput.getText().toString();
                            if (editing){
                                if (!name.equals(tagName) && !name.isEmpty()){
                                    break;
                                }
                            } else {
                                if (!name.isEmpty()){
                                    break;
                                }
                            }
                        }
                        tag = new Tag(name);
                        listener.onOkPressed(tag, old_tag, editing);
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeletePressed(old_tag);
                    }
                })
                .create();
    }
}
