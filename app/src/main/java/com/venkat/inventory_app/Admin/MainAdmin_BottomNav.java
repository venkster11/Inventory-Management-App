package com.venkat.inventory_app.Admin;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.venkat.inventory_app.Common.MainActivity;
import com.venkat.inventory_app.R;

public class MainAdmin_BottomNav extends AppCompatActivity {

    private ImageView uiStateIcon;
    private TextView uiStateMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin__bottom_nav);

        uiStateIcon = findViewById(R.id.ui_state_image);
        uiStateMessage = findViewById(R.id.ui_state_message);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Admin_available_frag()).commit();
    }

    public void setUiState(@DrawableRes int drawableRes, @NonNull String message) {
        uiStateMessage.setVisibility(View.VISIBLE);
        uiStateIcon.setVisibility(View.VISIBLE);
        uiStateIcon.setImageResource(drawableRes);
        uiStateMessage.setText(message);
    }

    public void hideUiState() {
        uiStateMessage.setVisibility(View.GONE);
        uiStateIcon.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_menu_item:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent( MainAdmin_BottomNav.this, MainActivity.class));
                                Log.d("AUTH","USER LOGGED OUT");
                                finish();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_aviailable:
                            selectedFragment = new Admin_available_frag();
                            break;

                        case R.id.nav_Requests:
                            selectedFragment = new Admin_Request_Frag();
                            break;

                        case R.id.nav_Logs:
                            selectedFragment = new Admin_logs_frag();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return true;
                }
            };

}
