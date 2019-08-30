package com.example.android.firebasepushnotifications;

import  android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView mProfileLabel;
    TextView mUsersLabel;
    TextView mNotificationLabel;

    ViewPager mMainPager;
    FirebaseAuth mAuth;

    PagerViewAdapter mPagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mProfileLabel=findViewById(R.id.profileLabel);
        mUsersLabel=findViewById(R.id.UsersLabel);
        mNotificationLabel=findViewById(R.id.notificationsLabel);

        //startActivity(new Intent(MainActivity.this,LoginActivity.class));



        RelativeLayout relativeLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        mMainPager=findViewById(R.id.mainPager);
        mMainPager.setOffscreenPageLimit(2);
        mPagerViewAdapter=new PagerViewAdapter(getSupportFragmentManager());

        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(0);
            }
        });
        mUsersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(1);
            }
        });
        mNotificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(2);
            }
        });

        mMainPager.setAdapter(mPagerViewAdapter);
        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int i) {
                changeTabs(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeTabs(int i) {

        if(i==0)
        {
            mProfileLabel.setTextColor(getColor(R.color.TextTabBright));
            mProfileLabel.setTextSize(22);

            mUsersLabel.setTextColor(getColor(R.color.TextTabLight));
            mUsersLabel.setTextSize(16);

            mNotificationLabel.setTextColor(getColor(R.color.TextTabLight));
            mNotificationLabel.setTextSize(16);

        }
        if(i==1)
        {
            mProfileLabel.setTextColor(getColor(R.color.TextTabLight));
            mProfileLabel.setTextSize(16);

            mUsersLabel.setTextColor(getColor(R.color.TextTabBright));
            mUsersLabel.setTextSize(22);

            mNotificationLabel.setTextColor(getColor(R.color.TextTabLight));
            mNotificationLabel.setTextSize(16);
        }
        if(i==2)
        {
            mProfileLabel.setTextColor(getColor(R.color.TextTabLight));
            mProfileLabel.setTextSize(16);

            mUsersLabel.setTextColor(getColor(R.color.TextTabLight));
            mUsersLabel.setTextSize(16);

            mNotificationLabel.setTextColor(getColor(R.color.TextTabBright));
            mNotificationLabel.setTextSize(22);
        }

    }
}
