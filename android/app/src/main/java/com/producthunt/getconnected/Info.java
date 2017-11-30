package com.producthunt.getconnected;

import android.media.Image;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Info {
  public String orgName;
  public String orgAddress;
  public String orgNumber;
  public int orgLogo;
  public String orgUrl;
  public LatLng latlng;

    public Info() {
    }

    public Info(String name, String address, String number, int logo) {
        this.orgName = name;
        this.orgAddress = address;
        this.orgNumber = number;
        this.orgLogo = logo;
    }

    public Info(JSONObject json) {
      try {
        this.latlng = new LatLng(json.getDouble("latitude"), json.getDouble("longitude"));
        this.orgName = (String) json.get("name");

        final String street = (String) json.getJSONObject("address").get("address_1");
        final String city = (String) json.getJSONObject("address").get("city");
        final String zipcode = (String) json.getJSONObject("address").get("postal_code");
        this.orgAddress = street + ", " + city + ", " + zipcode;

        this.orgNumber = "";
        final JSONArray phones = (JSONArray) json.getJSONArray("phones");
        if (phones.length() > 0) {
          this.orgNumber = (String) ((JSONObject) phones.get(0)).get("number");
        }
        this.orgUrl = (String) json.get("url");
      } catch (JSONException ex) {

      }
    }

}
