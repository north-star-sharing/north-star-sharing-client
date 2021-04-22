package edu.cnm.deepdive.northstarsharingclient.service;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class SensorService {

  private static float[] gravityValues;
  private static float[] GeoMagneticValues;
  private static float[] rotationMatrix = new float[9];
  private static float[] orientationAngles = new float[3]; //[0] = Azimuth, [1] = pitch, [2] = roll

  public static float[] getOrientation(SensorManager sensorManager) {
    Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    Sensor sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    SensorEventListener sensorEventListenerAccelerometer = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        gravityValues = event.values;
        SensorManager.getRotationMatrix(rotationMatrix, null, gravityValues, GeoMagneticValues);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) { /* Do nothing. */ }
    };
    SensorEventListener sensorEventListenerMagneticField = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        GeoMagneticValues = event.values;
        SensorManager.getRotationMatrix(rotationMatrix, null, gravityValues, GeoMagneticValues);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) { /* Do nothing. */ }
    };
    // Turn the sensors on to capture the camera angles.
    sensorManager.registerListener(sensorEventListenerAccelerometer, sensorAccelerometer,
        SensorManager.SENSOR_DELAY_NORMAL);
    sensorManager.registerListener(sensorEventListenerMagneticField, sensorMagneticField,
        SensorManager.SENSOR_DELAY_NORMAL);
    // Convert the camera angles to degrees.
    orientationAngles[0] = (float) (-orientationAngles[0] * 180 / Math.PI); // azimuth
    orientationAngles[1] = (float) (-orientationAngles[1] * 180 / Math.PI); // pitch
    orientationAngles[2] = (float) (-orientationAngles[2] * 180 / Math.PI); // roll
    // Turn the sensors off.
    sensorManager.unregisterListener(sensorEventListenerAccelerometer);
    sensorManager.unregisterListener(sensorEventListenerMagneticField);
    return orientationAngles;
  }


}
