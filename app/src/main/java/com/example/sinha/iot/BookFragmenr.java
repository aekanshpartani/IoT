package com.example.sinha.iot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class BookFragmenr extends Fragment {

    AutoCompleteTextView au;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cr = db.collection("Locations");
    CollectionReference subcr;
    List<String > location = new ArrayList<String>();
    List<String > locationId = new ArrayList<String>();
    List<String > storeName = new ArrayList<String>();
    List<String > storeImage = new ArrayList<String>();
    List<GeoPoint> storeLocation = new ArrayList<GeoPoint>();
    List<String> storeDetails  = new ArrayList<String>();
    List<String> storeId  = new ArrayList<String>();
 //   List<Bitmap> getImages =  new ArrayList<Bitmap>();
    ArrayAdapter<String>  adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter radapter;
    GeoPoint sLocation[];

    View v;
    String sImage[],sName[],sDetails[],storeIdd[];
    private FusedLocationProviderClient mFusedLocationClient;
    final  int LOCATION_SERVICE  = 1;
    double curLongitude,curLatitude;
    TextView book;


    @Override
    public void onStart() {
        super.onStart();

//        Toast.makeText(getActivity(), cr+"", Toast.LENGTH_SHORT).show();
        final ProgressDialog progress;
        progress =  new ProgressDialog(getContext());
        progress.setMessage("Initialising.... :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
        cr.orderBy("Location").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                location.clear();
                locationId.clear();
                for(QueryDocumentSnapshot ds: queryDocumentSnapshots )
                {
                    location.add(ds.getString("Location"));
                    locationId.add(ds.getId());
                }
                adapter = new ArrayAdapter<String >(getActivity(),android.R.layout.select_dialog_item,location);
                au.setAdapter(adapter);
                progress.dismiss();
       }
        });

        au.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                progress.setMessage("Getting all Locations.... :) ");
                progress.show();
                storeName.clear();
                storeImage.clear();
                storeLocation.clear();

                i = location.indexOf(au.getText().toString());
                subcr = cr.document(locationId.get(i)).collection("Sub_Locations");
                subcr.orderBy("StoreName").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(QueryDocumentSnapshot ds : queryDocumentSnapshots)
                        {
                           // Toast.makeText(getActivity(), ""+ds.getId(), Toast.LENGTH_SHORT).show();
                            storeName.add(ds.getString("StoreName"));
                            storeImage.add(ds.getString("StoreImage"));
                            storeLocation.add(ds.getGeoPoint("StoreLocation"));

                           // Toast.makeText(getActivity(), ""+ds.getGeoPoint("StoreLocation"), Toast.LENGTH_SHORT).show();
                            storeDetails.add(ds.getString("storeDetails"));
                            storeId.add((ds.getId()));
                        }
                        sName = storeName.toArray(new String[storeName.size()]);
                        sImage  = storeImage.toArray(new String[storeImage.size()]);
                        sLocation = storeLocation.toArray(new GeoPoint[storeLocation.size()]);
                        sDetails = storeDetails.toArray(new String[storeDetails.size()]);
                        storeIdd = storeId.toArray(new String[storeId.size()]);



                        radapter = new RecyclerAdapter();
                        ((RecyclerAdapter) radapter).Test(sName,sImage,sLocation,curLatitude,curLongitude,sDetails,storeIdd,subcr,getContext());
                        //Toast.makeText(getActivity(), "Count:"+k, Toast.LENGTH_SHORT).show();

                        recyclerView.setAdapter(radapter);
                        progress.dismiss();
                    }
                });

            }
        });

//     cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
//         @Override
//         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
////             Toast.makeText(getActivity(), ""+queryDocumentSnapshots.getDocuments().size(), Toast.LENGTH_SHORT).show();
//         }
//     });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_SERVICE);

            // Permission is not granted
        }
        else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                curLatitude = location.getLatitude();
                                curLongitude = location.getLongitude();
                            }
                        }
                    });
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_SERVICE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {

                                        curLatitude = location.getLatitude();
                                        curLongitude = location.getLongitude();
                                    }
                                }
                            });



                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_book_fragmenr, container, false);
         au = v.findViewById(R.id.editText1);
         book = v.findViewById(R.id.book);
        recyclerView =  v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        au.setThreshold(1);


        // Inflate the layout for this fragment
        return v;
    }


}
