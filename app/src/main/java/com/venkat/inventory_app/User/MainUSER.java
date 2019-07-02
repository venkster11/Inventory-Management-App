package com.venkat.inventory_app.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;

public class MainUSER extends AppCompatActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef=db.collection("Notebook");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String id;
    String name;
    String value;
    String docu_id;
    Number item_avail;

    private User_ItemAdapter adapter;

    private TextView item_name_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = notebookRef.orderBy("item_name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Itemshow> options = new FirestoreRecyclerOptions.Builder<Itemshow>()
                .setQuery(query, Itemshow.class)
                .build();

        adapter= new User_ItemAdapter(options);

        RecyclerView recyclerViewu = findViewById(R.id.Recyviewu);
        recyclerViewu.setHasFixedSize(true);
        recyclerViewu.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewu.setAdapter(adapter);

        adapter.setOnItemClickListener(new User_ItemAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Itemshow itemshow=documentSnapshot.toObject(Itemshow.class);
                // String path = documentSnapshot.getReference().getPath();
                // DocumentReference itemname=db.collection("Notebook").document(path);
                // item_avail=documentSnapshot.getDouble("count");


                value = documentSnapshot.getString("item_name");//name of the item
                docu_id=documentSnapshot.getId();//docu id
                item_avail = (Long) documentSnapshot.get("count");// count of item
                name = user.getDisplayName();// username
                uid = user.getUid();//userid
                Toast.makeText(MainUSER.this,
                        "UID: " + item_avail + "   docu id: " + docu_id, Toast.LENGTH_SHORT).show();

                openRequest_Dialog();

            }
        });
    }

    private void openRequest_Dialog(){
        User_Request_Dialog user_request_dialog=new User_Request_Dialog();
        user_request_dialog.show(getSupportFragmentManager(),"requset dialog");

    }
    public String getMyData() {
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
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
