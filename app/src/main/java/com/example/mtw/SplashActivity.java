package com.example.mtw;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db=new Database(this);
        //db.onUpgrade(db.getWritableDatabase(),0,1);
        Class SelectedClass;
        if(db.getIsUserLogged())
            SelectedClass = MainActivity.class;
        else
            SelectedClass = SignupActivity.class;

        final Class passInClass = SelectedClass;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, passInClass);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
