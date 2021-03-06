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
import com.venkat.inventory_app.Model.Logs_Model;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.Adapters.User_Logs_Adapter;

public class User_Logs_Frag extends Fragment {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    CollectionReference notebookRef=db.collection("Users").document("Items").collection("Logs "+uid);
    //CollectionReference logref = db.collection("AdminLogs1");
    private User_Logs_Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.user_logs_fragment, container, false);

        Query query = notebookRef.orderBy("timestamp", Query.Direction.DESCENDING);
        /*Query query = logref.whereEqualTo("uid",uid)
                .orderBy("timestamp", Query.Direction.DESCENDING);*/


        FirestoreRecyclerOptions<Logs_Model> options = new FirestoreRecyclerOptions.Builder<Logs_Model>()
                .setQuery(query, Logs_Model.class)
                .build();
        adapter = new User_Logs_Adapter(options) {
            @Override
            public void onDataChanged() {
                // If the latest data has size 0
                // Means there are no items, show the UI State accordingly
                if (adapter.getItemCount() == 0) {
                    // TODO No User Logs: Get an appropriate Drawable
                    ((MainUser_BottomNav) getActivity()).setUiState(R.drawable.ic_history_black_24dp, "There's no activity to show");
                } else {
                    // If there are non-zero items though, hide it
                    // Note that I cannot call hideUiState without the cast operation done ahead of it
                    // getActivity() returns a regular FragmentActivity
                    // So I need to cast it to MainUser_BottomNav
                    // KNOWING that it is the parent activity.
                    ((MainUser_BottomNav) getActivity()).hideUiState();
                }
                super.onDataChanged();
            }
        };

        RecyclerView req_recyclerView = RootView.findViewById(R.id.user_logs_recycler);
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
