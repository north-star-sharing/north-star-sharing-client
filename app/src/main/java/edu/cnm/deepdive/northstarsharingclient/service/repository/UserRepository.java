package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

  private final Context context;
  private final GoogleSignInService signInService;
  private final NorthStarSharingWebServiceProxy webServiceProxy;


  public UserRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    webServiceProxy = NorthStarSharingWebServiceProxy.getInstance();
  }

  public GoogleSignInAccount getAccount() {
    return signInService.getAccount();
  }

  public Single<User> getUserProfile() {
    return signInService.refresh()
        .observeOn(Schedulers.io())
        .flatMap((account) ->
            webServiceProxy.getProfile(getBearerToken(account.getIdToken())))
        .subscribeOn(Schedulers.io());
  }

  private String getBearerToken(String idToken) {
    return String.format("Bearer %s", idToken);
  }

}
