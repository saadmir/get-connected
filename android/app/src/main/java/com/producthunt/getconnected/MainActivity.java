package com.producthunt.getconnected;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
    }

    public void getFood(View v){

        String description = "none";
        if (v.getTag().equals("food"))
        {
            description = "food";
        }

        else if (v.getTag().equals("toilet"))
        {
            description = "toilet";
        }

        else if (v.getTag().equals("shelter"))
        {
            description = "shelter";
        }

        else if (v.getTag().equals("shower"))
        {
            description = "shower";
        }

        else if (v.getTag().equals("health"))
        {
            description = "health";
        }

        else if (v.getTag().equals("laundry"))
        {
            description = "laundry";
        }

        else if (v.getTag().equals("clothes"))
        {
            description = "clothes";
        }

        else if (v.getTag().equals("career"))
        {
            description = "career";
        }

        //When user clicks on Phone Number, it should open Dialer - Done
        //Also Map should zoom in. It should not be zoomed out. - Done
        //When User clicks on ListView Item, it should zoom in on map accordingly

        Intent intent = new Intent (this, MapsActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", description);

        startActivity(intent);

    }

    public void getShower(View v){

        Intent intent = new Intent (this, MapsActivity.class);
        startActivity(intent);

    }
}
