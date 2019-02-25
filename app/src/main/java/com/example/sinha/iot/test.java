package com.example.sinha.iot;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class test extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //CollectionReference cr

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "Los Angeles");
//        city.put("state", "CA");
//        city.put("country", "USA");
//
//        db.collection("cities")
//                .add(city);



    }
}
