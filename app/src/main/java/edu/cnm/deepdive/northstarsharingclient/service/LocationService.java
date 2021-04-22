package edu.cnm.deepdive.northstarsharingclient.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;

public class LocationService implements LocationListener {

  private final Context context;
  private LocationManager locationManager;
  private double latitude;
  private double longitude;
  private Emitter<Location> emitter;

  public LocationService(Context context) {
    this.context = context;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void startUpdates() {
    try {
      locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }
    catch(SecurityException e) {
      e.printStackTrace();
    }
  }

  public void stopUpdates() {
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    locationManager.removeUpdates(this);
    if (emitter != null) {
      emitter.onComplete();
    }
  }

  public Observable<Location> getLocation() {
    return Observable
        .create((ObservableEmitter<Location> emitter) -> this.emitter = emitter)
        .subscribeOn(Schedulers.io());
  }

  @Override
  public void onLocationChanged(@NonNull Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
    Log.d(getClass().getName(), location.toString());
    if (emitter != null) {
      emitter.onNext(location);
      Log.d(getClass().getName(), "Location emitted.");
    }
  }

  @Override
  public void onProviderDisabled(@NonNull String provider) {
    Toast.makeText(context, "Please Location Services and Internet", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onProviderEnabled(String provider) {

  }

}
