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
import com.venkat.inventory_app.User.MainUser_BottomNav;

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
        Query query = notebookRef.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Request_Model> options = new FirestoreRecyclerOptions.Builder<Request_Model>()
                .setQuery(query, Request_Model.class)
                .build();

        adapter= new Admin_Request_Adapter(options) {
            @Override
            public void onDataChanged() {
                // If the latest data has size 0
                // Means there are no items, show the UI State accordingly
                if (adapter.getItemCount() == 0) { // wait, I'll take a search yp
                    // TODO: Admin Request: Change Icon and Text
                    ((MainAdmin_BottomNav) getActivity()).setUiState(R.drawable.ic_notifications_black_24dp, "There are no requests as of now");
                } else {
                    // If there are non-zero items though, hide it
                    // Note that I cannot call hideUiState without the cast operation done ahead of it
                    // getActivity() returns a regular FragmentActivity
                    // So I need to cast it to MainUser_BottomNav
                    // KNOWING that it is the parent activity.
                    ((MainAdmin_BottomNav) getActivity()).hideUiState();
                }
                super.onDataChanged();
            }
        };

        RecyclerView req_recyclerView = rootView.findViewById(R.id.request_recyclerview);
        req_recyclerView.setHasFixedSize(true);
        req_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        req_recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new Admin_Request_Adapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Request_Model request_model = documentSnapshot.toObject(Admin_Request_Adapter.class);
                Request_Model request_model = documentSnapshot.toObject(Request_Model.class);




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
                                DocumentReference nbreff = db.collection("Notebook").document(nbdid);
                                nbreff.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                Number countavail1 = (Long) documentSnapshot.get("count");
                                                int countavail=countavail1.intValue();
                                                clickRef.update("realcount",countavail);
                                            }
                                        });
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
