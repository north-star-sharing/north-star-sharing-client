package edu.cnm.deepdive.northstarsharingclient.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.northstarsharingclient.BuildConfig;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import io.reactivex.Single;

/**
 * A database utility service for persisting {@link GoogleSignIn} data to the server and
 * retrieving it.
 */
public class GoogleSignInService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private static Application context;

  private final GoogleSignInClient client;
  private final MutableLiveData<String> bearerToken;

  private GoogleSignInAccount account;

  private GoogleSignInService() {
    GoogleSignInOptions options = new GoogleSignInOptions
        .Builder()
        .requestProfile()
        .requestEmail()
        .requestId()
        .requestIdToken(BuildConfig.CLIENT_ID)
        .build();
    client = GoogleSignIn.getClient(context, options);
    bearerToken = new MutableLiveData<>();
  }

  /**
   * Set the {@link Application} {@link android.content.Context} that is using this service.
   *
   * @param context The context that is using this service.
   */
  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  /**
   * Retrieve a reference to the singleton {@link GoogleSignInService}.
   */
  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Return the user's {@link GoogleSignInAccount}.
   *
   * @return {@link GoogleSignInAccount}
   */
  public GoogleSignInAccount getAccount() {
    return account;
  }

  /**
   * Return the user's bearer token for authenticating, using a {@link LiveData} holder class that
   * can be observed by the lifecycle owner.
   *
   * @return {@link LiveData&lt;String&gt;}
   */
  public LiveData<String> getBearerToken() {
    return bearerToken;
  }

  private void setAccount(GoogleSignInAccount account) {
    this.account = account;
    if (account != null) { //testing with Postman
      this.bearerToken.postValue(String.format(BEARER_TOKEN_FORMAT, account.getIdToken()));
      Log.d(getClass().getSimpleName() + "  Bearer Token ", account.getIdToken());
    } else {
      this.bearerToken.postValue(null);
    }
  }

  /**
   * Refresh the user's account if they have logged into the app previously, using a
   * {@link LiveData} holder class that can be observed by the lifecycle owner.
   *
   * @return {@link Single&lt;GoogleSignInAccount&gt;}
   */
  public Single<GoogleSignInAccount> refresh() {
    return Single.create((emitter) ->
        client.silentSignIn()
              .addOnSuccessListener(this::setAccount)
              .addOnSuccessListener(emitter::onSuccess)
              .addOnFailureListener(emitter::onError)
    );
  }

  /**
   * Refresh the user's bearer token, using a {@link LiveData} holder class that can be observed by
   * the lifecycle owner.
   *
   * @return {@link Single&lt;String&gt;}
   */
  public Single<String> refreshBearerToken() {
    return refresh()
        .map((account) -> String.format(BEARER_TOKEN_FORMAT, account.getIdToken()));
  }

  /**
   * Begin the {@link GoogleSignIn} process to request the user's login credentials
   *
   * @param activity The {@link Activity} window that is displaying the sign in interface.
   * @param requestCode A positive {@code int} flag that is returned when the sign in activity exits.
   */
  public void startSignIn(Activity activity, int requestCode) {
    account = null;
    Intent intent = client
        .getSignInIntent();
    activity
        .startActivityForResult(intent, requestCode);
  }

  /**
   * Returns the result of a sign in attempt from the user, regardless of whether it failed or succeed.
   *
   * @param data An {@link Intent} for requesting an Oauth2 authentication with {@link GoogleSignIn}.
   * @return {@link Task&lt;GoogleSignInAccount&gt;}
   */
  public Task<GoogleSignInAccount> completeSignIn(Intent data) {
    Task<GoogleSignInAccount> task = null;
    try {
      task = GoogleSignIn
          .getSignedInAccountFromIntent(data);
      setAccount(task
          .getResult(ApiException.class));
    } catch (ApiException e) {
      // Exception will pass to onFailureListener
    }
    return task;
  }

  /**
   * Logs the user out of {@link GoogleSignIn}.
   *
   * @return {@link Task&lt;Void&gt;}
   */
  public Task<Void> signOut() {
    return client
        .signOut()
        .addOnCompleteListener((ignore) -> setAccount(null));
  }

  private void onSuccess(GoogleSignInAccount account) {
    this.account = account;
  }

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService();
  }

}
