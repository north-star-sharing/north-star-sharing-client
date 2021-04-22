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

/**
 * Utility service to acquire the the GPS coordinates asynchronously.
 */
public class LocationService implements LocationListener {

  private final Context context;
  private LocationManager locationManager;
  private Emitter<Location> emitter;

  /**
   * Create an instance of the LocationService. A Context is required for pop-up warnings and
   * to initially acquire the SystemSerivce.
   * @param context The context in which the LocationService is being used.
   */
  public LocationService(Context context) {
    this.context = context;
  }

  /**
   * Turn on the location service updates. This service must be turned off with the stopUpdates()
   * method to prevent unnecessary battery consumption and avoid any memory leaks.
   */
  public void startUpdates() {
    try {
      locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }
    catch(SecurityException e) {
      e.printStackTrace();
    }
  }

  /**
   * Turns off the location service to prevent unnecessary battery consumption and avoid any memory
   * leaks
   */
  public void stopUpdates() {
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    locationManager.removeUpdates(this);
    if (emitter != null) {
      emitter.onComplete();
    }
  }

  /**
   * Get the GPS coordinate {@link Location} asynchronously. As lifecycle data running on a separate
   * thread, this information will need to be observed.
   * @return {@link Observable&lt;Location&gt;}
   */
  public Observable<Location> getLocation() {
    return Observable
        .create((ObservableEmitter<Location> emitter) -> this.emitter = emitter)
        .subscribeOn(Schedulers.io());
  }

  @Override
  public void onLocationChanged(@NonNull Location location) {
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
