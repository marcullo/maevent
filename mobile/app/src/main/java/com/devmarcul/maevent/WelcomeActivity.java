package com.devmarcul.maevent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView mWelcomeImageView;
    private TextView mWelcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("WELCOME", "Hello to Maevent!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mWelcomeImageView = findViewById(R.id.image_welcome);
        mWelcomeTextView = findViewById(R.id.message_welcome);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimation();
    }

    public void startAnimation() {
        Context context = getApplicationContext();
        final int animId = R.anim.anim_welcome;
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("WELCOME", "Welcome animation finished!");
                setNextActivity();
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mWelcomeImageView.startAnimation(animation);
        mWelcomeTextView.startAnimation(animation);
    }

    public void setNextActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
