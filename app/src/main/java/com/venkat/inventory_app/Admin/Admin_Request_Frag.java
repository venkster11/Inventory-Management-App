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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User.Request_Model;
import com.venkat.inventory_app.User.User_ItemAdapter;

public class Admin_Request_Frag extends Fragment {

    String id;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Requests");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Admin_Request_Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.request_recycler, null);

        Query query = notebookRef.orderBy("nameitem", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Request_Model> options = new FirestoreRecyclerOptions.Builder<Request_Model>()
                .setQuery(query, Request_Model.class)
                .build();

        adapter= new Admin_Request_Adapter(options);

        RecyclerView req_recyclerView = rootView.findViewById(R.id.request_recyclerview);
        req_recyclerView.setHasFixedSize(true);
        req_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Admin_Request_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Request_Model request_model = documentSnapshot.toObject(Admin_Request_Adapter.class);
                Request_Model request_model = documentSnapshot.toObject(Request_Model.class);
                id = documentSnapshot.getId();
               // String uid = user.getUid();
              //  String docu_id = documentSnapshot.getString("docu_id");
                String value=documentSnapshot.getString("nameitem");


                Toast.makeText(getActivity(), "clicked " + value,Toast.LENGTH_SHORT).show();
            }
        });

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
