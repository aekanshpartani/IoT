package com.example.sinha.iot;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mrgames13.jimdo.splashscreen.App.SplashScreenBuilder;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();

        if( firebaseUser != null && firebaseUser.isEmailVerified())
        {
            Intent i = new Intent(Home.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else
        {

            Intent i = new Intent(Home.this,Laucher.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //startActivity();
        }

        finish();
    }
}
