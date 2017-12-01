package com.producthunt.getconnected;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.ViewGroup.LayoutParams;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 111;
  private FusedLocationProviderClient mFusedLocationClient;
  private LatLng lastKnownLocation;
  private GoogleMap mMap;
  private enum Categories {
    food, toilet, shelter, shower, health, laundry, clothes, career;
  }


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.screen2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final String s = getIntent().getStringExtra("EXTRA_SESSION_ID");

        final String location = "94010";

        switch (Categories.valueOf(s.toLowerCase())) {
          case food:
            new Searcher().execute(new SearchQuery("Food pantry", location));
            break;
          case toilet:
            new Searcher().execute(new SearchQuery("shelter", location));
            break;
          case shelter:
            new Searcher().execute(new SearchQuery("shelter", location));
            break;
          case shower:
            new Searcher().execute(new SearchQuery("shelter", location));
            break;
          case health:
            new Searcher().execute(new SearchQuery("health care", location));
            break;
          case laundry:
            new Searcher().execute(new SearchQuery("shelter", location));
            break;
          case clothes:
            new Searcher().execute(new SearchQuery("clothing", location));
            break;
          case career:
            new Searcher().execute(new SearchQuery("jobs", location));
            break;
        }
    }

  private void updateMap(final ArrayList<Info> infos) {
    ArrayList<Info> arrayOfUsers = new ArrayList<Info>();
    // Create the adapter to convert the array to views
    infoAdapter adapter = new infoAdapter(this, arrayOfUsers);
    // Attach the adapter to a ListView
    ListView listView = (ListView) findViewById(R.id.listView);
    listView.setAdapter(adapter);

//      LayoutParams list = (LayoutParams) listView.getLayoutParams();
//      list.height = 300;
//      listView.setLayoutParams(list);

      this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(infos.get(0).latlng, 12));
    for (int i = 0; i < infos.size(); i++) {
      addMarker(infos.get(i));
      adapter.add(infos.get(i));
    }
  }

  private void addMarker(final Info info) {
    MarkerOptions markerOptions = new MarkerOptions();
    markerOptions.position(info.latlng);
    markerOptions.title(info.orgName);
    markerOptions.snippet(info.orgUrl);
    this.mMap.addMarker(markerOptions);
  }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
          return;
        }

      mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
          if (location != null) {
            lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, 12));
          }
        }
      });

      mMap.setMyLocationEnabled(true);
    }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
  }

  private class Searcher extends AsyncTask<SearchQuery, Void, ArrayList<Info>> {

    public Searcher() {
      super();
    }

    @Override
    protected ArrayList<Info> doInBackground(SearchQuery... params) {
      JSONArray searchResults = Search.GetResultsAsJSON(params[0].category, params[0].zipcode);
      ArrayList<Info> infos = new ArrayList<Info>(searchResults.length());
      try {
        for (int i = 0; i < searchResults.length(); i++) {
          infos.add(new Info(searchResults.getJSONObject(i)));
        }
      } catch (JSONException ex) {
        Log.e("[ERROR] doInBackground", ex.toString());
      }

      return infos;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Info> infos) {
      updateMap(infos);
    }
  }

}
