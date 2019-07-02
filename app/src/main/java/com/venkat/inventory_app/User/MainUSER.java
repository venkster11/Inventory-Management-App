package com.venkat.inventory_app.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

    String value;
    private User_ItemAdapter adapter;

    private TextView item_name_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Query query = notebookRef.orderBy("count", Query.Direction.DESCENDING);

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
                String id=documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
               // DocumentReference itemname=db.collection("Notebook").document(path);
                value = documentSnapshot.getString("item_name");
               /* Intent sendStuff = new Intent(MainUSER.this, User_Request_Dialog.class);
                sendStuff.putExtra("key", value);
                startActivity(sendStuff);*/



                Toast.makeText(MainUSER.this,
                        "Position: " + position + " ID: " + value, Toast.LENGTH_SHORT).show();



                //setContentView(R.layout.dialog_user);
                openRequest_Dialog();
              /*  setContentView(R.layout.dialog_user);
                item_name_dialog=findViewById(R.id.rv_item_name);
                item_name_dialog.setText(value);*/
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
