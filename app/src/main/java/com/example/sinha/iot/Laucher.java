package com.example.sinha.iot;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mrgames13.jimdo.splashscreen.App.SplashScreenBuilder;

public class Laucher extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreenBuilder.getInstance(this)
                .setVideo(R.raw.car_splash_animation)
                .setImage(R.drawable.main_logo)
                .setTitle("SPark")
                .setSubtitle("Park with ease")
                .show();
        setContentView(R.layout.main_scren);
        fragmentManager = getSupportFragmentManager();

    //        {
    //            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //            final FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
    //
    //            if( firebaseUser != null && firebaseUser.isEmailVerified())
    //            {
    //                Intent i = new Intent(Laucher.this,MainActivity.class);
    //                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //                startActivity(i);
    //            }
    //
    //        }



        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.Login_Fragment).commit();
        }


//        NotificationChannel channel = new NotificationChannel("Notify", "MyNotify", NotificationManager.IMPORTANCE_HIGH);
//        //channel.enableVibration(true);
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        manager.createNotificationChannel(channel);
//
//        FirebaseMessaging.getInstance().subscribeToTopic("download0211.gmail.com")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Success";
//                        if (!task.isSuccessful()) {
//                            msg = "failed";
//                        }
//                        Log.d("tag", msg);
//                        Toast.makeText(getApplicationContext(), "Subscibed", Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    // Replace Login Fragment with animation
    protected void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task

        if (SignUp_Fragment != null)
            replaceLoginFragment();
        else
            super.onBackPressed();
    }
}
