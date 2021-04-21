package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.northstarsharingclient.adapter.ImageAdapter;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageListBinding;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.GalleryViewModel;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;


public class ImageListFragment extends Fragment {

  private FragmentImageListBinding binding;
  private GalleryViewModel galleryViewModel;
  private UUID galleryId;
  private AlertDialog dialog;

  public ImageListFragment() {
    // Required empty public constructor
  }

  public static ImageListFragment newInstance() {
    ImageListFragment fragment = new ImageListFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

//  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//    binding = FragmentImageListBinding.inflate(LayoutInflater.from(getContext()), null, false);
//    dialog = new Builder(AlertDialog)
//        .set
//  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentImageListBinding.inflate(inflater, container, false);
//    binding.toCamera.setOnClickListener((click) -> dialog);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    galleryViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
    if (getArguments() != null) {
      ImageListFragmentArgs args = ImageListFragmentArgs.fromBundle(getArguments());
      // TODO Filter which images are shown in the recycle view by gallery ID.
      galleryId = UUID.fromString(args.getGalleryImages());
    }
    galleryViewModel.getGallery(galleryId);
    galleryViewModel.getGallery().observe(getViewLifecycleOwner(), (gallery) -> {
      if (gallery.getImages() != null) {
        binding.homeScreenImageList.setAdapter(new ImageAdapter(getContext(), gallery.getImages(), this));
      }
    });
    galleryViewModel.getThrowable().observe(getViewLifecycleOwner(), (throwable) -> {
      if (throwable != null) {
        Snackbar.make(binding.getRoot(), throwable.getMessage(),
            BaseTransientBottomBar.LENGTH_INDEFINITE).show();
      }
    });
  }
}