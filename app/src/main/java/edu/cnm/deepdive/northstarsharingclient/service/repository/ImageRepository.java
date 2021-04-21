package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
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

public class ImageRepository {

  private final Context context;
  private final NorthStarSharingWebServiceProxy serviceProxy;
  private final GoogleSignInService signInService;
  private final ContentResolver resolver;
  private final MediaType multipartFormType;

  public ImageRepository(Context context) {
    this.context = context;
    serviceProxy = NorthStarSharingWebServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
    resolver = context.getContentResolver();
    multipartFormType = MediaType.parse("multipart/form-data");
  }

  @SuppressWarnings("BlockingMethodInNonBlockingContext")
  public Single<Image> add(Uri uri, File file, String title, String description, String azimuth,
      String pitch, String roll, String latitude, String longitude,UUID galleryId) {
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
            RequestBody azimuthPart = RequestBody.create(azimuth, multipartFormType);
            RequestBody pitchPart = RequestBody.create(pitch, multipartFormType);
            RequestBody rollPart = RequestBody.create(roll, multipartFormType);
            RequestBody latitudePart = RequestBody.create(latitude, multipartFormType);
            RequestBody longitudePart = RequestBody.create(longitude, multipartFormType);
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

  public Single<List<Image>> getAll() {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap(serviceProxy::getAllImages);
  }
}
