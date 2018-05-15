package com.devmarcul.maevent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.devmarcul.maevent.fragment.AgendaFragment;

public class MainActivity extends AppCompatActivity {
    private ActionBar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = getSupportActionBar();
        mToolbar.hide();

        BottomNavigationView navigation = findViewById(R.id.main_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new AgendaFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.main_agenda:
                    fragment = new AgendaFragment();
                    break;

                case R.id.main_live_event:
                    fragment = new AgendaFragment();
                    break;

                case R.id.main_create_event:
                    fragment = new AgendaFragment();
                    break;

                default:
                    throw new IllegalArgumentException("Not implemented fragment!");
            }

            loadFragment(fragment);
            return true;
        }
    };
}
