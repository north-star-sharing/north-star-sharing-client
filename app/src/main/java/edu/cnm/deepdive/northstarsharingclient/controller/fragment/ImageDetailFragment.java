package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.BuildConfig;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageTechnicalDetailDialogBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Image;

/**
 * A {@link DialogFragment} that displays all the basic information of an {@link Image}.
 */
public class ImageDetailFragment extends DialogFragment {

  private Image image;

  /**
   * Creates a new instance of the ImageDetailFragment dialog to display an {@link Image}.
   *
   * @param {@link Image}
   * @return The instance of the ImageDetailFragment.
   */
  public static ImageDetailFragment newInstance(Image image) {
    ImageDetailFragment fragment = new ImageDetailFragment();
    Bundle args = new Bundle();
    args.putSerializable("image", image);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      image = (Image) getArguments().getSerializable("image");
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    FragmentImageTechnicalDetailDialogBinding binding =
        FragmentImageTechnicalDetailDialogBinding.inflate(
            LayoutInflater.from(getContext()));

    if (image.getHref() != null) {
      Picasso
          .get()
          .load(String
              .format(BuildConfig.CONTENT_FORMAT, image.getHref()))
          .into(binding.imageView);
    }

    binding.imageId.setText(
        (image.getId() != null) ? "Id: " + image.getId() : "N/A");
    binding.imageType.setText(
        (image.getContentType() != null) ? "Image type: " + image.getContentType() : "N/A");
    binding.imageDateCreated.setText(
        (image.getCreated() != null) ? "Created Date: " + image.getCreated() : "N/A");
    binding.imageUrl.setText(
        (image.getHref() != null) ? "URL: " + image.getHref() : "N/A");
    binding.latitude.setText(
        (image.getLatitude() != 0) ? "Latitude: " + image.getLatitude() : "N/A");
    binding.longitude.setText(
        (image.getLongitude() != 0) ? "Longitude: " + image.getLongitude() : "N/A");
    binding.azimuth.setText(
        (image.getAzimuth() != 0) ? "Azimuth: " + image.getAzimuth() : "N/A");
    binding.pitch.setText(
        (image.getPitch() != 0) ? "Pitch: " + image.getPitch() : "N/A");
    binding.roll.setText(
        (image.getRoll() != 0) ? "Roll: " + image.getRoll() : "N/A");
    return new AlertDialog.Builder(getContext())
        .setTitle((image.getTitle() != null) ? image.getTitle() : "Untitled")
        .setView(binding.getRoot())
        .setPositiveButton("Close", (dlg, which) -> {
//  Left blank to close dialog
        })
        .create();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return null;
  }
}
