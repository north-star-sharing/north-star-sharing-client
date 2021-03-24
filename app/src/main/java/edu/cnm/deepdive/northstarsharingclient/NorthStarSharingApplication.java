package edu.cnm.deepdive.northstarsharingclient;

import android.app.Application;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;

public class NorthStarSharingApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
  }
}
