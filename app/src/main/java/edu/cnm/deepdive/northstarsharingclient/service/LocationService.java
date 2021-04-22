package edu.cnm.deepdive.northstarsharingclient.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class LocationService implements LocationListener {

  // TODO implement as per example
  //  at https://www.thecrazyprogrammer.com/2017/01/how-to-get-current-location-in-android.html
  // Combine this class with SensorService? PositionService?

  private final Context context;
  private LocationManager locationManager;

  public LocationService(Context context) {
    this.context = context;
  }

  @Override
  public void onLocationChanged(@NonNull Location location) {

  }

  @Override
  public void onProviderDisabled(@NonNull String provider) {
    Toast.makeText(context, "Please Location Services and Internet", Toast.LENGTH_SHORT).show();
  }
}
