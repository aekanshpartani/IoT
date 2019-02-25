package com.example.sinha.iot;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PayActivity extends Activity {
//FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//CollectionReference cr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Toast.makeText(this, "Pay Now", Toast.LENGTH_SHORT).show();
        //firebaseAuth.getCurrentUser().getEmail();
//        cr = firebaseFirestore.collection("Users");
//        Toast.makeText(this, ""+firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
//        cr.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail()).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot ds :queryDocumentSnapshots)
//                {
//                    if(ds.getDocumentReference("Booked") != null)
//                    {
//                        Toast.makeText(PayActivity.this, "Pay", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(PayActivity.this, "Do Not Pay", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        //Toast.makeText(this, "nhi hua", Toast.LENGTH_SHORT).show();

    }
}
