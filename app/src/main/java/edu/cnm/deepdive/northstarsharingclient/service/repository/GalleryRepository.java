package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.Context;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;

public class GalleryRepository {

  private final Context context;
  private final NorthStarSharingWebServiceProxy serviceProxy;
  private final GoogleSignInService signInService;

  public GalleryRepository(Context context) {
    this.context = context;
    serviceProxy = NorthStarSharingWebServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  public Single<Gallery> getGallery(UUID id) {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap((account) -> serviceProxy.getGallery(id, account));
  }

  public Single<List<Gallery>> getGalleryList() {
    return signInService.refreshBearerToken()
                        .observeOn(Schedulers.io())
                        .flatMap(serviceProxy::getGalleryList);
  }
}

