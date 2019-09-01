package com.venkat.inventory_app.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.R;


public class Admin_CountUpdate_Dialog extends AppCompatDialogFragment {
    private EditText update_count;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.admin_update_count, null);

        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference clickRef = db.document("Onclickrv/click");


       /* final String did1 = "";
        clickRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String didd = documentSnapshot.getString("docID");
                        String did1=didd;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/


        update_count=view.findViewById(R.id.updcount);
        //final DocumentReference nbref = db.collection("Notebook").document(String.valueOf(did1));
        builder.setView(view)
                .setTitle("Update Component")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(getActivity(), "clicked " + did1,Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        clickRef.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String didd = documentSnapshot.getString("docID");
                                        final DocumentReference nbref = db.collection("Notebook").document(didd);
                                        int updtcount=Integer.parseInt(update_count.getText().toString());
                                        //int test = 11;
                                        nbref.update("count",updtcount);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                    }
                });
        return builder.create();

    }
}
