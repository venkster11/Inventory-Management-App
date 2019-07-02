package com.venkat.inventory_app.Not_needed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;

public class Add_item extends AppCompatActivity {

    private EditText additem_name;
    private EditText adduser_name;
    private EditText addcount;
    private Button addsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        additem_name=findViewById(R.id.item_name1);
        adduser_name=findViewById(R.id.user_name1);
        addcount=findViewById(R.id.count1);
      //  addcount.setInputType(InputType.TYPE_CLASS_NUMBER);
        addsave=findViewById(R.id.btnsave);



        addsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveitem();
            }
        });

    }

    private void saveitem(){

        String item_name=additem_name.getText().toString();
        String user_name=adduser_name.getText().toString();
        int count=Integer.parseInt(addcount.getText().toString());

        if (item_name.trim().isEmpty() || user_name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        notebookRef.add(new Itemshow(item_name, user_name, count));
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
