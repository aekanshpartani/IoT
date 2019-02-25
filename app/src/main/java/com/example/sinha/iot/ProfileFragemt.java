package com.example.sinha.iot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ProfileFragemt extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference cr;
    TextView name,email,vehicle,since;
    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_profile_fragemt, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        name = v.findViewById(R.id.tv_name);
        email = v.findViewById(R.id.Email);
        vehicle = v.findViewById(R.id.Vehicle);
        since = v.findViewById(R.id.Since);
        firebaseFirestore = FirebaseFirestore.getInstance();
        cr = firebaseFirestore.collection("Users");

        cr.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots)
                        {
                            name.setText(ds.get("Name").toString());
                            email.setText(ds.get("Enail").toString());
                            vehicle.setText(ds.get("VehicleNo").toString());
                            Date ts = ds.getDate("Date Created");
                            String formattedDate = new SimpleDateFormat("dd MMMM yyyy ").format(ts);

                            since.setText(formattedDate);


                            /*Date date = new Date();
                            date.setTime(ts.getTime());*/
                            //
                            //since.setText(ts.toString());
                        }
                    }
                });

        //name.setText(firebaseAuth.getCurrentUser().getDisplayName());
        return  v;

    }


}
