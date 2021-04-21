package edu.cnm.deepdive.northstarsharingclient;

import android.app.Application;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor.Chain;

public class NorthStarSharingApplication extends Application {

  private String bearerToken;

  @Override
  public void onCreate() {
    super.onCreate();
    setupSignin();
    setupPicasso();
  }

  private void setupSignin() {
    GoogleSignInService.setContext(this);
    GoogleSignInService.getInstance().getBearerToken().observeForever((bearerToken) ->
        this.bearerToken = bearerToken);
  }
  private void setupPicasso() {
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor((Chain chain) ->
            chain.proceed(
                chain.request().newBuilder()
                     .addHeader("Authorization", bearerToken)
                     .build()
            )
        )
        .build();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(client))
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    );
  }
}
