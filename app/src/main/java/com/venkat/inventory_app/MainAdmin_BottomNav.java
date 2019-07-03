package com.venkat.inventory_app;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.venkat.inventory_app.Admin.Admin_Request_Frag;

public class MainAdmin_BottomNav extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin__bottom_nav);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Admin_available_frag()).commit();
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
