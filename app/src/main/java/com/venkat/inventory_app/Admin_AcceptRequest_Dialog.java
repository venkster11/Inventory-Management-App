package com.venkat.inventory_app;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Admin_AcceptRequest_Dialog extends AppCompatDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String accepted = "Accepted";
    TextView requested;
    int flag = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_accept_request_dialog,null);

        Bundle mArgs = getArguments();
        if(mArgs!=null) {
            final Number countavail1 = mArgs.getLong("countavail");
            final int countavail = countavail1.intValue();

            final String docu_id = mArgs.getString("docu_id");
            final String nameitem = mArgs.getString("nameitem");

            final Number reqcount1 = mArgs.getLong("reqcount");
            final int reqcount = reqcount1.intValue();

            final String uid = mArgs.getString("uid");

            final int remaining_item = countavail-reqcount;


            builder.setView(view)
                    .setTitle("Requests")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DocumentReference noteRef=db.collection("Notebook").document(docu_id);
                            noteRef.update("count",remaining_item);

                            DocumentReference borrowed=db.collection(uid).document();
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

                            Toast.makeText(getActivity(), "data" + nameitem, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return builder.create();
    }
}
