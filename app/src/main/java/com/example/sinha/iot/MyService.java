package com.example.sinha.iot;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {
    CountDownTimer ct;
    DocumentReference dr;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    DocumentReference subcr;
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        dr = db.document(intent.getStringExtra("ref"));
        Toast.makeText(this, ""+intent.getStringExtra("ref"), Toast.LENGTH_SHORT).show();

        ct = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                Toast.makeText(MyService.this, "Running"+millisUntilFinished, Toast.LENGTH_SHORT).show();
            }
            DocumentReference mydr = null;
            public void onFinish() {
                //mTextField.setText("done!");
                Toast.makeText(MyService.this, "Timer Expired", Toast.LENGTH_SHORT).show();

                Log.v("Datata",dr.getPath());
                dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            mydr = documentSnapshot.getDocumentReference("Booked");
                            subcr = documentSnapshot.getDocumentReference("Booked");
                        }
                        catch (Exception e)
                        {

                        }
                        if( mydr != null ) {
                            mydr.collection("Bookings").whereEqualTo("Name", FirebaseAuth.getInstance()
                                    .getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    DocumentReference ff = null;
                                    for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                                        ff = ds.getReference();
                                    }
                                    if (ff != null)
                                        ff.delete();

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("Booked", FieldValue.delete());
                                    Toast.makeText(MyService.this, "" + dr.getId(), Toast.LENGTH_SHORT).show();
                                    dr.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MyService.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });

                            CollectionReference cr = dr.collection("All Booking");
                            Map<String, Object> user = new HashMap<>();
                            user.put("Status", "Cancelled");
                            user.put("Booking Time", new Timestamp(new Date().getTime()));
                            user.put("Destination",subcr );
                            user.put("Booked By APP",true);

                            dr.collection("All Booking").add(user);
                        }
                    }
                });



//                cr.orderBy("Booking Time", Query.Direction.DESCENDING).get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                for(QueryDocumentSnapshot fs : queryDocumentSnapshots)
//                                {
//                                    mdr = fs.getReference();
//                                    break;
//                                }
//
//                                mdr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        String str = documentSnapshot.getString("Status");
//                                        if(str.equals("Completed"))
//                                        {
//                                            //Do Nothing
//                                        }
//                                        else
//                                        {
//                                            Map<String, Object> user = new HashMap<>();
//                                            user.put("Status", "Cancelled");
//                                            mdr.update(user);
//                                        }
//                                    }
//                                });
//                            }
//                        });
            }

        }.start();

        stopSelf();

    }
}
