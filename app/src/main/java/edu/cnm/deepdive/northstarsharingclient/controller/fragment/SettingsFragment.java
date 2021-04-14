package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import edu.cnm.deepdive.northstarsharingclient.R;

/**
 * Fragment to create and manage the widgets on the Settings screen.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);
  }
}