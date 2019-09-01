package com.venkat.inventory_app.Admin;

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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Admin.ExampleDialog;
import com.venkat.inventory_app.Adapters.ItemAdapter;
import com.venkat.inventory_app.Model.Itemshow;
import com.venkat.inventory_app.R;

public class Admin_available_frag extends Fragment {



    private ItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.admin_available_frag, container, false);

        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference notebookRef=db.collection("Notebook");
        final DocumentReference clickRef = db.document("Onclickrv/click");

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

        // swipe to delete
       /* new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);*/


        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Itemshow itemshow_model = documentSnapshot.toObject(Itemshow.class);

                String docu_id = (String) documentSnapshot.getId();
                clickRef.update("docID", docu_id);
                Toast.makeText(getActivity(), "clicked " + docu_id,Toast.LENGTH_SHORT).show();

                Admin_CountUpdate_Dialog admin_countUpdate_dialog = new Admin_CountUpdate_Dialog();
                admin_countUpdate_dialog.show(getFragmentManager(), "count update dialog");

            }
        });

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
