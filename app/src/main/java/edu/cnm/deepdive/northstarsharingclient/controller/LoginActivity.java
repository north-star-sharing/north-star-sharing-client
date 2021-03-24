package edu.cnm.deepdive.northstarsharingclient.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
            });
    setContentView(binding.getRoot());
  }

  private void switchToMain(GoogleSignInAccount account) {
    Intent intent = new Intent(this, MainActivity.class)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
}