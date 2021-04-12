package edu.cnm.deepdive.northstarsharingclient.controller.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.northstarsharingclient.databinding.FragmentImageListBinding;


public class ImageListFragment extends Fragment {

  private FragmentImageListBinding binding;
  public ImageListFragment() {
    // Required empty public constructor
  }

  public static ImageListFragment newInstance(String param1, String param2) {
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
    binding = FragmentImageListBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}