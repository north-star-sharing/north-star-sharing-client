package edu.cnm.deepdive.northstarsharingclient.controller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;

public class LoginActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 0322;
  private GoogleSignInService signInService;
  private ActivityLoginBinding binding;

  @SuppressLint("CheckResult")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    signInService = GoogleSignInService.getInstance();
    signInService
        .refresh()
        .subscribe(
            this::switchToMain,
            (throwable) -> {
              binding = ActivityLoginBinding.inflate(getLayoutInflater());
              binding.signInButton.setOnClickListener((c) ->
                  signInService.startSignIn(this, LOGIN_REQUEST_CODE));
              setContentView(binding.getRoot());
            });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == LOGIN_REQUEST_CODE) {
      signInService.completeSignIn(data)
          .addOnSuccessListener(this::switchToMain)
          .addOnFailureListener((throwable) ->
              Toast.makeText(this,
                  getString(R.string.login_failure),
                  Toast.LENGTH_LONG)
                  .show());
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void switchToMain(GoogleSignInAccount account) {
    Intent intent = new Intent(this, MainActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
}