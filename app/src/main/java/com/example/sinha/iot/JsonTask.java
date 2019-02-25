package com.example.sinha.iot;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JsonTask extends AsyncTask<String, String, String> {

    Context context;
    JSONObject jj;
    DocumentReference subcr;
    String doc_id ="";

    public AsyncResponse myresponse = null;
    //public SharedPreferences sharedpreferences = context.getSharedPreferences("ref", Context.MODE_PRIVATE);
    JsonTask(Context context, DocumentReference subcr,AsyncResponse asyncResponse)
    {
        this.context = context;
        this.subcr = subcr;
        this.myresponse = asyncResponse;
    }
    ProgressDialog progress ;


    protected void onPreExecute() {
        super.onPreExecute();
        progress =  new ProgressDialog(context);
       progress.setMessage("Booking.... :) ");
       progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    protected String doInBackground(String... params) {


        //
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line); //here u ll get whole response...... :-)

            }
            return buffer.toString();

            } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        progress.dismiss();

// if (pd.isShowing()){
// pd.dismiss();

        myresponse.processFinish(result,subcr);
        Log.i("Result",result);
    }
}
