package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.Context;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;

/**
 * A database repository utility for persisting {@link Gallery Galleries} to the server and
 * retrieving them.
 */
public class GalleryRepository {

  private final Context context;
  private final NorthStarSharingWebServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  /**
   * Create a {@link GalleryRepository}. A {@link Context} may be required for some operations.
   *
   * @param context {@link Context}
   */
  public GalleryRepository(Context context) {
    this.context = context;
    serviceProxy = NorthStarSharingWebServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  /**
   * Gets a single {@link Gallery} asynchronously, using a {@link Single} ReactiveX value response.
   *
   * @param id The unique identifier of the Gallery.
   * @return {@link Single&lt;Gallery&gt;}
   */
  public Single<Gallery> getGallery(UUID id) {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap((account) -> serviceProxy.getGallery(id, account));
  }

/**
 * Gets a {@link java.util.List} of all
 * {@link edu.cnm.deepdive.northstarsharingclient.model.Gallery Galleries} asynchronously, using a
 * {@link Single} ReactiveX value response.
 *
 * @return {@link Single&lt;List&lt;Gallery&gt;&gt;}
 */
  public Single<List<Gallery>> getGalleryList() {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap(serviceProxy::getGalleryList);
  }
}

