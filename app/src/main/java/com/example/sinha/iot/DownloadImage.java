package com.example.sinha.iot;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;

public class DownloadImage extends AsyncTask<String,Void, Bitmap> {

    ImageView imageView;

    DownloadImage(ImageView img)
    {
        imageView = img;

    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {

        imageView.setImageBitmap(bitmap);
    }
}