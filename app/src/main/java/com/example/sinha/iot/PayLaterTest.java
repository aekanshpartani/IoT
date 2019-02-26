package com.example.sinha.iot;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PayLaterTest extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference cr ;
    DocumentReference dr;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_later_test);
        NotificationManager m = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);;
        //m.notify(999,notification);
        m.cancel(999);
        dr = firebaseFirestore.collection("Locations").document(
                "jwSeHF5lka1ne2pklGWY").collection("Sub_Locations").
                document("atrtLTLvTInzsnE9TB6r");

        b = findViewById(R.id.submit);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayLaterTest.this, "Verified & Paid", Toast.LENGTH_LONG).show();
                dr.update("GateBit",true);
                Intent switchToMain = new Intent(PayLaterTest.this, MainActivity.class);
                switchToMain.putExtra("FLAG", "YES");
                startActivity(switchToMain);
            }
        });
    }
}
