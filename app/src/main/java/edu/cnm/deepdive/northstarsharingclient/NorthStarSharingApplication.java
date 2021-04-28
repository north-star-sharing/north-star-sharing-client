package edu.cnm.deepdive.northstarsharingclient;

import android.app.Application;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * North Star Sharing is a proof-of-concept group project for
 * <a href="https://deepdivecoding.com/">Deep Dive Coding</a> through
 * <a href="CNM">https://www.cnm.edu/</a>. The
 * purpose of this Android app is to create a social media-style experience around sharing
 * astronomy pictures of the night sky. For more information about the project, please visit the
 * <a href="https://north-star-sharing.github.io/>North Star Sharing site</a> on Github.
 *
 * This class is the entry point for the {@link Application} that starts the
 * {@link GoogleSignInService} and initializes {@link Picasso}
 * for handling {@link edu.cnm.deepdive.northstarsharingclient.model.Image Images}.
 */
public class NorthStarSharingApplication extends Application {

  private String bearerToken;

  @Override
  public void onCreate() {
    super.onCreate();
    setupSignin();
    setupPicasso();
    Stetho.initializeWithDefaults(this);
  }

  private void setupSignin() {
    GoogleSignInService.setContext(this);
    GoogleSignInService.getInstance()
                       .getBearerToken()
                       .observeForever((bearerToken) ->
                           this.bearerToken = bearerToken);
  }

  private void setupPicasso() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(BuildConfig.DEBUG ? Level.HEADERS : Level.NONE);
    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor((Chain chain) ->
            chain.proceed(
                chain.request()
                     .newBuilder()
                     .addHeader("Authorization", bearerToken)
                     .build()
            )
        )
        .addInterceptor(interceptor)
        .build();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(client))
            .loggingEnabled(BuildConfig.DEBUG)
            .build()
    );
  }
}
