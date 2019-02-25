package com.example.sinha.iot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Seotoolzz on 13/6/17.
 */
public class adapter extends ArrayAdapter<list> {

    Activity context;
    List<list> items;
    Integer[] imageId = {
            R.drawable.girl
    };


    public adapter(Activity mainActivity, ArrayList<list> dataArrayList) {
        super(mainActivity, 0, dataArrayList);

        this.context = mainActivity;
        this.items = dataArrayList;
    }


    private class ViewHolder {

        TextView status, name,bookingTime;
        ImageView image;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        adapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(
                    R.layout.list_xml, parent, false);

            holder = new adapter.ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.bookingTime = (TextView) convertView.findViewById(R.id.bookingTime);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);

        } else {
            holder = (adapter.ViewHolder) convertView.getTag();
        }

        list productItems = items.get(position);


        holder.name.setText(productItems.getName());
        holder.status.setText(productItems.getStatus());
        holder.bookingTime.setText(productItems.getBookingTime());
        holder.image.setImageResource(productItems.getImageId());

        return convertView;

    }


}