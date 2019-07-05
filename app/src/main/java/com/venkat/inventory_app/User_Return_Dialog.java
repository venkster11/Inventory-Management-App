package com.venkat.inventory_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class User_Return_Dialog extends AppCompatDialogFragment {
    private EditText ReturnCount;
    private TextView itmname;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
   // public Number realcount1;
  //  public int realcount;
    //private CollectionReference notebookRef = db.collection(uid);

    private Number mycountavail1;
    private int mycountavail;
    public String docuid;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.item_return_dialog, null);

        itmname=view.findViewById(R.id.return_item_name);
        ReturnCount = view.findViewById(R.id.return_count);

        Bundle mArgs = getArguments();
        if(mArgs!=null) {

            // final String docu_id = mArgs.getString("docu_id");
            final String nameitem = mArgs.getString("key");
            final String docuid=mArgs.getString("docuid");
            final String userdoc_id=mArgs.getString("userdoc");
            final String user_uid = mArgs.getString("uid");
            mycountavail1=mArgs.getLong("mycount");
            mycountavail=mycountavail1.intValue();
            final Number realcount1=mArgs.getLong("realcount");
            final int realcount=realcount1.intValue();
            itmname.setText(nameitem);


           final DocumentReference realcountdb = db.collection("Notebook").document(docuid);
           final DocumentReference userdb = db.collection(user_uid).document(userdoc_id);
          /*  realcountdb.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, e.toString());
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        realcount1 = (Long) documentSnapshot.get("count");
                        realcount = realcount1.intValue();
                        //String title = documentSnapshot.getString(KEY_TITLE);
                       // String description = documentSnapshot.getString(KEY_DESCRIPTION);

                        //textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                    }

                }
            });*/



            builder.setView(view)
                    .setTitle("Return")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Return", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int return_count = Integer.parseInt(ReturnCount.getText().toString());
                            Toast.makeText(getActivity(), "c  " + realcount, Toast.LENGTH_SHORT).show();
                            int user_actual = mycountavail-return_count;
                            if(return_count<=mycountavail){
                                int actual = realcount+return_count;

                                realcountdb.update("count",actual);

                               // realcount=realcount+return_count;
                            }

                            if(user_actual==0)
                            {
                                userdb.delete();
                            }
                            else {
                                userdb.update("mycount", user_actual);
                            }
                        }
                    });
            //ReturnCount = view.findViewById(R.id.return_count);

        }
        return builder.create();
    }

}
