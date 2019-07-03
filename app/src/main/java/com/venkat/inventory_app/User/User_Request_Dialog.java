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
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.R;

public class User_Request_Dialog extends AppCompatDialogFragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText request_count;
    private TextView item_name_dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        MainUSER activity = (MainUSER) getActivity();//getting i
        final String docu_id=activity.Docu_id();
        final String nameitem = activity.getMyData();
        final Number countavail1=activity.countavail();
        final int countavail=countavail1.intValue();
        final String username=activity.user_name();
        final String uid=activity.userid();


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user, null);

        item_name_dialog=view.findViewById(R.id.rv_item_name);
        item_name_dialog.setText(nameitem);

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

                        int reqcount=Integer.parseInt(request_count.getText().toString());
                        if(reqcount <= countavail)
                        {
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

                           // Toast.makeText(getActivity(), "item available", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Limit Exceded", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getActivity(), "count   "+countavail, Toast.LENGTH_SHORT).show();

                    }
                });

        request_count=view.findViewById(R.id.rv_count);

        return builder.create();
    }

}
