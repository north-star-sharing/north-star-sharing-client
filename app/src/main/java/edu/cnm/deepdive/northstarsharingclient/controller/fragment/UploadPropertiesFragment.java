package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageDetailNewBinding;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.MainViewModel;

public class UploadPropertiesFragment extends DialogFragment implements TextWatcher {

  private FragmentImageDetailNewBinding binding;
  private Uri uri;
  private AlertDialog dialog;
  private MainViewModel viewModel;

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    uri =
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {

  }
}
