package com.venkat.inventory_app.User;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Borrowed_Model;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User_Borrowed_Adapter;

public class User_Borrowed_Frag extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection(uid);
    private User_Borrowed_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_borrowed_fragment, container, false);

        Query query = notebookRef.orderBy("item_name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Borrowed_Model> options = new FirestoreRecyclerOptions.Builder<Borrowed_Model>()
                .setQuery(query, Borrowed_Model.class)
                .build();

        adapter = new User_Borrowed_Adapter(options);
        RecyclerView recyclerViewu = rootView.findViewById(R.id.borrowed_recyview);
        recyclerViewu.setHasFixedSize(true);
        recyclerViewu.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewu.setAdapter(adapter);

        return rootView;
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
