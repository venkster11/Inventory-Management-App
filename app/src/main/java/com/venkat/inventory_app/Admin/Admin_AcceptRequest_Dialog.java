package com.venkat.inventory_app.Admin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
import com.venkat.inventory_app.Model.Request_Model;
import com.venkat.inventory_app.R;

import java.util.HashMap;
import java.util.Map;

public class Admin_AcceptRequest_Dialog extends AppCompatDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView itemsname;
    private TextView avlc;
    private TextView reqc;
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

        itemsname=view.findViewById(R.id.acptreqname);
        avlc=view.findViewById(R.id.avilcount);
        reqc=view.findViewById(R.id.reqcount);

        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference clickRef = db.document("Onclickrv/click");




            builder.setView(view)
                    .setTitle("Requests")
                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           // Toast.makeText(getActivity(), "currid  "+Request_docuID, Toast.LENGTH_SHORT).show();
                            final FirebaseFirestore db=FirebaseFirestore.getInstance();
                            final DocumentReference clickRef = db.document("Onclickrv/click");
                            clickRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            final String docu_id1 = documentSnapshot.getString("reqdocID");
                                            final String username=documentSnapshot.getString("name");
                                            final String uid=documentSnapshot.getString("uid");
                                            //final String nbid=documentSnapshot.getString("nbdocID");
                                           // final String name=documentSnapshot.getString("name");
                                            final DocumentReference rqref = db.collection("Requests").document(docu_id1);
                                            rqref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            final String uid1=documentSnapshot.getString("uid");
                                                            Number count = (Long) documentSnapshot.get("reqcount");
                                                            String nameitem = (String) documentSnapshot.get("nameitem");

                                                            DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                            Map<String, Object> note1 = new HashMap<>();
                                                            note1.put("item_name", nameitem);
                                                            note1.put("countitem", count);
                                                            note1.put("username","Admin "+username);
                                                            note1.put("status","Request Rejected");

                                                            note1.put("uid",uid);
                                                            note1.put("timestamp", FieldValue.serverTimestamp());
                                                            adminlogs.set(note1);

                                                            DocumentReference userlogs = db.collection("Users").document("Items").collection("Logs "+uid1).document();
                                                            Map<String, Object> note11 = new HashMap<>();
                                                            note11.put("item_name", nameitem);
                                                            note11.put("countitem", count);
                                                            note11.put("username",username);
                                                            note11.put("status","Rejected");

                                                            note11.put("uid",uid1);
                                                            note11.put("timestamp", FieldValue.serverTimestamp());
                                                            userlogs.set(note11);

                                                            if (getView() != null)
                                                                Snackbar.make(getView(), String.format("Request rejected for '%d' number of Item '%s' ", count, nameitem), BaseTransientBottomBar.LENGTH_LONG).show();

                                                            rqref.delete();
                                                        }
                                                    });
                                        }
                                    });

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
                                            final String docu_id1 = documentSnapshot.getString("reqdocID");
                                            final String username=documentSnapshot.getString("name");
                                            final String uid=documentSnapshot.getString("uid");
                                            final String nbid=documentSnapshot.getString("nbdocID");
                                          //  Number countreq1 = (Long) documentSnapshot.get("reqcount");
                                          //  final int reqcount = countreq1.intValue();
                                            final DocumentReference rqref = db.collection("Requests").document(docu_id1);
                                            rqref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                           // final String docu_id_nb = documentSnapshot.getString("nbdocID");
                                                            final String uid1=documentSnapshot.getString("uid");
                                                           // final DocumentReference borrowed=db.collection(uid1).document();
                                                            final DocumentReference borrowed = db.collection("Users").document("Items").collection(uid1).document();
                                                            final Map<String, Object> note = new HashMap<>();
                                                            note.put("docuID",nbid);


                                                            Number countreq1 = (Long) documentSnapshot.get("reqcount");
                                                            final int reqcount = countreq1.intValue();
                                                            final DocumentReference nbref1 = db.collection("Notebook").document(nbid);
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

                                                                                note.put("item_name", nameitem);
                                                                                note.put("mycount", reqcount);


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

                                                                                note1.put("uid",uid1);
                                                                                note1.put("timestamp", FieldValue.serverTimestamp());
                                                                                adminlogs.set(note1);

                                                                                DocumentReference userlogs = db.collection("Users").document("Items").collection("Logs "+uid1).document();
                                                                                Map<String, Object> note11 = new HashMap<>();
                                                                                note11.put("item_name", nameitem);
                                                                                note11.put("countitem", reqcount);
                                                                                note11.put("username",username);
                                                                                note11.put("status","Accepted");

                                                                                note11.put("uid",uid1);
                                                                                note11.put("timestamp", FieldValue.serverTimestamp());
                                                                                userlogs.set(note11);

                                                                                if (getView() != null)
                                                                                    Snackbar.make(getView(), String.format("Request accepted for'%d' number of Item '%s'", reqcount, nameitem), BaseTransientBottomBar.LENGTH_LONG).show();

                                                                                rqref.delete();
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

                        }
                    });
        //}
        clickRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final String docu_id1 = documentSnapshot.getString("reqdocID");
                        final String username=documentSnapshot.getString("name");
                        final String uid=documentSnapshot.getString("uid");
                        final String nbid=documentSnapshot.getString("nbdocID");
                      /*  final DocumentReference nbre2 = db.collection("Notebook").document(nbid);
                        nbre2.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        final String nameitem = documentSnapshot.getString("item_name");
                                        Number countavail1 = (Long) documentSnapshot.get("count");
                                        int countavail=countavail1.intValue();
                                        //itemsname.setText(nameitem);
                                       // avlc.setText(String.valueOf(countavail));
                                    }
                                });*/

                        //  Number countreq1 = (Long) documentSnapshot.get("reqcount");
                        //  final int reqcount = countreq1.intValue();
                        final DocumentReference rqref = db.collection("Requests").document(docu_id1);
                        rqref.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        final String uid1=documentSnapshot.getString("uid");
                                        final DocumentReference borrowed = db.collection("Users").document("Items").collection(uid1).document();
                                        Number countreq1 = (Long) documentSnapshot.get("reqcount");
                                        final int reqcount = countreq1.intValue();
                                        final String nameitem = (String) documentSnapshot.get("nameitem");
                                        final DocumentReference nbref1 = db.collection("Notebook").document(nbid);
                                        nbref1.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        //final String nameitem = documentSnapshot.getString("item_name");
                                                        Number countavail1 = (Long) documentSnapshot.get("count");
                                                        int countavail=countavail1.intValue();
                                                        itemsname.setText(nameitem);
                                                        //  avlc.setText(String.valueOf(countavail));
                                                        reqc.setText(String.valueOf(reqcount));
                                                        clickRef.get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        Number countreal1 = (Long) documentSnapshot.get("realcount");
                                                                        int countreal = countreal1.intValue();
                                                                        avlc.setText(String.valueOf(countreal));
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });

                    }
                });
        return builder.create();
    }
}
