package com.venkat.inventory_app.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.venkat.inventory_app.R;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Admin_CountUpdate_Dialog extends AppCompatDialogFragment {
    private EditText update_count;
    private @ServerTimestamp
    Date timestamp;
    TextView Nameofitem;
    TextView Countofitem;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_update_count, null);

        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference clickRef = db.document("Onclickrv/click");

        Nameofitem=view.findViewById(R.id.rv_item_name);
        Countofitem=view.findViewById(R.id.rv_count_avail);
      //  final String did1 = "";
        clickRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String didd = documentSnapshot.getString("docID");
                        final DocumentReference nbref = db.collection("Notebook").document(didd);
                        nbref.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Number count1 = (Long) documentSnapshot.get("count");
                                        String nameitem = (String) documentSnapshot.get("item_name");
                                        int count = count1.intValue();
                                        Nameofitem.setText(nameitem);
                                        Countofitem.setText(String.valueOf(count));
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        update_count=view.findViewById(R.id.updcount);
        //final DocumentReference nbref = db.collection("Notebook").document(String.valueOf(did1));
        builder.setView(view)
                .setTitle("Update or Delete Component")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        clickRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String didd = documentSnapshot.getString("docID");
                                        final String name=documentSnapshot.getString("name");
                                        final String uid=documentSnapshot.getString("uid");
                                        final DocumentReference nbref = db.collection("Notebook").document(didd);


                                        nbref.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Number count = (Long) documentSnapshot.get("count");
                                                        String nameitem = (String) documentSnapshot.get("item_name");

                                                        DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                        Map<String, Object> note1 = new HashMap<>();
                                                        note1.put("item_name", nameitem);
                                                        note1.put("countitem", count);
                                                        note1.put("username","Admin "+name);
                                                        note1.put("status","Item Deleted");

                                                        note1.put("uid",uid);
                                                        note1.put("timestamp", FieldValue.serverTimestamp());
                                                        adminlogs.set(note1);

                                                        if (getView() != null)
                                                            Snackbar.make(getView(), String.format("Deleted '%d' number of component '%s' returned successfully", count, nameitem), BaseTransientBottomBar.LENGTH_LONG).show();
                                                        nbref.update("count",0);
                                                        //nbref.delete();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {


                                                    }
                                                });
                                       // nbref.delete();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        //Toast.makeText(getActivity(), "clicked " + did1,Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String upd = update_count.getText().toString();
                        if(upd!="") {
                            clickRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String didd = documentSnapshot.getString("docID");
                                            final String name=documentSnapshot.getString("name");
                                            final String uid=documentSnapshot.getString("uid");
                                            final DocumentReference nbref = db.collection("Notebook").document(didd);


                                            try {
                                                int updtcount = Integer.parseInt(update_count.getText().toString());
                                                //int test = 11;

                                                nbref.update("count", updtcount);
                                            }catch (NumberFormatException ex){

                                            }

                                            nbref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            Number count = (Long) documentSnapshot.get("count");
                                                            String nameitem = (String) documentSnapshot.get("item_name");


                                                            DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                            Map<String, Object> note1 = new HashMap<>();
                                                            note1.put("item_name", nameitem);
                                                            note1.put("countitem", count);
                                                            note1.put("username","Admin "+name);
                                                            note1.put("status","Count Updated");

                                                            note1.put("uid",uid);
                                                            note1.put("timestamp", FieldValue.serverTimestamp());
                                                            adminlogs.set(note1);

                                                            if (getView() != null)
                                                                Snackbar.make(getView(), String.format("Updated count of '%s' to '%d' successfully", nameitem, count), BaseTransientBottomBar.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });



                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }

                    }
                });
        return builder.create();

    }
}
