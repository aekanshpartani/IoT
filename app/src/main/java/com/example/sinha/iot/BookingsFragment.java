package com.example.sinha.iot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BookingsFragment extends Fragment {

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_bookings, container, false);
        new GetBookings(getActivity(),v,getActivity()).execute();
//        listView =  v.findViewById(R.id.listview);
//        user = db.collection("Users");
//        dataArrayList = new ArrayList<>();
//        user.whereEqualTo("Enail",firebaseAuth.getCurrentUser().getEmail())
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for(QueryDocumentSnapshot ds :queryDocumentSnapshots)
//                {
//                    userID = ds.getId().toString();
//                }
//                allbooking = user.document(userID).collection("All Booking");
//
//                allbooking.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                                got_count = 0;
//                                total_count = queryDocumentSnapshots.size();
//                                List<DocumentReference> drlist = new ArrayList<DocumentReference>();
//                                //Toast.makeText(getContext(), "Total : "+total_count, Toast.LENGTH_SHORT).show();
//                                for (QueryDocumentSnapshot ds:queryDocumentSnapshots)
//                                {
//
//                                    Date ts = ds.getDate("Booking Time");
//                                    String formattedDate = new SimpleDateFormat("dd MMM yy hh:mm a").format(ts);
//                                    DocumentReference dr = ds.getDocumentReference("Destination");
//
//                                    status.add( ds.getString("Status"));
//                                    bktime.add( formattedDate);
//                                    drlist.add(dr);
////
//                                }
//                                c = -1;
//                                for(DocumentReference df :drlist)
//                                {
//                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot storeName) {
//                                            c++;
//                                            //Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(getActivity(), ""+storeName.getId(), Toast.LENGTH_SHORT).show();
//                                            dataArrayList.add(data = new list(""+storeName.getString("StoreName"), ""+status.get(c),""+bktime.get(c),R.drawable.girl));
//                                            got_count++;
//                                            Toast.makeText(getContext(), "Got Count"+got_count, Toast.LENGTH_SHORT).show();
//                                            listAdapter = new adapter(getActivity(), dataArrayList);
//                                            listView.setAdapter(listAdapter);
//                                        }
//                                    });
//                                }
//                            }
//                        });
//            }
//        });
        return  v;
    }
}
