package com.venkat.inventory_app.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Model.Borrowed_Model;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.Adapters.User_Borrowed_Adapter;

public class User_Borrowed_Frag extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection(uid);
    private User_Borrowed_Adapter adapter;
    public Number realcount1=0;
    public int realcount=0;
    private String title;

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

        adapter.setOnItemClickListener(new User_Borrowed_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Borrowed_Model note = documentSnapshot.toObject(Borrowed_Model.class);
                String user_docu_id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                String itmname = documentSnapshot.getString("item_name");
                String docuid=documentSnapshot.getString("docuID");
                Number mycount= (Long) documentSnapshot.get("mycount");


                DocumentReference realcountdb = db.collection("Notebook").document(docuid);
                realcountdb.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, e.toString());
                            return;
                        }
                        if (documentSnapshot.exists()) {
                            realcount1 = (Long) documentSnapshot.get("count");
                            realcount = realcount1.intValue();

                        }

                    }
                });



                Bundle args = new Bundle();
                args.putString("key", itmname);
                args.putString("docuid",docuid);
                args.putLong("mycount", (Long) mycount);
               args.putLong("realcount",  realcount);
               args.putString("userdoc",user_docu_id);
               args.putString("uid",uid);

                DialogFragment newFragment = new User_Return_Dialog();
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "TAG");

                Toast.makeText(getActivity(),
                        "do " + title + " actual count " + realcount, Toast.LENGTH_SHORT).show();

                User_Return_Dialog user_return_dialog = new User_Return_Dialog();
                user_return_dialog.show(getFragmentManager(), "return dialog");
            }
        });

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
