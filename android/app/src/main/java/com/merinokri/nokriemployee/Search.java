package com.merinokri.nokriemployee;
import android.util.Log;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

class Search {
  private static final String GET_CONNECTED_URL = "https://api.smc-connect.org/search?";
  private static final int TIMEOUT = 60000;

  public static JSONArray GetResultsAsJSON(final String category, final String zipcode) {
    final String str = getResults(category, zipcode);
    try {
      final JSONArray arr = (JSONArray) new JSONTokener(str).nextValue();
//      for (int i = 0; i < arr.length(); i++) {
//        JSONObject o = arr.getJSONObject(i);
//        Log.d("coordinates", o.getJSONArray("coordinates").toString());
//      }
      return arr;
    } catch (JSONException ex) {
      Log.e("Search.GetResults", ex.toString());
    }

    return new JSONArray();
  }

  public static String GetResults(final String category, final String zipcode) {
    return getResults(category, zipcode);
  }

  private static String getResults(final String category, final String zipcode) {
    HttpURLConnection c = null;
    String url = GET_CONNECTED_URL + category + "&" + "location=" + zipcode;
    try {
      URL u = new URL(url);
      c = (HttpURLConnection) u.openConnection();
      c.setRequestMethod("GET");
//            c.setRequestProperty("Content-length", "0");
      c.setUseCaches(false);
      c.setAllowUserInteraction(false);
      c.setInstanceFollowRedirects(true);
      c.setConnectTimeout(TIMEOUT);
      c.setReadTimeout(TIMEOUT);
      c.connect();
      int status = c.getResponseCode();
      switch (status) {
        case 200:
        case 201:
          BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
          }
          br.close();
          Log.d("Json", sb.toString());
          return sb.toString();
      }
    } catch (MalformedURLException ex) {
      Log.e("Search.GetResults", ex.toString());
    } catch (IOException ex) {
      Log.e("Search.GetResults", ex.toString());
    } finally {
      if (c != null) {
        try {
          c.disconnect();
        } catch (Exception ex) {
          Log.e("Search.GetResults", ex.toString());
        }
      }
    }
    return null;
  }
}
