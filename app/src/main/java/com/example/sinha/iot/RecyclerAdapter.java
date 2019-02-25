package com.example.sinha.iot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements AsyncResponse {

    private String titles[],images[],details[],storeId[],doc_id;
    private GeoPoint location[];
    Context context;
    JsonTask js;


    double sourceLatitude,sourceLongitude ;
    CollectionReference subcr;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userr = db.collection("Users");

    private CollectionReference cr[];// = new CollectionReference[getItemCount()];
    //static int ikj =0;


    public void Test(String []titles,String[] images,GeoPoint []location,double sourceLatitude,double sourceLongitude,String [] details,String [] storeId,CollectionReference subcr,Context context)
    {
        this.titles = titles;
       // this.details = details;
        this.images = images;

        this.location = location;
        this.sourceLatitude = sourceLatitude;
        this.sourceLongitude = sourceLongitude;
        this.details = details;
        this.storeId = storeId;
        this.subcr = subcr;
        this.context = context;
        cr = new CollectionReference[getItemCount()];
        for(int j = 0 ; j < getItemCount() ; j++)
        {
            cr[j] = subcr.document(storeId[j]).collection("Slots");
        }
        //return  getItemCount();

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;

        public TextView itemDetail;
        public  TextView slotNo;
        public ImageButton map;
        public int pos;
        public Button book;
        public int slot_size;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);
            slotNo = itemView.findViewById(R.id.slot);
            map = itemView.findViewById(R.id.direction);
            book = itemView.findViewById(R.id.book);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + storeId[position],
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //subcr = cr.document(locationId.get(i)).collection("Sub_Locations");



                }
            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layoutg, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override

    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.pos = i;
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);

        DownloadImage d = new DownloadImage(viewHolder.itemImage);
        //d.execute(images[i]);
//        while (d.getStatus() == AsyncTask.Status.RUNNING)
//        {
//            Log.v("dd","");
//        }
            //Toast.makeText(context, "Running", Toast.LENGTH_SHORT).show();
        viewHolder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double destinationLatitude = location[viewHolder.pos].getLatitude();
                double destinationLongitude = location[viewHolder.pos].getLongitude();


//                Snackbar.make(v, "Click detected on item " + viewHolder.pos,
//                        Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", sourceLatitude, sourceLongitude, "Home Sweet Home", destinationLatitude, destinationLongitude, "Where the party is at");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(mapIntent);
                }

            }
        });

        viewHolder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "Click detected on item " + viewHolder.pos,
//                        Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                final double destinationLatitude = location[viewHolder.pos].getLatitude();
                final double destinationLongitude = location[viewHolder.pos].getLongitude();
                userr.whereEqualTo("Enail",(FirebaseAuth.getInstance()).getCurrentUser().getEmail()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot ds :queryDocumentSnapshots)
                                {
                                    if(ds.get("Booked") != null)
                                    {
                                        Toast.makeText(context, "It seems you already have a Booking", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, "Nhi hai", Toast.LENGTH_SHORT).show();
                                       js = new JsonTask(context,cr[viewHolder.pos].getParent(),RecyclerAdapter.this);
                                       js.execute("https://maps.googleapis.com/maps/api/directions/json?origin="+sourceLatitude+","+sourceLongitude+"&destination="+destinationLatitude+","+destinationLongitude+"&key=AIzaSyDd4Uzc7zmSMBVz4IjZrRT9J4noeZ1NWp4");
                                        //while(js.getStatus() == AsyncTask.Status.RUNNING)
                                        //Toast.makeText(context, ""+js.getStatus(), Toast.LENGTH_SHORT).show();

                                        //Toast.makeText(context, "DONE"+js.getStatus(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
               //
            }
        });
       //


        //Log.i("Reference",+"");
        cr[i].whereEqualTo("Status",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                 viewHolder.slot_size = queryDocumentSnapshots.getDocuments().size();



                CollectionReference ref = cr[viewHolder.pos].getParent().collection("Bookings");
                ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        int x = viewHolder.slot_size-queryDocumentSnapshots.getDocuments().size();

                        Log.d("Size",x+"    "+viewHolder.itemTitle.getText().toString());
                        if( x == 0 )
                        {
                            viewHolder.book.setEnabled(false);
                        }
                        else
                        {
                            viewHolder.book.setEnabled(true);
                        }
                        viewHolder.slotNo.setText(viewHolder.slot_size-queryDocumentSnapshots.getDocuments().size()+"");

                    }
                });

