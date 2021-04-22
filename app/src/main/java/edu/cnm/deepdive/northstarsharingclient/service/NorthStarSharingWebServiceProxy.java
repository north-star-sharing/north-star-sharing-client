package edu.cnm.deepdive.northstarsharingclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.northstarsharingclient.BuildConfig;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import io.reactivex.Single;
import java.util.List;
import java.util.UUID;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * An interface used by {@link Retrofit} to dynamically construct the HTTP GET and POST requests
 * to the web service.
 */
public interface NorthStarSharingWebServiceProxy {

  /**
   * Get a reference to the singleton instance of the {@link NorthStarSharingWebServiceProxy}.
   *
   * @return {@link NorthStarSharingWebServiceProxy}
   */
  static NorthStarSharingWebServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Get user's {@link com.google.android.gms.auth.api.signin.GoogleSignIn} profile asynchronously,
   * using a {@link Single} ReactiveX value response
   *
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @return {@link Single&lt;User&gt;}
   */
  @GET("users/me")
  Single<User> getProfile(@Header("Authorization") String bearerToken);

  /**
   * POST a single {@link Image} to the web service without a description asynchronously, using a
   * {@link Single} ReactiveX value response.
   *
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @param file The {@link Image} {@link java.io.File} to be persisted.
   * @param title The user-entered title.
   * @param azimuth The azimuth of the camera at the time of the picture.
   * @param pitch The pitch of the camera at the time of the picture.
   * @param roll The roll of the camera at the time of the picture.
   * @param latitude The latitude of the camera at the time of the picture.
   * @param longitude The longitude of the camera at the time of the picture.
   * @param id The unique identifier for the {@link Gallery} that will contain this {@link Image} sent as a path variable.
   * @return {@link Single&lt;Image&gt;}
   */
  @Multipart
  @POST("galleries/{id}/images")
  Single<Image> post(
      @Header("Authorization") String bearerToken,
      @Part MultipartBody.Part file,
      @Part("title") RequestBody title,
      @Part("azimuth") RequestBody azimuth,
      @Part("pitch") RequestBody pitch,
      @Part("roll") RequestBody roll,
      @Part("latitude") RequestBody latitude,
      @Part("longitude") RequestBody longitude,
      @Path("id") UUID id);

  /**
   * POST a single {@link Image} to the web service with a description asynchronously, using a
   * {@link Single} ReactiveX value response.
   *
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @param file The {@link Image} {@link java.io.File} to be persisted.
   * @param title The user-entered title.
   * @param description The user-entered description.
   * @param azimuth The azimuth of the camera at the time of the picture.
   * @param pitch The pitch of the camera at the time of the picture.
   * @param roll The roll of the camera at the time of the picture.
   * @param latitude The latitude of the camera at the time of the picture.
   * @param longitude The longitude of the camera at the time of the picture.
   * @param id The unique identifier for the {@link Gallery} that will contain this {@link Image} sent as a path variable.
   * @return {@link Single&lt;Image&gt;}
   */
  @Multipart
  @POST("galleries/{id}/images")
  Single<Image> post(
      @Header("Authorization") String bearerToken,
      @Part MultipartBody.Part file,
      @Part("title") RequestBody title,
      @Part("description") RequestBody description,
      @Part("azimuth") RequestBody azimuth,
      @Part("pitch") RequestBody pitch,
      @Part("roll") RequestBody roll,
      @Part("latitude") RequestBody latitude,
      @Part("longitude") RequestBody longitude,
      @Path("id") UUID id);

  /**
   * Retrieve a {@link List} of {@link Image Images} asynchronously, using a {@link Single}
   * ReactiveX value response.
   *
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @return {@link Single&lt;List&lt;Image&gt;&gt;}
   */
  @GET("images")
  Single<List<Image>> getAllImages(@Header("Authorization") String bearerToken);

  /**
   * Retrieve a {@link Gallery} asynchronously, using a {@link Single}
   * ReactiveX value response.
   *
   * @param id The unique identifier for the {@link Gallery} sent as a path variable.
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @return {@link Single&lt;List&lt;Image&gt;&gt;}
   */
  @GET("galleries/{galleryId}")
  Single<Gallery> getGallery(@Path("id") UUID id, @Header("Authorization") String bearerToken);

  /**
   * Retrieve a {@link List} of all {@link Gallery Galleries} asynchronously, using a {@link Single}
   * ReactiveX value response.
   *
   * @param bearerToken The Oauth2 bearer token used for authorizing the request.
   * @return {@link Single&lt;List&lt;Image&gt;&gt;}
   */
  @GET("galleries")
  Single<List<Gallery>> getGalleryList(@Header("Authorization") String bearerToken);

  /**
   * A helper class to create and contain a singleton instance of the
   * {@link NorthStarSharingWebServiceProxy}.
   */
  class InstanceHolder {

    private static final NorthStarSharingWebServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(BuildConfig.DEBUG ? Level.HEADERS : Level.NONE);
      OkHttpClient okClient = new OkHttpClient
          .Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit
          .Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(okClient)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(NorthStarSharingWebServiceProxy.class);
    }
  }

}
