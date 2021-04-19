package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageDetailNewBinding;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.MainViewModel;

public class UploadPropertiesFragment extends DialogFragment implements TextWatcher {

  private FragmentImageDetailNewBinding binding;
  private Uri uri;
  private AlertDialog dialog;
  private MainViewModel viewModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    uri = A.fromBundle(getArguments()).getContentUri;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding =
        FragmentImageDetailNewBinding.inflate(LayoutInflater.from(getContext()), null, false);
    dialog = new Builder(getContext())
        .setIcon(R.drawable.ic_add)
        .setTitle(R.string.store_dialog)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {/* No need to do anything. */})
        .setPositiveButton(android.R.string.ok, (dlg, which) -> upload())
        .create();
    dialog.setOnShowListener((dlg) -> checkSubmitConditions());
    return dialog;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Picasso
        .get()
        .load(uri)
        .into(binding.image);
    binding.imageTitle.addTextChangedListener(this);
    //noinspection ConstantConditions
    viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    // TODO Observe as necessary, in the ImageListFragment
  }


  private void checkSubmitConditions() {
    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    //noinspection ConstantConditions
    positive.setEnabled(!binding.imageTitle.getText()
                                           .toString()
                                           .trim()
                                           .isEmpty());
  }

  @SuppressWarnings("ConstantConditions")
  private void upload() {
    String title = binding.imageTitle.getText()
                                     .toString()
                                     .trim();
    String description = binding.description.getText()
                                            .toString()
                                            .trim();
    viewModel.store(uri, title, (description.isEmpty() ? null : description));
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    checkSubmitConditions();
  }
}
