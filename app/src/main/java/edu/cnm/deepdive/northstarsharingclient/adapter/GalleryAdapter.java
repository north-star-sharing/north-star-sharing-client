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

/**
 * A {@link RecyclerView.Adapter} to display a scrolling list of {@link Gallery Galleries}.
 */
public class GalleryAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final LayoutInflater inflater;
  private final List<Gallery> galleryList;
  private final OnGalleryClickHelper onGalleryClickHelper;

  /**
   * Create an instance of a {@link GalleryAdapter} to manage the scrolling list of {@link Gallery
   * Galleries}.
   *
   * @param context              The application {@link Context} in which the instance is being
   *                             used.
   * @param galleryList          The {@link List} of {@link Gallery Galleries} displayed by the
   *                             {@link RecyclerView}
   * @param onGalleryClickHelper An implementation of the {@link OnGalleryClickHelper} to handle
   *                             {@link OnClickListener} events
   */
  public GalleryAdapter(Context context, List<Gallery> galleryList,
      OnGalleryClickHelper onGalleryClickHelper) {
    this.context = context;
    inflater = LayoutInflater.from(context);
    this.galleryList = galleryList;
    this.onGalleryClickHelper = onGalleryClickHelper;
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

  /**
   * An interface that handles {@link OnClickListener} events for {@link Gallery} items.
   */
  public interface OnGalleryClickHelper {

    /**
     * The action to perform when clicking a {@link Gallery} in the {@link RecyclerView}.
     *
     * @param galleryId A unique identifier for the {@link Gallery} that is being clicked
     * @param view      Which layout to display the {@link Gallery Gallery's} {@link
     *                  edu.cnm.deepdive.northstarsharingclient.model.Image Images} in
     */
    void onGalleryClick(String galleryId, View view);
  }

  /**
   * A nested class of {@link GalleryAdapter} to manage the behavior of an individual {@link
   * Gallery} in the {@link RecyclerView}.
   */
  class Holder extends RecyclerView.ViewHolder implements OnClickListener {

    private final ItemImageBinding binding;
    OnGalleryClickHelper onGalleryClickHelper;
    private Gallery gallery;

    /**
     * Create an instance of a {@link Holder}
     *
     * @param binding              The binding to the layout that will be used for displaying a
     *                             single {@link Gallery} element
     * @param onGalleryClickHelper The {@link OnGalleryClickHelper} that will handle click actions.
     */
    public Holder(ItemImageBinding binding, OnGalleryClickHelper onGalleryClickHelper) {
      super(binding.getRoot());
      this.binding = binding;
      this.onGalleryClickHelper = onGalleryClickHelper;
      binding.getRoot()
          .setOnClickListener(this);
    }

    private void bind(int position) {
      gallery = galleryList.get(position);
      binding.thumbnailTitle.setText(gallery.getTitle());
      binding.getRoot()
          .setOnClickListener(this);
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


}
