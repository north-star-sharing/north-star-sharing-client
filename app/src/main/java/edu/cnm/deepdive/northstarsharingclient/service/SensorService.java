package edu.cnm.deepdive.northstarsharingclient.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.schedulers.Schedulers;
import java.util.Arrays;

public class SensorService {

  private final Context context;
  private final SensorManager sensorManager;
  // private final float[] rotationMatrix = new float[16]; // For use with rotation vectors.
  private final float[] rotationMatrix = new float[9]; // For use with magnetometer and accelerometer.
  private final float[] inclinationMatrix = new float[9];
  private final float[] orientationAngles = new float[3]; //[0] = Azimuth, [1] = pitch, [2] = roll

  private SensorEventListener accelerometerListener;
  private SensorEventListener magnetometerListener;
  private SensorEventListener rotationVectorListener;
  private float[] gravityValues = new float[3];
  private float[] geoMagneticValues = new float[3];
  // private float[] rotationVectorValues = new float[4];
  private Emitter<float[]> emitter;

  public SensorService(Context context) {
    this.context = context;
    sensorManager = setupListeners(context);
  }

  private void convertToDegrees() {
    orientationAngles[0] = (float) Math.toDegrees(orientationAngles[0]); // azimuth
    orientationAngles[1] = (float) Math.toDegrees(orientationAngles[1]); // pitch
    orientationAngles[2] = (float) Math.toDegrees(orientationAngles[2]); // roll
  }

  private SensorManager setupListeners(Context context) {
    final SensorManager sensorManager;
    sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    accelerometerListener = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        gravityValues = event.values;
        Log.d("Accelerometer vector: ", Arrays.toString(gravityValues));
        updateOrientation();
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing.
      }
    };
    magnetometerListener = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        geoMagneticValues = event.values;
        Log.d("Geomagnetic vector: ", Arrays.toString(geoMagneticValues));
        updateOrientation();
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing.
      }
    };
    /*rotationVectorListener = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        rotationVectorValues = event.values;
        Log.d("Rotation vector: ", Arrays.toString(rotationVectorValues));
        updateOrientation();
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) { *//* Do nothing. *//* }
    };*/
    return sensorManager;
  }

  private void updateOrientation() {
    float[] previousOrientation = Arrays.copyOf(orientationAngles, orientationAngles.length);
    SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravityValues, geoMagneticValues);
    // SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVectorValues);
    SensorManager.getOrientation(rotationMatrix, orientationAngles);
    convertToDegrees();
    if (emitter != null && !Arrays.equals(previousOrientation, orientationAngles)) {
      emitter.onNext(orientationAngles);
    }
  }

  public Observable<float[]> getOrientation() {
    return Observable
        .create((ObservableEmitter<float[]> emitter) -> this.emitter = emitter)
        .subscribeOn(Schedulers.io());
  }

  public void startSensors() {
    Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    // Sensor sensorRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    // Turn the sensors on to capture the camera angles.
    sensorManager.registerListener(accelerometerListener, sensorAccelerometer,
        SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(magnetometerListener, sensorMagneticField,
        SensorManager.SENSOR_DELAY_NORMAL);
    // sensorManager.registerListener(rotationVectorListener, sensorRotationVector, SensorManager.SENSOR_DELAY_NORMAL);

  }

  public void stopSensors() {
    sensorManager.unregisterListener(accelerometerListener);
    sensorManager.unregisterListener(magnetometerListener);
    if (emitter != null) {
      emitter.onComplete();
    }
  }

}
