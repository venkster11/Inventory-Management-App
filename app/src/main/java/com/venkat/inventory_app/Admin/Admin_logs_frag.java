package com.venkat.inventory_app.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Adapters.Logs_Adapter;
import com.venkat.inventory_app.Model.Logs_Model;
import com.venkat.inventory_app.R;

public class Admin_logs_frag extends Fragment {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    CollectionReference notebookRef=db.collection("AdminLogs");
    private Logs_Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.admin_logs_frag, container, false);

        Query query = notebookRef.orderBy("countitem", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Logs_Model> options = new FirestoreRecyclerOptions.Builder<Logs_Model>()
                .setQuery(query, Logs_Model.class)
                .build();

        adapter=new Logs_Adapter(options);

        RecyclerView req_recyclerView = RootView.findViewById(R.id.admin_logs_recycler);
        req_recyclerView.setHasFixedSize(true);
        req_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_recyclerView.setAdapter(adapter);

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
