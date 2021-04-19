package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageListBinding;


public class ImageListFragment extends Fragment {

  private FragmentImageListBinding binding;
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
}