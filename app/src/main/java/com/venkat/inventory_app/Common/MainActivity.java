package com.venkat.inventory_app.Common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.venkat.inventory_app.Not_needed.MainADM;
import com.venkat.inventory_app.Admin.MainAdmin_BottomNav;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User.MainUser_BottomNav;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    String userid;

    final FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    ProgressDialog dialog;

    final DocumentReference admref = db.collection("Admins").document("admin_id");
    //final CollectionReference admref = db.collection("Admins");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialog = new ProgressDialog(this);
        dialog.setTitle("Please Wait");
        dialog.setMessage("We're logging you in");

        auth=FirebaseAuth.getInstance();

        // If logged in already, login to Inventory
        // Else, set listener to button, clicking it would launch the Sign-In activity
        if(auth.getCurrentUser()!=null){
            loginToInventory();
        } else {
            findViewById(R.id.btnlogout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build())).build(),
                            RC_SIGN_IN
                    );
                    dialog.show();
                }
            });
        }

    }

    private void loginToInventory() {
        dialog.show();
        db.collection("Admins").whereEqualTo(FieldPath.documentId(), FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        // If the Snapshot Object is null, we've received some valid data
                        if (queryDocumentSnapshots != null) {
                            // If the size is 1, then the document exists, which proves that
                            // User is admin
                            if (queryDocumentSnapshots.size() == 1) {
                                Intent intent=new Intent(MainActivity.this, MainAdmin_BottomNav.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else if (queryDocumentSnapshots.size() == 0) {
                                // That condition was just to be sure, hehe
                                Intent intent1=new Intent(MainActivity.this, MainUser_BottomNav.class);
                                startActivity(intent1);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                            }
                            dialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnlogout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent( MainActivity.this,MainActivity.class));
                            Log.d("AUTH","USER LOGGED OUT");
                            finish();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Log.d("AUTH",auth.getCurrentUser().getEmail());
                // Log in successful, we'll proceed to Inventory Log in
                loginToInventory();
            }
            else {
                Log.d("AUTH","NOT AUTHENTICATED");
            }
        }
    }
}
