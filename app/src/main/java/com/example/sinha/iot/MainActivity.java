package com.example.sinha.iot;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mrgames13.jimdo.splashscreen.App.SplashScreenBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference cr ;
    DocumentReference dr;
    FragmentTransaction fm;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fm = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    BookFragmenr bff = (BookFragmenr)getSupportFragmentManager().findFragmentByTag("BookFra") ;
                    if(bff == null) {
                        BookFragmenr bf = new BookFragmenr();
                        fm.replace(R.id.fragment, bf,"BookFra");
                        fm.commit();
                    }
                   // mTextMessage.setText(R.string.title_home);

                    return true;
                case R.id.navigation_dashboard:

                    ProfileFragemt pff = (ProfileFragemt)getSupportFragmentManager().findFragmentByTag("ProfileFra") ;
                    if(pff == null) {
                        ProfileFragemt bf = new ProfileFragemt();
                        fm.replace(R.id.fragment, bf,"ProfileFra");
                        fm.commit();
                    }
                 //   mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:


                    BookingsFragment bf = (BookingsFragment) getSupportFragmentManager().findFragmentByTag("BookingFra") ;
                    if(bf == null) {
                        BookingsFragment bfff = new BookingsFragment();
                        fm.replace(R.id.fragment, bfff,"BookingFra");
                        fm.commit();
                    }

                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreenBuilder.getInstance(this)
                .setVideo(R.raw.car_splash_animation)
                .setImage(R.drawable.main_logo)
                .setTitle("SPark")
                .setSubtitle("Park with ease")
                .show();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("SPark");

        NotificationChannel channel = new NotificationChannel("Notify", "MyNotify", NotificationManager.IMPORTANCE_HIGH);
        //channel.enableVibration(true);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance()
                .getCurrentUser().getEmail().replace('@', '.'))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                        Log.d("tag", msg);
                        Toast.makeText(getApplicationContext(), "Subscibed", Toast.LENGTH_SHORT).show();
                    }
                });

        // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signOut:
                FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseAuth.getInstance()
                        .getCurrentUser().getEmail().replace('@', '.'));

                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(MainActivity.this,Laucher.class));
                finish();
                
                return true;
            case R.id.payLater:
                cr = firebaseFirestore.collection("Users");

                final String[] userId = new String[1];
                Toast.makeText(this, ""+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                cr.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot ds :queryDocumentSnapshots)
                                {
                                    dr = ds.getReference();
                                }
                                Map<String, Object> dues = new HashMap<>();
                                dues.put("Dues",false );
                                dr.update(dues);


                            }
                        });

                return  true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fm = getSupportFragmentManager().beginTransaction();
        BookFragmenr bff1 = (BookFragmenr)getSupportFragmentManager().findFragmentByTag("BookFra");
        if(bff1 == null) {
            BookFragmenr bf = new BookFragmenr();
            fm.replace(R.id.fragment, bf,"BookFra");
            fm.commit();
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
}