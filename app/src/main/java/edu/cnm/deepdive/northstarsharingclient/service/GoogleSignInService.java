package edu.cnm.deepdive.northstarsharingclient.service;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.northstarsharingclient.BuildConfig;
import io.reactivex.Single;

public class GoogleSignInService {

  private static Application context;

  private final GoogleSignInClient client;

  private GoogleSignInAccount account;

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private GoogleSignInService() {
    GoogleSignInOptions options = new GoogleSignInOptions
        .Builder()
        .requestProfile()
        .requestEmail()
        .requestId()
        .requestIdToken(BuildConfig.CLIENT_ID)
        .build();

    client = GoogleSignIn.getClient(context, options);
  }

  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public GoogleSignInAccount getAccount() {
    return account;
  }

  private void setAccount(GoogleSignInAccount account) {
    this.account = account;
    if (account != null) { //testing with Postman
      Log.d(getClass().getSimpleName() + "  Bearer Token ", account.getIdToken());
    }
  }

  public Single<GoogleSignInAccount> refresh() {
    return Single.create((emitter) ->
        client.silentSignIn()
              .addOnSuccessListener(this::setAccount)
              .addOnSuccessListener(emitter::onSuccess)
              .addOnFailureListener(emitter::onError)
    );
  }

  public Single<String> refreshBearerToken() {
    return refresh()
        .map((account) -> String.format(BEARER_TOKEN_FORMAT, account.getIdToken()));
  }

  public void startSignIn(Activity activity, int requestCode) {
    account = null;
    Intent intent = client
        .getSignInIntent();
    activity
        .startActivityForResult(intent, requestCode);
  }

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
