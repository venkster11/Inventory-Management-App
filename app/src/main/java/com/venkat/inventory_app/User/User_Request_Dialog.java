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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.venkat.inventory_app.Common.Itemshow;
import com.venkat.inventory_app.R;

public class User_Request_Dialog extends AppCompatDialogFragment {

   
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText request_count;
    private TextView item_name_dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //User_Available_Frag activity = new User_Available_Frag();//getting i
       /* final String docu_id=activity.Docu_id();
        final String nameitem = activity.getMyData();
       // final Number countavail1=activity.countavail();
        //final int countavail=countavail1.intValue();
        final String username=activity.user_name();
        final String uid=activity.userid();*/


        item_name_dialog=view.findViewById(R.id.rv_item_name);
        Bundle mArgs = getArguments();
        if(mArgs!=null) {

            final String docu_id=mArgs.getString("docu_id");
            final String nameitem = mArgs.getString("key");
            final Number countavail1=mArgs.getLong("item_avail");
            final int countavail=countavail1.intValue();
            final String username=mArgs.getString("name");
            final String uid=mArgs.getString("uid");

            item_name_dialog.setText(nameitem);


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
                           // Toast.makeText(getActivity(), "count   " +countavail, Toast.LENGTH_SHORT).show();

                        }
                    });

        }
        request_count=view.findViewById(R.id.rv_count);

        return builder.create();
    }

}
