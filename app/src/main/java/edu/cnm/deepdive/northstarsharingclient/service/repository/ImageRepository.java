package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
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
  public Single<Image> add(Uri uri, File file, String title, String description) {
    File[] filesCreated = new File[1];
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
            if (description != null) {
              RequestBody descriptionPart = RequestBody.create(description, multipartFormType);
              return serviceProxy.post(token, filePart, titlePart, descriptionPart);
            } else {
              return serviceProxy.post(token, filePart, titlePart);
            }
          }
        })
        .doFinally(() -> {
          if (filesCreated[0] != null) {
            try {
              //noinspection ResultOfMethodCallIgnored
              filesCreated[0].delete();
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
