package com.saloj.android.wes_task_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.saloj.android.wes_task_project.R;
import com.saloj.android.wes_task_project.activity.login.LoginActivity;
import com.saloj.android.wes_task_project.activity.register.RegisterActivity;
import com.saloj.android.wes_task_project.helper.AppPrefrence;

public class SplashActivity extends AppCompatActivity {
AppPrefrence appPrefrence;
    Animation anim1,anim2,anim3;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appPrefrence = new AppPrefrence(this);

        logo =findViewById(R.id.imgSpl);

        anim1 = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        logo.setVisibility(View.VISIBLE);
        logo.startAnimation(anim1);


        Handler handler = new Handler();
       handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (appPrefrence.getIsLogin().equals("true")) {

                        Intent intent =new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                }else if(appPrefrence.getIsLogin().equals("false")) {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

        }, 2500);

    }

}