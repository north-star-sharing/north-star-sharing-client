package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A database repository utility for persisting {@link Image Images} to the server and retrieving
 * them.
 */
public class ImageRepository {

  private final Context context;
  private final NorthStarSharingWebServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final ContentResolver resolver;
  private final MediaType multipartFormType;

  /**
   * Create an {@link ImageRepository}. A {@link Context} may be required for some operations.
   *
   * @param context {@link Context}
   */
  public ImageRepository(Context context) {
    this.context = context;
    serviceProxy = NorthStarSharingWebServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
    resolver = context.getContentResolver();
    multipartFormType = MediaType.parse("multipart/form-data");
  }

  /**
   * Adds a single {@link Image} asynchronously, using a {@link Single} ReactiveX value response.
   *
   * @param uri The location of the file in local storage.
   * @param file A reference to the {@link File} object.
   * @param title The user-entered title.
   * @param description The user-entered description.
   * @param azimuth The azimuth of the camera at the time of the picture.
   * @param pitch The pitch of the camera at the time of the picture.
   * @param roll The roll of the camera at the time of the picture.
   * @param latitude The latitude of the camera at the time of the picture.
   * @param longitude The longitude of the camera at the time of the picture.
   * @param galleryId The {@link Gallery} that was selected by the user.
   * @return {@link Single&lt;Image&gt;}
   */
  @SuppressWarnings("BlockingMethodInNonBlockingContext")
  public Single<Image> add(Uri uri, File file, String title, String description, float azimuth,
      float pitch, float roll, double latitude, double longitude, UUID galleryId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> {
          try (
              Cursor cursor = resolver.query(uri, null, null, null, null);
              InputStream input = resolver.openInputStream(uri)
          ) {
            MediaType type = MediaType.parse(resolver.getType(uri));
            RequestBody fileBody = RequestBody.create(file, type);
            MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("file", file.getName(), fileBody);
            RequestBody titlePart = RequestBody.create(title, multipartFormType);
            RequestBody azimuthPart = RequestBody.create(String.valueOf(azimuth), multipartFormType);
            RequestBody pitchPart = RequestBody.create(String.valueOf(pitch), multipartFormType);
            RequestBody rollPart = RequestBody.create(String.valueOf(roll), multipartFormType);
            RequestBody latitudePart = RequestBody.create(String.valueOf(latitude), multipartFormType);
            RequestBody longitudePart = RequestBody.create(String.valueOf(longitude), multipartFormType);
            Log.d(getClass().getName(), String.format("Azimuth = %f, Pitch = %f, Roll = %f, Latitude = %f, Longitude = %f", azimuth, pitch, roll, latitude, longitude));
            if (description != null) {
              RequestBody descriptionPart = RequestBody.create(description, multipartFormType);
              return serviceProxy.post(token, filePart, titlePart, descriptionPart, azimuthPart,
                  pitchPart, rollPart, latitudePart, longitudePart, galleryId);
            } else {
              return serviceProxy.post(token, filePart, titlePart, azimuthPart,
                  pitchPart, rollPart, latitudePart, longitudePart, galleryId);
            }
          }
        })
        .doFinally(() -> {
          if (file != null) {
            try {
              //noinspection ResultOfMethodCallIgnored
              file.delete();
            } catch (Exception e) {
              Log.e(getClass().getName(), e.getMessage(), e);
            }
          }
        });
  }

  /**
   * Get a {@link List} of all {@link Image Images} from the server asynchronously, using a
   * {@link Single} ReactiveX value response.
   *
   * @return {@link Single&lt;List&lt;Image&gt&gt;}
   */
  public Single<List<Image>> getAll() {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap(serviceProxy::getAllImages);
  }
}
