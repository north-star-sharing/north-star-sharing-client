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
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageDetailEditBinding;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageTechnicalDetailDialogBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Image;

public class ImagePreviewFragment extends DialogFragment {

  private Image image;

  public static ImagePreviewFragment newInstance(Image image) {
    ImagePreviewFragment fragment = new ImagePreviewFragment();
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
    FragmentImageDetailEditBinding binding = FragmentImageDetailEditBinding.inflate(
        LayoutInflater.from(getContext()));
    if (image.getHref() != null) {
      Picasso
          .get()
          .load(String
              .format(BuildConfig.CONTENT_FORMAT, image.getHref()))
          .into(binding.image);
    }
    binding.description.setText(
        (image.getDescription() != null) ? image.getDescription() : "Not included");
    binding.galleryTitle.setText(
        (image.getGalleryTitle() != null) ? image.getGalleryTitle() : "Not added to a gallery");
    return new AlertDialog.Builder(getContext())
        .setTitle((image.getTitle() != null) ? image.getTitle() : "Untitled")
        .setView(binding.getRoot())
        .setNeutralButton("Close", (dlg, which) -> {
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
