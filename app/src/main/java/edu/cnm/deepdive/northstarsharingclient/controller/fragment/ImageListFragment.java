package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import edu.cnm.deepdive.northstarsharingclient.adapter.ImageAdapter;
import edu.cnm.deepdive.northstarsharingclient.adapter.ImageAdapter.OnImageClickHelper;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageListBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.GalleryViewModel;
import edu.cnm.deepdive.northstarsharingclient.viewmodel.ImageViewModel;
import java.util.List;
import java.util.UUID;

/**
 * A {@link Fragment} that displays a scrolling list of {@link Image}s. Each element in the list is
 * clickable to open up an {@link ImagePreviewFragment} for that specific image. Long-pressing an
 * element opens a card to display meta-data about the Image.
 */
public class ImageListFragment extends Fragment implements OnImageClickHelper {

  private FragmentImageListBinding binding;
  private GalleryViewModel galleryViewModel;
  private ImageViewModel imageViewModel;
  private UUID galleryId;
  private AlertDialog dialog;
  private List<Images> images;

  /**
   * Create a new instance of the {@link ImageListFragment}.
   *
   * @return The instance of the ImageDetailFragment.
   */
  public static ImageListFragment newInstance() {
    ImageListFragment fragment = new ImageListFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentImageListBinding.inflate(inflater);
//    binding.toCamera.setOnClickListener((click) -> ); TODO open nav to custom camera
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //noinspection ConstantConditions
    imageViewModel = new ViewModelProvider(getActivity()).get(ImageViewModel.class);
    imageViewModel.loadImages();
    imageViewModel.getImageList()
                  .observe(getViewLifecycleOwner(), (imageList) -> {
                    if (imageList != null) {
                      binding.homeScreenImageList.setAdapter(
                          new ImageAdapter(getContext(), imageList, this));
                    }
                  });
    galleryViewModel = new ViewModelProvider(getActivity()).get(GalleryViewModel.class);
//    if (getArguments() != null) {
//      ImageListFragmentArgs args = ImageListFragmentArgs.fromBundle(getArguments());
      // TODO Filter which images are shown in the recycle view by gallery ID.
//      galleryId = UUID.fromString(args.());
//    }
//    galleryViewModel.getGallery(galleryId);

    imageViewModel.getThrowable()
                  .observe(getViewLifecycleOwner(), (throwable) -> {
                    if (throwable != null) {
                      //noinspection ConstantConditions
                      Snackbar.make(binding.getRoot(), throwable.getMessage(),
                          BaseTransientBottomBar.LENGTH_INDEFINITE)
                              .show();
                    }
                  });
  }

  @Override
  public void onImageLongClick(Image image, int position) {
    ImageDetailFragment fragment = ImageDetailFragment.newInstance(image);
    fragment.show(getChildFragmentManager(), fragment.getClass().getName());
  }

  @Override
  public void onImageClick(Image image, int position) {
    ImagePreviewFragment fragment = ImagePreviewFragment.newInstance(image);
    fragment.show(getChildFragmentManager(), fragment.getClass().getName());
  }

}