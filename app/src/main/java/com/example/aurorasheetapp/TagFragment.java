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

public class TagFragment extends DialogFragment {
    private EditText tagNameInput;
    private OnFragmentInteractionListener listener;

    private String tagName;
    private Boolean editing = false;
    private Tag tag;
    private String name;

    private String dialogTitle;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Tag newTag, Boolean editing);
        void onCancelPressed();
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
        this.tag = tag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState){
        tagName = "default";
        name = null;
        dialogTitle = "Add New Tag";

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_tag_fragment, null);
        tagNameInput = view.findViewById(R.id.tagName_input);

        if (tag != null){
            name = tag.getName();
            dialogTitle = "Edit Tag";
            editing = true;
            tagNameInput.setHint(tagName);
            tagName = tag.getName();
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
                        Tag newTag = new Tag(name);
                        listener.onOkPressed(newTag, editing);
                    }
                }).create();
    }
}
