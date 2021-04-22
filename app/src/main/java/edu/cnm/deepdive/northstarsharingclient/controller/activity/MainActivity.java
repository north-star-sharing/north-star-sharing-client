package edu.cnm.deepdive.northstarsharingclient.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.northstarsharingclient.MobileNavigationDirections;
import edu.cnm.deepdive.northstarsharingclient.MobileNavigationDirections.OpenNewUpload;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageListBinding;
import edu.cnm.deepdive.northstarsharingclient.service.GoogleSignInService;
import edu.cnm.deepdive.northstarsharingclient.service.SensorService;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.GalleryViewModel;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.ImageViewModel;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.PermissionViewModel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements DrawerListener {

  static final int PERMISSIONS_REQUEST_CODE = 1515;
  static final int REQUEST_IMAGE_CAPTURE = 1414;
  private final List<MenuItem> dynamicItems = new LinkedList<>();
  private final Random rng = new Random();
  private String currentPhotoPath;
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;
  private PermissionViewModel permissionViewModel;
  private GalleryViewModel galleryViewModel;
  private ImageViewModel imageViewModel;
  private NavigationView navigationView;
  private DrawerLayout drawer;
  private File image;
  private Uri uri;

  /* Camera orientation fields */
  private FragmentImageListBinding binding;
  private SensorManager sensorManager;
  private Location location;
  private float[] orientation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    binding = FragmentImageListBinding.inflate(getLayoutInflater());
    // Establish app permissions
    permissionViewModel = new ViewModelProvider(this).get(PermissionViewModel.class);
    checkPermissions();
    setUpImageViewModel();
    setUpGalleryViewModel();
    setUpCamera();
    setUpNavigation();
    // Get access to the sensor manager for establishing the camera's position/angles.
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  private void setUpCamera() {
    FloatingActionButton camera = findViewById(R.id.to_camera);
    camera.setOnClickListener((v) -> dispatchTakePictureIntent());
  }

  private void setUpNavigation() {
    drawer = findViewById(R.id.drawer_layout);
    drawer.setDrawerListener(this);
    navigationView = findViewById(R.id.nav_view);
    appBarConfiguration = new AppBarConfiguration
        .Builder(
        R.id.navigation_home)
        .setDrawerLayout(drawer)
        .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }

  private void setUpGalleryViewModel() {
    galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
    getLifecycle().addObserver(galleryViewModel);
    galleryViewModel.getThrowable()
                    .observe(this, (throwable) -> {
                      if (throwable != null) {
                        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG)
                             .show();
                      }
                    });
  }

  private void setUpImageViewModel() {
    imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
    getLifecycle().addObserver(imageViewModel);
    imageViewModel.getThrowable().observe(this, (throwable) -> {
      if (throwable != null) {
        Toast.makeText(this, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
      }
    });
    imageViewModel.getLocation().observe(this, (location)-> {
      this.location = location;
      Log.d(getClass().getName(), location.toString());
    });
    imageViewModel.getOrientation().observe(this, (orientation) -> {
      this.orientation = orientation;
      Log.d(getClass().getName(), Arrays.toString(orientation));
    });
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
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
      case R.id.setting:
        navController.navigate(R.id.navigation_settings);
        break;
      case R.id.sign_out:
        GoogleSignInService.getInstance()
                           .signOut()
                           .addOnCompleteListener((ignore) -> {
                             Intent intent = new Intent(this, LoginActivity.class)
                                 .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                     | Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(intent);
                           });
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

//  Permissions

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == PERMISSIONS_REQUEST_CODE) {
      for (int i = 0; i < permissions.length; i++) {
        String permission = permissions[i];
        int result = grantResults[i];
        if (result == PackageManager.PERMISSION_GRANTED) {
          permissionViewModel.grantPermission(permission);
        } else {
          permissionViewModel.revokePermission(permission);
        }
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }


  public void onAcknowledge(String[] permissionsToRequest) {
    ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSIONS_REQUEST_CODE);
  }

  private void checkPermissions() {
    try {
      PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
          PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
      String[] permissions = info.requestedPermissions;
      List<String> permissionsToRequest = new LinkedList<>();
      List<String> permissionsToExplain = new LinkedList<>();
      for (String permission : permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
          permissionsToRequest.add(permission);
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            permissionsToExplain.add(permission);
          }
        } else {
          permissionViewModel.grantPermission(permission);
        }
      }
      if (!permissionsToExplain.isEmpty()) {
        explainPermissions(
            permissionsToExplain.toArray(new String[0]),
            permissionsToRequest.toArray(new String[0]));
      } else if (!permissionsToRequest.isEmpty()) {
        onAcknowledge(permissionsToRequest.toArray(new String[0]));
      }
    } catch (NameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private void explainPermissions(String[] permissionsToExplain, String[] permissionsToRequest) {
    navController.navigate(
        MobileNavigationDirections.explainPermissions(permissionsToExplain, permissionsToRequest));
  }


  //  Camera
  //   TODO move to repository
  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "CELST_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    image = File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    );
    currentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      try {
        File photoFile = createImageFile();
        uri = FileProvider.getUriForFile(this,
            "edu.cnm.deepdive.northstarsharingclient",
            photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
      } catch (IOException ex) {
        // Error occurred while creating the File
        Snackbar.make(findViewById(R.id.drawer_layout), "Failed to take a picture",
            BaseTransientBottomBar.LENGTH_INDEFINITE)
                .show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
      OpenNewUpload action = MobileNavigationDirections.openNewUpload(
          0,
          "Test Title" ,
          0 ,
          "Test description");
      action.setImageUri(uri);
      action.setImageFile(image);
      // Set camera GPS coordinates here.
      if (location != null) {
        action.setLatitude(location.getLatitude());
        action.setLongitude(location.getLongitude());
      }
      if (orientation != null) {
        action.setAzimuth(orientation[0]);
        action.setPitch(orientation[1]);
        action.setRoll(orientation[2]);
      }
      navController.navigate(action);
    }
  }

  @Override
  public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {

  }

  @Override
  public void onDrawerOpened(@NonNull @NotNull View drawerView) {
//    TODO From the field containing the list of galleries add each gallery to the menu in the
//     fallowing line
    Menu menu = navigationView.getMenu();
    MenuItem itemStar = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Stars");
    itemStar.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Stars");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemNubile = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Nubile");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Nubile");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemBlackHoles = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Black Holes");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Black Holes");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemComets = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Comets");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Comets");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemPlanets = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Planets");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Planets");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemMoons = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Moons");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Moons");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    MenuItem itemMMS = menu.add(Menu.NONE, rng.nextInt(), Menu.NONE, "Man Made Satellite's");
    itemNubile.setOnMenuItemClickListener((mi) -> {
      Log.d(getClass().getName(), "New Menu Man Made Satellite's");
      drawer.closeDrawer(GravityCompat.START, true);
      return true;
    });
    dynamicItems.add(itemStar);
    dynamicItems.add(itemNubile);
    dynamicItems.add(itemBlackHoles);
    dynamicItems.add(itemComets);
    dynamicItems.add(itemPlanets);
    dynamicItems.add(itemMoons);
    dynamicItems.add(itemMMS);
  }

  @Override
  public void onDrawerClosed(@NonNull @NotNull View drawerView) {
    Menu menu = navigationView.getMenu();
    for (MenuItem item : dynamicItems) {
      menu.removeItem(item.getItemId());
    }
    dynamicItems.clear();
  }

  @Override
  public void onDrawerStateChanged(int newState) {

  }

}