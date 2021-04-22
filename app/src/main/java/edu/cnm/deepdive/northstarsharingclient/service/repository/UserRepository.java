package edu.cnm.deepdive.northstarsharingclient.service.repository;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.NorthStarSharingWebServiceProxy;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * A database repository utility for persisting {@link User Users} to the server and
 * retrieving them.
 */
public class UserRepository {

  private final Context context;
  private final GoogleSignInService signInService;
  private final NorthStarSharingWebServiceProxy webServiceProxy;

  /**
   * Create a {@link UserRepository} for persisting {@link User Users} to the server and retrieving
   * them. A {@link Context} may be required for some operations.
   *
   * @param context {@link Context}
   */
  public UserRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    webServiceProxy = NorthStarSharingWebServiceProxy.getInstance();
  }

  /**
   * Retrieve the user's account from the {@link GoogleSignInService}
   *
   * @return {@link GoogleSignInAccount}
   */
  public GoogleSignInAccount getAccount() {
    return signInService.getAccount();
  }

  /**
   * Retrieve the user's profile from the {@link GoogleSignInService} asynchronously, using a
   * {@link Single} ReactiveX value response.
   *
   * @return {@link Single&lt;User&gt;}
   */
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
