package com.venkat.inventory_app.User;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.venkat.inventory_app.Admin_available_frag;
import com.venkat.inventory_app.R;

public class MainUser_BottomNav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_bottom_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_bottom_nav,new User_Available_Frag()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_available:
                    selectedFragment = new User_Available_Frag();
                    break;
                case R.id.navigation_borrowed:
                    selectedFragment = new User_Borrowed_Frag();
                    break;
                case R.id.navigation_logs:
                    selectedFragment = new User_Logs_Frag();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.user_bottom_nav, selectedFragment).commit();
            return true;
        }
    };

}
