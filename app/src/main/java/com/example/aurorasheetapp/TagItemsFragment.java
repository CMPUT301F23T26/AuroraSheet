package com.example.aurorasheetapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagItemsFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;


    private ArrayList<Tag> tags;
    private ArrayList<Item> items;
    private ArrayList<Tag> selected_tags;

    private RecyclerView tagView;
    private CustomTagAdapter tagAdapter;

    private String dialogTitle;
    private Context context;

    private FirebaseFirestore firestore;


    public interface OnFragmentInteractionListener {
        void onOK_Pressed(ArrayList<Tag> selected_tags);
        void onCancel_Pressed();
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else{
            throw new RuntimeException(context
                    + "must implement OnFragmentInteractionListener");
        }
    }

    private void db_add_tagItem(Tag tag, Item item){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(context, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = item.getName();
        String description = item.getBriefDescription();
        String date = item.getDateOfPurchase().toString();
        Double value = item.getEstimatedValue();
        Double serial = item.getSerialNumber();
        String make = item.getMake();
        String model = item.getModel();
        String comment = item.getComment();
        Map<String, Object> newItem = new HashMap<>();
        newItem.put("name", name);
        newItem.put("description", description);
        newItem.put("date", date);
        newItem.put("value", value);
        newItem.put("serial", serial);
        newItem.put("make", make);
        newItem.put("model", model);
        newItem.put("comment", comment);
        firestore.collection("users")
                .document(currentUser.getUid())
                .collection("tags")
                .document(tag.getDocumentID())
                .collection("tagged_items")
                .add(newItem)
                .addOnSuccessListener(documentReference -> item.setTaggedDocumentId(documentReference.getId()))
                .addOnFailureListener(e -> Toast.makeText(context, "Error adding tagged item", Toast.LENGTH_SHORT).show());
    }

    private void db_delete_tagItem(Tag tag, Item item){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tag.getDocumentID() != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("tags")
                    .document(tag.getDocumentID())
                    .collection("tagged_items")
                    .document(item.getTaggedDocumentId())
                    .delete()
                    .addOnSuccessListener(documentReference -> Toast.makeText(context, "Tagged item deleted", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Error deleting tagged item", Toast.LENGTH_SHORT).show());
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
        firestore = FirebaseFirestore.getInstance();
        dialogTitle = "Select Tags:";

        selected_tags = new ArrayList<>();
        for (Tag tag : tags){
            if (tag.getStatus()) {
                tag.select_tagItem();
                tag.setTmp_status(true);
                selected_tags.add(tag);
                tag.setStatus(false);
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
                            tag.setStatus(tag.getTmp_status());
                        }
                        listener.onCancel_Pressed();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Tag tag : selected_tags){
                            for (Item item : items){
                                if (!tag.getTagged_items().contains(item)) {
                                    tag.tagItem(item);
                                    db_add_tagItem(tag, item);
                                }
                            }
                        }
                        for (Tag tag : tags){
                            tag.setStatus(tag.getTmp_status());
                            if (!selected_tags.contains(tag)){
                                for (Item item : items) {
                                    if (tag.getTagged_items().contains(item)){
                                        tag.untagItem(item);
                                        db_delete_tagItem(tag, item);
                                    }
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
