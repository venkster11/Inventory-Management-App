package com.venkat.inventory_app.Common;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.venkat.inventory_app.Admin.MainADM;
import com.venkat.inventory_app.R;
import com.venkat.inventory_app.User.MainUSER;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 123;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  FirebaseApp.initializeApp(this);



        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            //user already signed in
            Log.d("AUTH",auth.getCurrentUser().getEmail());

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
            userid=currentFirebaseUser.getEmail();

            if( userid.equals("venkateshm11799@gmail.com")){
                Toast.makeText(this, "ADMIN - " + currentFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this, MainADM.class);
                startActivity(intent);
            }
            else
            {
                Intent intent1=new Intent(MainActivity.this, MainUSER.class);
                startActivity(intent1);
                Toast.makeText(this, "USER - " + userid, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build())).build(),
                    RC_SIGN_IN
            );


        }

        findViewById(R.id.btnlogout).setOnClickListener(this);






        Button btnadmin=(Button)findViewById(R.id.admin);
        Button btnuser=(Button)findViewById(R.id.user);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MainADM.class);
                startActivity(intent);

            }
        });

        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this, MainUSER.class);
                startActivity(intent1);
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
              /*  FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                userid=currentFirebaseUser.getEmail();
                Toast.makeText(this, "User Id" + currentFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();

                if( userid.equals("venkateshm11799@gmail.com")){
                    Toast.makeText(this, "User Id" + currentFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this, MainADM.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent1=new Intent(MainActivity.this, MainUSER.class);
                    startActivity(intent1);
                    Toast.makeText(this, "User Id......." + userid, Toast.LENGTH_SHORT).show();
                }*/

            }
            else {
                Log.d("AUTH","NOT AUTHENTICATED");
            }
        }
    }
}
