package com.offlineapp.offlineshortrangecommunicationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class home_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toast.makeText(this, "Home Screen loaded", Toast.LENGTH_SHORT).show();

        timer();

    }

    private void timer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(home_screen.this, MainActivity.class);
                startActivity(intent);

            }
        },5000);


        //finish activity

        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<8;i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        }).start();

    }

}