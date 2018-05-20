package com.devmarcul.maevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.devmarcul.maevent.fragment.AgendaFragment;
import com.devmarcul.maevent.fragment.LiveEventFragment;
import com.devmarcul.maevent.helper.BottomNavigationBehavior;
import com.devmarcul.maevent.interfaces.ViewScroller;
import com.devmarcul.maevent.profile.MaeventAccountManager;
import com.devmarcul.maevent.static_data.MainActivityStaticData;
import com.devmarcul.maevent.utils.tools.Prompt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements MainActivityStaticData {

    int lastLoadedFragmentId;
    Fragment lastLoadedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastLoadedFragmentId = 0;

        BottomNavigationView bottomNavigation = findViewById(R.id.main_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        //TODO Load agenda if there is no live event
        bottomNavigation.setSelectedItemId(R.id.main_live_event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_action_configure_profile) {
            setConfigureProfileActivity();
            return true;
        }
        if (id == R.id.main_action_support) {
            Prompt.displayTodo(this);
            return true;
        }
        //TODO Move logout to the proper place
        if (id == R.id.main_action_logout) {
            MaeventAccountManager.signOut(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    setWelcomeActivity();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //TODO Add proper back button behavior
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

            int newFragmentId = item.getItemId();
            if (newFragmentId == lastLoadedFragmentId) {
                NestedScrollView scrollView = ((ViewScroller)lastLoadedFragment).getScrollView();
                if (scrollView != null) {
                    animateScroll(scrollView);
                }
                return true;
            }

            Fragment fragment;
            switch (newFragmentId) {
                case R.id.main_agenda:
                    fragment = new AgendaFragment();
                    break;

                case R.id.main_live_event:
                    fragment = new LiveEventFragment();
                    break;

                case R.id.main_create_event:
                    fragment = new AgendaFragment();
                    break;

                default:
                    throw new IllegalArgumentException("Not implemented fragment!");
            }

            loadFragment(fragment);
            lastLoadedFragmentId = newFragmentId;
            lastLoadedFragment = fragment;
            return true;
        }
    };

    private void animateScroll(NestedScrollView scrollView) {
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.fullScroll(View.FOCUS_UP);
    }

    private void setWelcomeActivity() {
        Log.d(LOG_TAG, "Setting welcome activity.");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setConfigureProfileActivity() {
        Log.d(LOG_TAG, "Setting configure profile activity.");
        Intent intent = new Intent(this, ConfigureProfileActivity.class);
        intent.putExtra(KEY_CONFIG_PROFILE_REQUESTED, true);
        startActivity(intent);
    }
}
