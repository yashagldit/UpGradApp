package com.upgrad.internshipapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferenceUtils sharedPreferenceUtils=new SharedPreferenceUtils(this,"auth");
        String tok=sharedPreferenceUtils.getStringValue("token","");
        if(tok.equals("")){
            startActivity(new Intent(MainActivity.this ,Login.class));
        }
        else{
            startActivity(new Intent(MainActivity.this ,UserInterest.class));
        }
    }
}
