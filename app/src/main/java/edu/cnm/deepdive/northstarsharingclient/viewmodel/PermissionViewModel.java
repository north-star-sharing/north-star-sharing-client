package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link androidx.lifecycle.ViewModel} to hold the {@link LiveData} for the Android permissions
 * information.
 */
public class PermissionViewModel extends ViewModel {

  private final MutableLiveData<Set<String>> permissions = new MutableLiveData<>(new HashSet<>());

  public LiveData<Set<String>> getPermissions() {
    return permissions;
  }

  public void grantPermission(String permission) {
    Set<String> permissions = this.permissions.getValue();
    //noinspection ConstantConditions
    if (permissions.add(permission)) {
      this.permissions.setValue(permissions);
    }
  }

  public void revokePermission(String permission) {
    Set<String> permissions = this.permissions.getValue();
    //noinspection ConstantConditions
    if (permissions.remove(permission)) {
      this.permissions.setValue(permissions);
    }
  }

}
