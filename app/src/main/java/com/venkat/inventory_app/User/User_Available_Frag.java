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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Model.Itemshow;
import com.venkat.inventory_app.Adapters.User_ItemAdapter;
import com.venkat.inventory_app.R;

public class User_Available_Frag extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
   // String uid = user.getUid();
   /* String id;
    String name;
    String value;
    String docu_id;
    Number item_avail;*/

    User_ItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.user_available_fragment, container, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DocumentReference clickRef = db.document("Onclickrv/click");
        Query query = notebookRef.orderBy("item_name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Itemshow> options = new FirestoreRecyclerOptions.Builder<Itemshow>()
                .setQuery(query, Itemshow.class)
                .build();

//        FirestoreRecyclerOptions<Itemshow> optionsFull = new FirestoreRecyclerOptions.Builder<Itemshow>()
//                .setQuery(query, Itemshow.class)
//                .build();

        adapter = new User_ItemAdapter(options) {
            @Override
            public void onDataChanged() {
                // If the latest data has size 0
                // Means there are no items, show the UI State accordingly
                if (adapter.getItemCount() == 0) {
                    ((MainUser_BottomNav) getActivity()).setUiState(R.drawable.ic_info, "There are no available items as of now");
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

        RecyclerView recyclerViewu = rootView.findViewById(R.id.Recyviewu);
        recyclerViewu.setHasFixedSize(true);
        recyclerViewu.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewu.setAdapter(adapter);

        // Firestore Adapter method to listen to incoming data
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        adapter.setOnItemClickListener(new User_ItemAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Itemshow itemshow = documentSnapshot.toObject(Itemshow.class);

             /*   value = documentSnapshot.getString("item_name");//name of the item
                docu_id = documentSnapshot.getId();//docu id
                item_avail = (Long) documentSnapshot.get("count");// count of item
                name = user.getDisplayName();// username
                uid = user.getUid();//userid

                Bundle args = new Bundle();
                args.putString("key", value);
                args.putString("docu_id",docu_id);
                args.putLong("item_avail", (Long) item_avail);
                args.putString("name",name);
                args.putString("uid",uid);*/

                String docu_id = (String) documentSnapshot.getId();
                String name = user.getDisplayName();
                String uid = user.getUid();
                clickRef.update("docID", docu_id);
                clickRef.update("uid", uid);
                clickRef.update("name", name);
                //Toast.makeText(getActivity(), "clicked " + docu_id,Toast.LENGTH_SHORT).show();

               // DialogFragment newFragment = new User_Request_Dialog();
               // newFragment.setArguments(args);
               // newFragment.show(getFragmentManager(), "TAG");


                User_Request_Dialog user_request_dialog = new User_Request_Dialog();
                user_request_dialog.show(getFragmentManager(), "request dialog");

            }
        });

        return rootView;
    }

   /* public String getMyData() {
        return value;
    }
    public String Docu_id(){
        return docu_id;
    }
    public Number countavail(){
        return item_avail;
    }
    public String user_name(){
        return name;
    }
    public String userid(){
        return uid;
    }*/

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
