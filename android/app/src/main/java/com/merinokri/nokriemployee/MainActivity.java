package com.merinokri.nokriemployee;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.mapping.MapQuestAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import static android.R.attr.button;
import static android.net.sip.SipErrorCode.TIME_OUT;
import static java.security.spec.RSAKeyGenParameterSpec.F0;

public class MainActivity extends Activity {
    private final LatLng SAN_FRAN = new LatLng(37.77564, -122.38674);
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private Spinner spinner;
    private static final String[] paths = {"Babysitter", "Mechanic", "Carpenter", "Plumber", "Gardener", "Tailor", "Maid", "Tutor"};
    private Button online;
    public boolean status = false;
    private String MAKE_AVAILABLE_URI = "https://us-central1-mitrabajo-us.cloudfunctions.net/makeAvailable?phone=%s&location=%s";
    private AccessibilityService mAppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapQuestAccountManager.start(getApplicationContext());
        setContentView(R.layout.activity_main);
        online = (Button) findViewById(R.id.btn1);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SAN_FRAN, 11));
                addMarker(mMapboxMap);
            }
        });
    }

    private void addMarker(MapboxMap mapboxMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SAN_FRAN);
        markerOptions.title("San Francisco");
        markerOptions.snippet("Welcome to San Fran!");
        mapboxMap.addMarker(markerOptions);
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
           // TelephonyManager mTelephonyMgr;
           // mTelephonyMgr = (TelephonyManager)
           //         getSystemService(Context.TELEPHONY_SERVICE);
          //  String mPhoneNumber = mTelephonyMgr.getLine1Number();
            clt("https://us-central1-mitrabajo-us.cloudfunctions.net/makeAvailable?phone=14082197539&location=37.77564,-122.38674");
        }

    }

    public void clt(final String url_str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    URL url = new URL(url_str);
                    URLConnection conection = url.openConnection();
                    conection.setConnectTimeout(180);
                    conection.connect();
                    int lenghtOfFile = conection.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                    }
                    input.close();
                } catch (SocketTimeoutException e) {
                    Log.e("Error 1: ", e.toString());
                } catch (Exception e) {
                    Log.e("Error 2: ", e.toString());
                }
            }
        }).start();
    }

}