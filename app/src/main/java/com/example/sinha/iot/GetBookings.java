package com.example.sinha.iot;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GetBookings extends AsyncTask<String, String, String> {

    Context context;
    Activity act;

    ListView listView;
    View v;
    private ArrayList<list> dataArrayList;
    private adapter listAdapter;
    private list data;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference user,allbooking;
    String userID;
    Boolean gotdata = false;
    int total_count,got_count;

    List<String> status = new ArrayList<String>();
    List<String> bktime = new ArrayList<String>();
    int c = 0;
    ProgressDialog progress ;

    //public SharedPreferences sharedpreferences = context.getSharedPreferences("ref", Context.MODE_PRIVATE);
    GetBookings(Context context,View v,Activity act)
    {
        this.context = context;
        this.v = v;
        this.act = act;

    }



    protected void onPreExecute() {
        super.onPreExecute();
        progress =  new ProgressDialog(context);
        progress.setMessage("Fetching Details.... :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.show();

    }

    protected String doInBackground(String... params) {
        listView =  v.findViewById(R.id.listview);
        user = db.collection("Users");
        dataArrayList = new ArrayList<>();
        user.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.isEmpty() )
                {
                    Toast.makeText(context, "Zero", Toast.LENGTH_SHORT).show();
                }
                for(QueryDocumentSnapshot ds :queryDocumentSnapshots)
                {
                    userID = ds.getId().toString();
                }
                allbooking = user.document(userID).collection("All Booking");

                allbooking.orderBy("Booking Time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        got_count = 0;
                        total_count = queryDocumentSnapshots.size();
                        final List<DocumentReference> drlist = new ArrayList<DocumentReference>();
                        //Toast.makeText(getContext(), "Total : "+total_count, Toast.LENGTH_SHORT).show();
                        for (QueryDocumentSnapshot ds:queryDocumentSnapshots)
                        {

                            Date ts = ds.getDate("Booking Time");
                            String formattedDate = new SimpleDateFormat("dd MMM yy hh:mm a").format(ts);
                            DocumentReference dr = ds.getDocumentReference("Destination");

                            status.add( ds.getString("Status"));
                            bktime.add( formattedDate);
                            drlist.add(dr);
                        }
                        c = -1;
                        for(DocumentReference df :drlist)
                        {
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot storeName) {
                                    c++;
                                    //Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(context, ""+storeName.getId(), Toast.LENGTH_SHORT).show();
                                    dataArrayList.add(data = new list(""+storeName.getString("StoreName"), ""+status.get(c),""+bktime.get(c),R.drawable.girl));
                                    got_count++;
//                                    Toast.makeText(context, "Got Count"+got_count, Toast.LENGTH_SHORT).show();
                                    listAdapter = new adapter(act, dataArrayList);
                                    listView.setAdapter(listAdapter);
                                    if( c == drlist.size()-1 )
                                        progress.dismiss();
                                }
                            });
                        }
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
            }
        });


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Toast.makeText(context, "Bookings Loaded", Toast.LENGTH_SHORT).show();
        //progress.dismiss();
// if (pd.isShowing()){
// pd.dismiss();


    }
}

