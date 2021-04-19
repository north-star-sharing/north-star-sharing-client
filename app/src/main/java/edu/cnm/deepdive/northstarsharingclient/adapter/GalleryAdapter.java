package edu.cnm.deepdive.northstarsharingclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.northstarsharingclient.adapter.GalleryAdapter.Holder;
import edu.cnm.deepdive.northstarsharingclient.databinding.ItemImageBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GalleryAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final LayoutInflater inflater;
  private final List<Gallery> galleryList;
  private final OnGalleryClickHelper onGalleryClickHelper;

  public GalleryAdapter(Context context, List<Gallery> galleryList,
      OnGalleryClickHelper onGalleryClickHelper) {
    this.context = context;
    inflater = LayoutInflater.from(context);
    this.galleryList = galleryList;
    this.onGalleryClickHelper= onGalleryClickHelper;
  }

  @NotNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemImageBinding binding = ItemImageBinding.inflate(inflater, parent, false);
    return new Holder(binding, onGalleryClickHelper);
  }

  @Override
  public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return galleryList.size();
  }


  class Holder extends RecyclerView.ViewHolder implements OnClickListener {

    private final ItemImageBinding binding;
    OnGalleryClickHelper onGalleryClickHelper;
    private Gallery gallery;

    public Holder(ItemImageBinding binding, OnGalleryClickHelper onGalleryClickHelper) {
      super(binding.getRoot());
      this.binding = binding;
      this.onGalleryClickHelper = onGalleryClickHelper;
      binding.getRoot().setOnClickListener((OnClickListener) this);
    }

    private void bind(int position) {
      gallery = galleryList.get(position);
      binding.thumbnailTitle.setText(gallery.getTitle());
      binding.getRoot().setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
      onGalleryClickHelper
          .onGalleryClick(
              galleryList
                  .get(getAdapterPosition())
                  .getId()
                  .toString(),
              v);
    }
  }

  public interface OnGalleryClickHelper {
    void onGalleryClick(String galleryId, View view);
  }
}