//                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                    }
//                });
//
//                viewHolder.slotNo.setText(queryDocumentSnapshots.getDocuments().size()+"");

            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    @Override
    public void processFinish(String output,DocumentReference crr) {
        final  DocumentReference subcr = crr;
        //Here you will receive the result fired from async class

        JSONObject jsonObj = null;
        String time;
        try {
            jsonObj = new JSONObject(output);
            JSONArray routes = jsonObj.getJSONArray("routes");

            JSONObject c = routes.getJSONObject(0);
            JSONArray legs = c.getJSONArray("legs");
            JSONObject d = legs.getJSONObject(0);
            JSONObject duration = d.getJSONObject("duration");
            time = duration.getString("text");


        if( time.indexOf("day") > 0 || time.indexOf("hours") > 0  || Integer.parseInt(time.substring(0,time.indexOf(' '))) > 10)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LinearLayout ls = new LinearLayout(alert.getContext());
                ls.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                ls.setOrientation(LinearLayout.VERTICAL);
                ImageView im = new ImageView(alert.getContext());
                im.setImageResource(R.drawable.slow);
                im.setAdjustViewBounds(true);
                im.setMaxHeight(1000);
                im.setMaxWidth(1000);
                im.setScaleType(ImageView.ScaleType.FIT_XY);

                ls.addView(im);

                TextView tt = new TextView(context);
                tt.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

                tt.setText("You Won't Be Able to Reach in 10 Mins");
                tt.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,30,0,0);
                tt.setLayoutParams(params);
                //tt.setTextColor(Color.CYAN);
                ls.addView(tt);
                alert.setView(ls);
                alert.create().show();
//
//                Toast  toast = new Toast(context);
//                LinearLayout ls = new LinearLayout(context);
//                ls.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
//                ls.setOrientation(LinearLayout.VERTICAL);
//                ImageView view = new ImageView(context);
//                view.setAdjustViewBounds(true);
//                view.setImageResource(R.drawable.slow);
//                ls.addView(view);
//
//                TextView tt = new TextView(context);
//                tt.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
//
//                tt.setText("You Won't Be Able to Reach in 10 Mins");
//                tt.setGravity(Gravity.CENTER);
//                //tt.setTextColor(Color.CYAN);
//                ls.addView(tt);
//                toast.setView(ls);
//               toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.show();
            }
            else if(Integer.parseInt(time.substring(0,time.indexOf(' '))) <= 10) {
            Map<String, Object> user = new HashMap<>();

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            Log.d("UserID",firebaseAuth.getCurrentUser().getEmail());
            user.put("Name", firebaseAuth.getCurrentUser().getEmail());
            subcr.collection("Bookings").add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context, "transaction added", Toast.LENGTH_SHORT).show();
                        }
                    });

            final CollectionReference cr = db.collection("Users");


            cr.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    // Toast.makeText(context, "@@@@@@@"+doc_id, Toast.LENGTH_SHORT).show();
                    for(QueryDocumentSnapshot ds : queryDocumentSnapshots)
                    {
                        doc_id = ds.getId().toString();
                        //Toast.makeText(context, "heloa"+doc_id, Toast.LENGTH_SHORT).show();
                    }

                    //Toast.makeText(context, "heloa1"+doc_id, Toast.LENGTH_SHORT).show();
                    cr.document(doc_id).update("Booked",subcr);
//                    Map<String, Object> user = new HashMap<>();
//                    user.put("Status", "Not Completed");
//                    user.put("Booking Time", new Timestamp(new Date().getTime()));
//                    user.put("Destination",subcr );
//                    user.put("Booked By APP",true);
//                    Log.v("Time", new Timestamp(new Date().getTime()).toString());
//
//                    cr.document(doc_id).collection("All Booking")
//                            .add(user)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context,MyService.class);

                    Toast.makeText(context, cr.document(doc_id).getPath(), Toast.LENGTH_SHORT).show();
                                    i.putExtra("ref",cr.document(doc_id).getPath() );
                                    context.startService(i);


//                                }
//                            });

                }

            });


        }


        // Getting JSON Array node



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}