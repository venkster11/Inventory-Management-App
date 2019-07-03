package com.venkat.inventory_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Admin.ExampleDialog;
import com.venkat.inventory_app.Admin.ItemAdapter;
import com.venkat.inventory_app.Common.Itemshow;

public class Admin_available_frag extends Fragment {



    private ItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.admin_available_frag, container, false);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference notebookRef=db.collection("Notebook");

        FloatingActionButton addfloat1= RootView.findViewById(R.id.floatadd1);
        addfloat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExampleDialog exampleDialog = new ExampleDialog();
                exampleDialog.show(getFragmentManager(), "example dialog");
            }
        });

        Query query = notebookRef.orderBy("item_name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Itemshow> options = new FirestoreRecyclerOptions.Builder<Itemshow>()
                .setQuery(query, Itemshow.class)
                .build();

        adapter = new ItemAdapter(options);

        RecyclerView recyclerView = RootView.findViewById(R.id.Recyview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);


        return RootView;
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
