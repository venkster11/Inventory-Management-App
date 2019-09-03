package com.venkat.inventory_app.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private User_Borrowed_Adapter adapter;
    private String TAG="tags";
  /*  public Number realcount1=0;
    public int realcount=0;
    private String title;*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_borrowed_fragment, container, false);


        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference notebookRef1=db.collection("Notebook");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        final DocumentReference clickRef = db.document("Onclickrv/click");
        // String uid = user.getUid();
        //private FirebaseFirestore db = FirebaseFirestore.getInstance();
        //CollectionReference notebookRef = db.collection(uid);
        CollectionReference notebookRef = db.collection("Users").document("Items").collection(uid);
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
              /*  String user_docu_id = documentSnapshot.getId();
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
                newFragment.show(getFragmentManager(), "TAG");*/

                String docu_id = documentSnapshot.getId();
                String name = user.getDisplayName();
                String uid = user.getUid();
                clickRef.update("UsrdocID", docu_id);
                clickRef.update("uid", uid);
                clickRef.update("name", name);

                final DocumentReference userref = db.collection("Users").document("Items").collection(uid).document(docu_id);
                userref.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Number usrcount1 = (Long) documentSnapshot.get("mycount");
                                final int usrcount = usrcount1.intValue();
                                clickRef.update("usercount",usrcount);
                                String nbdoc =  documentSnapshot.getString("docuID");
                                clickRef.update("docID",nbdoc);


                                Log.i(TAG,nbdoc);
                                //final DocumentReference admref = db.document(nbdoc);
                               /* admref.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                Number admcount1 = (Long) documentSnapshot.get("count");
                                                final int admcount = admcount1.intValue();
                                                clickRef.update("admcount",admcount);
                                                //final String itmname = documentSnapshot.getString("item_name");
                                               // clickRef.update("itemname",itmname);
                                            }
                                        });*/
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });



               /* Toast.makeText(getActivity(),
                        "do " + name + " ab docid " , Toast.LENGTH_SHORT).show();*/

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
