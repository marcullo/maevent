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

import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.main.AgendaFragment;
import com.devmarcul.maevent.main.LiveEventFragment;
import com.devmarcul.maevent.main.CreateEventFragment;
import com.devmarcul.maevent.main.common.EventDetailsHandler;
import com.devmarcul.maevent.utils.bottom_navig.BottomNavigationBehavior;
import com.devmarcul.maevent.utils.bottom_navig.ViewScroller;
import com.devmarcul.maevent.business_logic.MaeventAccountManager;
import com.devmarcul.maevent.utils.Prompt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    public static String KEY_CONFIG_PROFILE_REQUESTED = "config-profile-requested";
    public static String KEY_CONFIG_PROFILE_CONTENT = "config-profile-content";

    private static String LOG_TAG = "MainActivity";
    public static final int PLACE_PICKER_REQUEST = 1;

    public static Maevent pendingEvent = null;
    //Fragment should call focus() before using this handler
    public static EventDetailsHandler eventDetailsHandler = new EventDetailsHandler();

    private int lastLoadedFragmentId;
    private Fragment lastLoadedFragment;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastLoadedFragmentId = 0;

        bottomNavigation = findViewById(R.id.main_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        if (pendingEvent != null) {
            bottomNavigation.setSelectedItemId(R.id.main_live_event);
        }
        else {
            bottomNavigation.setSelectedItemId(R.id.main_agenda);
        }
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

        if (id == R.id.main_action_profile) {
            setProfileActivity();
            return true;
        }
        if (id == R.id.main_action_support) {
            Prompt.displayTodo(this);
            return true;
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        lastLoadedFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void loadLiveEventFragment() {
        if (pendingEvent == null) {
            return;
        }
        loadFragment(new LiveEventFragment(), R.id.main_live_event);
    }

    private void loadFragment(Fragment fragment, int newFragmentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        lastLoadedFragmentId = newFragmentId;
        lastLoadedFragment = fragment;
        bottomNavigation.setSelectedItemId(newFragmentId);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int newFragmentId = item.getItemId();
            if (newFragmentId == lastLoadedFragmentId) {
                NestedScrollView scrollView = null;
                try {
                    scrollView = ((ViewScroller) lastLoadedFragment).getScrollView();
                } catch (Exception e) {
                }
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
                    if (pendingEvent == null) {
                        displayNoPendingEventPrompt();
                        return false;
                    }

                    fragment = new LiveEventFragment();
                    break;

                case R.id.main_create_event:
                    fragment = new CreateEventFragment();
                    break;

                default:
                    throw new IllegalArgumentException("Not implemented fragment!");
            }

            loadFragment(fragment, newFragmentId);
            return true;
        }
    };

    private void displayNoPendingEventPrompt() {
        String message = getString(R.string.text_no_pending_event);
        Prompt.displayShort(message, this);
    }

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

    private void setProfileActivity() {
        Log.d(LOG_TAG, "Setting configure profile activity.");
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
