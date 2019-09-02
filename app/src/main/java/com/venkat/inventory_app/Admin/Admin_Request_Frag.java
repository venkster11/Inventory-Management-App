package com.venkat.inventory_app.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Adapters.Admin_Request_Adapter;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.Model.Request_Model;

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DocumentReference clickRef = db.document("Onclickrv/click");
        Query query = notebookRef.orderBy("nameitem", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Request_Model> options = new FirestoreRecyclerOptions.Builder<Request_Model>()
                .setQuery(query, Request_Model.class)
                .build();

        adapter= new Admin_Request_Adapter(options);

        RecyclerView req_recyclerView = rootView.findViewById(R.id.request_recyclerview);
        req_recyclerView.setHasFixedSize(true);
        req_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_recyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                 ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(req_recyclerView);

        adapter.setOnItemClickListener(new Admin_Request_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Request_Model request_model = documentSnapshot.toObject(Admin_Request_Adapter.class);
                Request_Model request_model = documentSnapshot.toObject(Request_Model.class);


             /*   Number countavail =(Long) documentSnapshot.get("countavail");
                String docu_id = (String) documentSnapshot.get("docu_id");
                String nameitem = (String) documentSnapshot.get("nameitem");
                Number reqcount = (Long) documentSnapshot.get("reqcount");
                String uid = (String) documentSnapshot.get("uid");
                String Request_docu_id = documentSnapshot.getId();
                //Number RealAvailCount = (Long)notebookRef.document(docu_id)

                Bundle args = new Bundle();
                args.putLong("countavail", (Long) countavail);
                args.putString("docu_id",docu_id);
                args.putString("nameitem",nameitem);
                args.putLong("reqcount", (Long) reqcount);
                args.putString("uid", uid);
                args.putString("Request_docu_id",Request_docu_id);


                DialogFragment newFragment = new Admin_AcceptRequest_Dialog();
                newFragment.setArguments(args);
                newFragment.show(getFragmentManager(), "TAG");*/

                String docu_id = (String) documentSnapshot.getId();
                String name = user.getDisplayName();
                String uid = user.getUid();
                DocumentReference reqref = db.collection("Requests").document(docu_id);
                reqref.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String nbdid = documentSnapshot.getString("docu_id");
                                clickRef.update("nbdocID", nbdid);
                            }
                        });
                clickRef.update("reqdocID", docu_id);
                clickRef.update("uid", uid);
                clickRef.update("name", name);


               // Toast.makeText(getActivity(), "clicked " + nameitem,Toast.LENGTH_SHORT).show();

                Admin_AcceptRequest_Dialog admin_acceptRequest_dialog = new Admin_AcceptRequest_Dialog();
                admin_acceptRequest_dialog.show(getFragmentManager(),"accept request dialog");
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
