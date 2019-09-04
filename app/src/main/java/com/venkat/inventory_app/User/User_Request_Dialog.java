package com.venkat.inventory_app.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.venkat.inventory_app.Model.Request_Model;
import com.venkat.inventory_app.R;

import java.util.HashMap;
import java.util.Map;

public class User_Request_Dialog extends AppCompatDialogFragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText request_count;
    private TextView item_name_dialog;
    private TextView countcurravail;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user, null);



        item_name_dialog=view.findViewById(R.id.rv_item_name);
        countcurravail=view.findViewById(R.id.rv_count_avail);
       // Bundle mArgs = getArguments();
       // if(mArgs!=null) {

          /*  final String docu_id=mArgs.getString("docu_id");
            final String nameitem = mArgs.getString("key");
            final Number countavail1=mArgs.getLong("item_avail");
            final int countavail=countavail1.intValue();
            final String username=mArgs.getString("name");
            final String uid=mArgs.getString("uid");*/

        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference clickRef = db.document("Onclickrv/click");
        clickRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String did = documentSnapshot.getString("docID");
                        final DocumentReference nbref = db.collection("Notebook").document(did);
                        nbref.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String nameitem = documentSnapshot.getString("item_name");
                                        Number countavail1 = (Long) documentSnapshot.getLong("count");
                                        int countavail=countavail1.intValue();
                                        item_name_dialog.setText(nameitem);
                                        countcurravail.setText(String.valueOf(countavail));
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




            //item_name_dialog.setText(nameitem);


            builder.setView(view)
                    .setTitle("Component Request")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {

                                final int reqcount = Integer.parseInt(request_count.getText().toString());
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                final DocumentReference clickRef = db.document("Onclickrv/click");


                            clickRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            final String docu_id = documentSnapshot.getString("docID");
                                            final String username=documentSnapshot.getString("name");
                                            final String uid=documentSnapshot.getString("uid");
                                            final DocumentReference nbref = db.collection("Notebook").document(docu_id);
                                            nbref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            String nameitem = documentSnapshot.getString("item_name");
                                                            Number countavail1 = (Long) documentSnapshot.get("count");
                                                            int countavail=countavail1.intValue();

                                                            //int reqcount=Integer.parseInt(request_count.getText().toString());



                                                                if (reqcount <= countavail) {
                                                                    //try {
                                                                    Request_Model request_model = new Request_Model(docu_id, nameitem, username, uid, countavail, reqcount, null);
                                                                    //Request_Model request_model=new Request_Model(nameitem,username,countavail,reqcount);
                                                                    db.collection("Requests").document().set(request_model)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    //Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {

                                                                                    //Toast.makeText(getContext(), "Request Failed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                DocumentReference userlogs = db.collection("Users").document("Items").collection("Logs "+uid).document();
                                                                Map<String, Object> note1 = new HashMap<>();
                                                                note1.put("item_name", nameitem);
                                                                note1.put("countitem", reqcount);
                                                                note1.put("username",username);
                                                                note1.put("status","Requested");

                                                                note1.put("uid", uid);
                                                                note1.put("timestamp", FieldValue.serverTimestamp());
                                                                userlogs.set(note1);

                                                                    DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                                    Map<String, Object> note2 = new HashMap<>();
                                                                    note2.put("item_name", nameitem);
                                                                    note2.put("countitem", reqcount);
                                                                    note2.put("username", "User " + username);
                                                                    note2.put("status", "Request Received");

                                                                    note2.put("uid", uid);
                                                                    note2.put("timestamp", FieldValue.serverTimestamp());
                                                                    adminlogs.set(note2);

                                                                    // Toast.makeText(getActivity(), "item available", Toast.LENGTH_SHORT).show();
                                                                  //  }catch (NumberFormatException ex){Toast.makeText(getActivity(), "Limit Exceded", Toast.LENGTH_SHORT).show();}
                                                                }

                                                          /*  else {

                                                                    Toast.makeText(getActivity(), "Limit Exceded", Toast.LENGTH_SHORT).show();
                                                            }*/



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
                        //int reqcount=Integer.parseInt(request_count.getText().toString());

                           // Toast.makeText(getActivity(), "count   " +countavail, Toast.LENGTH_SHORT).show();

                            }catch (NumberFormatException ex){Toast.makeText(getActivity(), "Specify count", Toast.LENGTH_SHORT).show();}
                        }
                    });

     //  }
        request_count=view.findViewById(R.id.rv_count);

        return builder.create();
    }

}
