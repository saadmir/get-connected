package com.producthunt.getconnected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HaniAbidi on 11/27/17.
 */

public class infoAdapter extends ArrayAdapter<Info> {
    public infoAdapter(Context context, ArrayList<Info> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Info user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview, parent, false);
        }
        // Lookup view for data population
        TextView orgName = (TextView) convertView.findViewById(R.id.nameTextViewID);
        TextView orgAddress = (TextView) convertView.findViewById(R.id.infoTextViewID);
        TextView orgPhone = (TextView) convertView.findViewById(R.id.phoneID);
        ImageView orgImage = (ImageView) convertView.findViewById(R.id.imageView1ID);

        // Populate the data into the template view using the data object
        orgName.setText(user.orgName);
        orgAddress.setText(user.orgAddress);
        orgPhone.setText(user.orgNumber);
        orgImage.setImageResource(user.orgLogo);


        // Return the completed view to render on screen
        return convertView;
    }
}
