package com.venkat.inventory_app.Admin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.R;

import java.util.HashMap;
import java.util.Map;

public class Admin_AcceptRequest_Dialog extends AppCompatDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String username = user.getDisplayName();

    String accepted = "Accepted";
    TextView requested;
    int flag = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.admin_accept_request_dialog,null);

        //Bundle mArgs = getArguments();
       /* if(mArgs!=null) {
            final Number countavail1 = mArgs.getLong("countavail");
            final int countavail = countavail1.intValue();

            final String docu_id = mArgs.getString("docu_id");
            final String nameitem = mArgs.getString("nameitem");

            final Number reqcount1 = mArgs.getLong("reqcount");
            final int reqcount = reqcount1.intValue();

            final String uid = mArgs.getString("uid");
            final String Request_docuID = mArgs.getString("Request_docu_id");


            final int remaining_item = countavail-reqcount;*/


            builder.setView(view)
                    .setTitle("Requests")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // Toast.makeText(getActivity(), "currid  "+Request_docuID, Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final FirebaseFirestore db=FirebaseFirestore.getInstance();
                            final DocumentReference clickRef = db.document("Onclickrv/click");
                            clickRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            final String docu_id = documentSnapshot.getString("docID");
                                            final String username=documentSnapshot.getString("name");
                                            final String uid=documentSnapshot.getString("uid");
                                          //  Number countreq1 = (Long) documentSnapshot.get("reqcount");
                                          //  final int reqcount = countreq1.intValue();
                                            final DocumentReference nbref = db.collection("Requests").document(docu_id);
                                            nbref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            final String docu_id_nb = documentSnapshot.getString("docu_id");
                                                            final String uid1=documentSnapshot.getString("uid");
                                                            Number countreq1 = (Long) documentSnapshot.get("reqcount");
                                                            final int reqcount = countreq1.intValue();
                                                            final DocumentReference nbref1 = db.collection("Notebook").document(docu_id_nb);
                                                            nbref1.get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            final String nameitem = documentSnapshot.getString("item_name");
                                                                            Number countavail1 = (Long) documentSnapshot.get("count");
                                                                            int countavail=countavail1.intValue();
                                                                            final int remaining_item = countavail-reqcount;
                                                                            if (reqcount<=countavail){
                                                                                nbref1.update("count",remaining_item);
                                                                                DocumentReference borrowed=db.collection(uid1).document();

                                                                                Map<String, Object> note = new HashMap<>();
                                                                                note.put("item_name", nameitem);
                                                                                note.put("mycount", reqcount);
                                                                                note.put("docuID",docu_id);

                                                                                borrowed.set(note)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                // Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                //Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                                                                //Log.d(TAG, e.toString());
                                                                                                //Log.d(TAG, "onFailure: ");
                                                                                            }
                                                                                        });

                                                                                DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                                                Map<String, Object> note1 = new HashMap<>();
                                                                                note1.put("item_name", nameitem);
                                                                                note1.put("countitem", reqcount);
                                                                                note1.put("username","Admin "+username);
                                                                                note1.put("status","Request Accepted");

                                                                                note1.put("uid",uid);
                                                                                note1.put("timestamp", FieldValue.serverTimestamp());
                                                                                adminlogs.set(note1);
                                                                                nbref.delete();
                                                                            }
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
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                         /*   DocumentReference noteRef=db.collection("Notebook").document(docu_id);
                            noteRef.update("count",remaining_item);

                            DocumentReference borrowed=db.collection(uid).document();

                            Map<String, Object> note = new HashMap<>();
                            note.put("item_name", nameitem);
                            note.put("mycount", reqcount);
                            note.put("docuID",docu_id);
                            //View rootView = inflater.inflate(R.layout.request_recycler, null);
                            //requested=(TextView) view.findViewById(R.id.requested_text);
                            //requested.setText("Accepted");


                            borrowed.set(note)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                           // Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                            //Log.d(TAG, e.toString());
                                            //Log.d(TAG, "onFailure: ");
                                        }
                                    });*/

                          /*  DocumentReference adminlogs = db.collection("AdminLogs").document();
                            Map<String, Object> note1 = new HashMap<>();
                            note1.put("item_name", nameitem);
                            note1.put("countitem", reqcount);
                            note1.put("username","Admin "+username);
                            note1.put("status","Request Accepted");
                            adminlogs.set(note1);

                            DocumentReference userlogs = db.collection(uid+" Logs").document();
                            Map<String, Object> note11 = new HashMap<>();
                            note11.put("item_name", nameitem);
                            note11.put("countitem", reqcount);
                            note11.put("username",username);
                            note11.put("status","Request Accepted");
                            userlogs.set(note11);*/

                            //Toast.makeText(getActivity(), "data" + nameitem, Toast.LENGTH_SHORT).show();
                           // DocumentReference requestdelete=db.collection("Requests").document(Request_docuID);
                           // requestdelete.delete();
                        }
                    });
        //}
        return builder.create();
    }
}
