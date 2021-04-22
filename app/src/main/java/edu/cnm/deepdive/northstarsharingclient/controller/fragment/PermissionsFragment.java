package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link DialogFragment} that requests permissions from the user to access Android system services,
 * such as Internet, location, camera, etc.
 */
public class PermissionsFragment extends DialogFragment {

  private static final String PERMISSIONS_TO_EXPLAIN_KEY = "permissions_to_explain";
  private static final String PERMISSIONS_TO_REQUEST_KEY = "permissions_to_request";
  private static final String EXPLANATION_KEY_SUFFIX = "_explanation";
  private static final String PERMISSION_DELIMITER = "\\.";

  @NonNull
  public static PermissionsFragment createInstance(@NonNull String[] permissionsToExplain,
      String[] permissionsToRequest) {
    if (permissionsToExplain.length == 0) {
      throw new IllegalArgumentException();
    }
    Bundle args = new Bundle();
    args.putStringArray(PERMISSIONS_TO_EXPLAIN_KEY, permissionsToExplain);
    args.putStringArray(PERMISSIONS_TO_REQUEST_KEY, permissionsToRequest);
    PermissionsFragment fragment = new PermissionsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @SuppressWarnings("ConstantConditions")
  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    OnAcknowledgeListener listener = getListener();
    PermissionsFragmentArgs args = PermissionsFragmentArgs.fromBundle(getArguments());
    return new Builder(getContext())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.permissions_dialog_title)
        .setMessage(buildMessage(args.getPermissionsToExplain()))
        .setNeutralButton(android.R.string.ok,
            (dlg, which) -> listener.onAcknowledge(args.getPermissionsToRequest()))
        .create();
  }

  private OnAcknowledgeListener getListener() {
    OnAcknowledgeListener listener;
    Fragment parentFragment = getParentFragment();
    FragmentActivity hostActivity = getActivity();
    if (parentFragment instanceof OnAcknowledgeListener) {
      listener = (OnAcknowledgeListener) parentFragment;
    } else if (hostActivity instanceof OnAcknowledgeListener) {
      listener = (OnAcknowledgeListener) hostActivity;
    } else {
      listener = (perms) -> {
      };
    }
    return listener;
  }

  private String buildMessage(String[] permissionsToExplain) {
    //noinspection ConstantConditions
    String packageName = getContext().getPackageName();
    Resources res = getResources();
    return Arrays.stream(permissionsToExplain)
        .map((permission) -> {
          String[] permissionNameParts = permission.split(PERMISSION_DELIMITER);
          String permissionKey =
              permissionNameParts[permissionNameParts.length - 1].toLowerCase()
                  + EXPLANATION_KEY_SUFFIX;
          int explanationId = res.getIdentifier(permissionKey, "string", packageName);
          return (explanationId != 0) ? getString(explanationId) : null;
        })
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.joining("\n"));
  }

  /**
   * Callback interface for returning control to controller managing permissions request flow.
   */
  public interface OnAcknowledgeListener {

    /**
     * Continues permissions request flow after user has acknowledged permission rationales.
     *
     * @param permissionsToRequest Permissions that should be requested of user.
     */
    void onAcknowledge(String[] permissionsToRequest);

  }

}
