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
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User.Request_Model;
import com.venkat.inventory_app.User.User_ItemAdapter;

public class Admin_Request_Frag extends Fragment {

    private Admin_Request_Adapter adapter;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Requests");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_recycler, null);

        Query query = notebookRef.orderBy("timestamp", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Request_Model> options = new FirestoreRecyclerOptions.Builder<Request_Model>()
                .setQuery(query, Request_Model.class)
                .build();

        adapter= new Admin_Request_Adapter(options);

        RecyclerView req_recyclerView = rootView.findViewById(R.id.request_recyclerview);
        req_recyclerView.setHasFixedSize(true);
        req_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_recyclerView.setAdapter(adapter);

        return rootView;
    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
