package com.venkat.inventory_app.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.venkat.inventory_app.R;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class User_Return_Dialog extends AppCompatDialogFragment {
    private EditText ReturnCount;
    private TextView nameofitem;


    private Number mycountavail1;
   // private int countavail;
    public String docuid;
    //private int return_count;
    private static final String TAG = "MyActivity";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.item_return_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        final FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference clickRef = db.document("Onclickrv/click");
        //int return_count;
        // public Number realcount1;
        //  public int realcount;
        //private CollectionReference notebookRef = db.collection(uid);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final String username = user.getDisplayName();
        nameofitem=view.findViewById(R.id.return_item_name);
        ReturnCount = view.findViewById(R.id.return_count);

     /*   Bundle mArgs = getArguments();
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
            itmname.setText(nameitem);*/


         //  final DocumentReference realcountdb = db.collection("Notebook").document(docuid);
         //  final DocumentReference userdb = db.collection(uid).document(userdoc_id);
        clickRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final String userdid = documentSnapshot.getString("UsrdocID");
                        final DocumentReference usrref = db.collection("Users").document("Items").collection(uid).document(userdid);
                        usrref.get()
                              .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                  @Override
                                  public void onSuccess(DocumentSnapshot documentSnapshot) {

                                      final String itmname = documentSnapshot.getString("item_name");
                                      nameofitem.setText(itmname);
                                  }
                              });

                    }
                });


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
                            // int return_count = Integer.parseInt(ReturnCount.getText().toString());
                            clickRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            final String userdid = documentSnapshot.getString("UsrdocID");
                                            //final String itmdid = documentSnapshot.getString("docID");
                                            final String nbid = documentSnapshot.getString("nbdocID");
                                            final DocumentReference nbref = db.collection("Notebook").document(nbid);
                                            Log.i(TAG, "reached clickref" );
                                            nbref.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            Log.i(TAG, "reached nbref" );
                                                            final String itmname = documentSnapshot.getString("item_name");
                                                            //nameofitem.setText(itmname);
                                                          //  Toast.makeText(getActivity(), "Count  "+itmname, Toast.LENGTH_SHORT).show();
                                                            Number countavail1 = (Long) documentSnapshot.get("count");
                                                            final int countavail = countavail1.intValue();


                                                            final DocumentReference usrref = db.collection("Users").document("Items").collection(uid).document(userdid);

                                                            usrref.get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            Number usrcount1 = (Long) documentSnapshot.get("mycount");
                                                                            final int usrcount = usrcount1.intValue();

                                                                            try {
                                                                                int return_count = Integer.parseInt(ReturnCount.getText().toString());

                                                                                int user_actual = usrcount - return_count;
                                                                                if (return_count <= usrcount) {
                                                                                    int actual = countavail + return_count;
                                                                                    nbref.update("count", actual);
                                                                                    if (user_actual == 0) {
                                                                                        usrref.delete();
                                                                                    } else {
                                                                                        usrref.update("mycount", user_actual);
                                                                                    }
                                                                                    DocumentReference adminlogs = db.collection("AdminLogs1").document();
                                                                                    Map<String, Object> note2 = new HashMap<>();
                                                                                    note2.put("item_name", itmname);
                                                                                    note2.put("countitem", return_count);
                                                                                    note2.put("username", "User " + username);
                                                                                    note2.put("status", "Item Returned");

                                                                                    note2.put("uid", uid);
                                                                                    note2.put("timestamp", FieldValue.serverTimestamp());
                                                                                    adminlogs.set(note2);
                                                                                } /*else {
                                                                                    Toast.makeText(getActivity(), "Count exceeds ", Toast.LENGTH_SHORT).show();
                                                                                }*/

                                                                            }catch (NumberFormatException ex){}
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

                        /*    int return_count = Integer.parseInt(ReturnCount.getText().toString());
                            Toast.makeText(getActivity(), "real count  " + realcount, Toast.LENGTH_SHORT).show();
                            int user_actual = mycountavail-return_count;
                            if(return_count<=mycountavail) {
                                int actual = realcount + return_count;

                                realcountdb.update("count", actual);

                                // realcount=realcount+return_count;


                                if (user_actual == 0) {
                                    userdb.delete();
                                } else {
                                    userdb.update("mycount", user_actual);
                                }

                                DocumentReference userlogs = db.collection(uid+" Logs").document();
                                Map<String, Object> note1 = new HashMap<>();
                                note1.put("item_name", nameitem);
                                note1.put("countitem", return_count);
                                note1.put("username",username);
                                note1.put("status","Item Returned");
                                userlogs.set(note1);

                                DocumentReference adminlogs = db.collection("AdminLogs").document();
                                Map<String, Object> note2 = new HashMap<>();
                                note2.put("item_name", nameitem);
                                note2.put("countitem", return_count);
                                note2.put("username","User "+username);
                                note2.put("status","Item Returned");
                                adminlogs.set(note2);
                            }*/
                        }
                    });
            //ReturnCount = view.findViewById(R.id.return_count);

       // }
        return builder.create();
    }

}
