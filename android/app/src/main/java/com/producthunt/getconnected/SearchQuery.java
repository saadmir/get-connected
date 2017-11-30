package com.producthunt.getconnected;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by saad on 11/29/17.
 */

final class SearchQuery {
  public String category;
  public String zipcode;
  public LatLng latlng;

  public SearchQuery(String category, String zipcode) {
    this.category = category;
    this.zipcode = zipcode;
//    this.latlng = latlng;
  }
}
