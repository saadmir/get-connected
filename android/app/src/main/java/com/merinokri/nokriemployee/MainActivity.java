package com.merinokri.nokriemployee;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private Button online;
    public boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuestAccountManager.start(getApplicationContext());
        setContentView(R.layout.activity_main);
        online = (Button) findViewById(R.id.btn1);
        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
            }
        });
    }

    private void updateMap(final LatLng latlng) {
        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 11));
    }

    private void addMarker(final String title, final String snippet, final LatLng latlng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        this.mMapboxMap.addMarker(markerOptions);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    public void online(View view) throws IOException {
        if (status == true) {
            online.setText("Go Online");
            status = false;
            online.setBackgroundColor(Color.TRANSPARENT);

        } else if (status == false) {
            online.setText("Go Offline");
            status = true;
            online.setBackgroundColor(Color.GREEN);
            showSearchResults("Food Pantry", "94066");
        }

    }

    public void showSearchResults(final String category, final String zipcode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray searchResults = Search.GetResultsAsJSON(category,zipcode);
                try {
                    for (int i = 0; i < searchResults.length(); i++) {
                        JSONObject o = searchResults.getJSONObject(i);
                        final LatLng latlng = new LatLng(o.getDouble("latitude"), o.getDouble("longitude"));
                        Log.d("latlng", latlng.toString());
                        final String title = (String) o.get("name");
                        Log.d("title", title);
                        final String description = (String) o.get("description");
                        Log.d("description", description);
                        if (i == 1) {
                            updateMap(latlng);
                        }
                        addMarker(title, description, latlng);
                    }
                } catch (JSONException ex) {
                    Log.e("Search.GetResults", ex.toString());
                }
            }
        }).start();
    }
}