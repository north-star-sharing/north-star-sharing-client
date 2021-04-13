package edu.cnm.deepdive.northstarsharingclient.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.controller.activity.LoginActivity;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_options_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.sign_out:
        GoogleSignInService.getInstance().signOut()
            .addOnCompleteListener((ignore) -> {
              Intent intent = new Intent(this, LoginActivity.class)
                  .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
            });
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }
}