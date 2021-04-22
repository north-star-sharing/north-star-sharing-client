package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.R;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentUploadPropertiesBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.GalleryViewModel;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.ImageViewModel;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class UploadPropertiesFragment extends DialogFragment implements TextWatcher {

  private FragmentUploadPropertiesBinding binding;
  private Uri uri;
  private File file;
  private String title;
  private Gallery gallery;
  private String description;
  private AlertDialog dialog;
  private ImageViewModel imageViewModel;
  private GalleryViewModel galleryViewModel;
  private List<Gallery> galleryList;
  private float azimuth;
  private float pitch;
  private float roll;
  private double latitude;
  private double longitude;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UploadPropertiesFragmentArgs args = UploadPropertiesFragmentArgs.fromBundle(getArguments());
    uri = args.getImageUri();
    file = args.getImageFile();
    description = args.getDescription();
    title = args.getImageTitle();
    azimuth = args.getAzimuth();
    pitch = args.getPitch();
    roll = args.getRoll();
    latitude = (args.getLatitude() != null) ? args.getLatitude() : 0;
    longitude = (args.getLongitude() != null) ? args.getLongitude() : 0;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    binding =
        FragmentUploadPropertiesBinding.inflate(LayoutInflater.from(getContext()), null,
            false);
    dialog = new Builder(getContext())
        .setIcon(R.drawable.ic_add)
        .setTitle(R.string.store_dialog)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.cancel, (dlg, which) -> {/* No need to do anything. */})
        .setPositiveButton(android.R.string.ok, (dlg, which) -> upload())
        .create();
    dialog.setOnShowListener((dlg) -> {
      binding.galleryTitle.addTextChangedListener(this);
      binding.imageTitle.addTextChangedListener(this);
      checkSubmitConditions();
    });
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getActivity()
        .getWindow()
        .setFlags(LayoutParams.FLAG_NOT_TOUCHABLE, LayoutParams.FLAG_NOT_TOUCHABLE);
    Picasso
        .get()
        .load(uri)
        .into(binding.image);
    binding.imageTitle.addTextChangedListener(this);
    binding.galleryTitle.addTextChangedListener(this);
    //noinspection ConstantConditions
    imageViewModel = new ViewModelProvider(getActivity()).get(ImageViewModel.class);
    // TODO Observe as necessary, in the ImageListFragment
    // Populate the spinner
    galleryViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
    galleryViewModel
        .getGalleryList()
        .observe(getViewLifecycleOwner(),
            (galleryList) -> {
              this.galleryList = galleryList;
              AutoCompleteTextView simpleAutoText = binding.galleryTitle;
              ArrayAdapter<Gallery> adapter = new ArrayAdapter<>(
                  getContext(), android.R.layout.simple_list_item_1, galleryList);
              simpleAutoText.setThreshold(1);
              simpleAutoText.setAdapter(adapter);
              getActivity().getWindow()
                           .clearFlags(LayoutParams.FLAG_NOT_TOUCHABLE);
              binding.workingIndicator.setVisibility(View.GONE);
            });
  }


  private void checkSubmitConditions() {
    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    String galleryTitle = binding.galleryTitle.getText()
                                              .toString()
                                              .trim();
    UUID galleryId = null;
    for (Gallery g : galleryList) {
      if (g != null && galleryTitle.equals(g.getTitle())) {
        galleryId = g.getId();
      }
    }
    //noinspection ConstantConditions
    positive.setEnabled(galleryId != null && !binding.imageTitle.getText()
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
    // TODO Access the bundle of arguments.
//    OpenNewUpload action = MobileNavigationDirections.
//     args = UploadPropertiesFragmentArgs.fromBundle(getArguments());

    String galleryTitle = binding.galleryTitle.getText()
                                              .toString()
                                              .trim();
    UUID galleryId = null;
    for (Gallery g : galleryList) {
      if (g != null && galleryTitle.equals(g.getTitle())) {
        galleryId = g.getId();
      }
    }
    imageViewModel.store(uri, file, title, (description.isEmpty() ? null : description), azimuth,
        pitch, roll, latitude, longitude, galleryId);
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
