package com.venkat.inventory_app.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.Model.Itemshow;
import com.venkat.inventory_app.R;

import java.util.HashMap;
import java.util.Map;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText additem_name;
   // private EditText adduser_name;
    private EditText addcount;
    private Button addsave;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
   // FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
    //String uid = user.getUid();
    String username = user.getDisplayName();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);



        builder.setView(view)
                .setTitle("Add Component")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveitem();

                    }
                });

        additem_name=view.findViewById(R.id.item_name1);
        //adduser_name=view.findViewById(R.id.user_name1);
        addcount=view.findViewById(R.id.count1);
        //  addcount.setInputType(InputType.TYPE_CLASS_NUMBER);
        addsave=view.findViewById(R.id.btnsave);
        return builder.create();

    }

    private void saveitem(){

        String item_name=additem_name.getText().toString();
       // String user_name=adduser_name.getText().toString();
        int count=Integer.parseInt(addcount.getText().toString());
        String user_name = user.getDisplayName();


        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("Notebook");
        notebookRef.add(new Itemshow(item_name, user_name, count));

        DocumentReference adminlogs = db.collection("AdminLogs").document();
        Map<String, Object> note = new HashMap<>();
        note.put("item_name", item_name);
        note.put("countitem", count);
        note.put("username","Admin "+user_name);
        note.put("status","Item Added");

        adminlogs.set(note);


    }
}
