package edu.cnm.deepdive.northstarsharingclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.northstarsharingclient.BuildConfig;
import edu.cnm.deepdive.northstarsharingclient.adapter.ImageAdapter.Holder;
import edu.cnm.deepdive.northstarsharingclient.databinding.ItemImageBinding;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} to display a scrolling list of {@link Image Images}.
 */
public class ImageAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final LayoutInflater inflater;
  private final List<Image> imageList;
  private final OnImageClickHelper onImageClickHelper;

  /**
   * Create an instance of a {@link ImageAdapter} to manage the scrolling list of {@link Image
   * Images}.
   *
   * @param context            The application {@link Context} in which the instance is being used.
   * @param imageList          The {@link List} of {@link Image Images} displayed by the {@link
   *                           RecyclerView}
   * @param onImageClickHelper An implementation of the {@link OnImageClickHelper} to handle {@link
   *                           OnClickListener} events
   */
  public ImageAdapter(Context context, List<Image> imageList,
      OnImageClickHelper onImageClickHelper) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
    this.imageList = imageList;
    this.onImageClickHelper = onImageClickHelper;
  }

  @NonNull
  @Override
  public ImageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemImageBinding binding = ItemImageBinding.inflate(inflater, parent, false);
    return new Holder(binding, onImageClickHelper);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  /**
   * An interface that handles {@link OnClickListener} events for {@link Image} items.
   */
  public interface OnImageClickHelper {

    /**
     * The action to perform when long-clicking a {@link Image} in the {@link RecyclerView}.
     *
     * @param image    The {@link Image} entity that is being long-clicked
     * @param position The index position of the {@link Image} in the list
     */
    void onImageLongClick(Image image, int position);

    /**
     * The action to perform when clicking a {@link Image} in the {@link RecyclerView}.
     *
     * @param image    The {@link Image} entity that is being clicked
     * @param position The index position of the {@link Image} in the list
     */
    void onImageClick(Image image, int position);
  }

  /**
   * A nested class of {@link ImageAdapter} to manage the behavior of an individual {@link Image} in
   * the {@link RecyclerView}.
   */
  public class Holder extends RecyclerView.ViewHolder implements OnLongClickListener,
      OnClickListener {

    private final ItemImageBinding binding;
    private final OnImageClickHelper onImageClickHelper;
    private Image image;

    /**
     * Create an instance of a {@link ImageAdapter.Holder}
     *
     * @param binding            The binding to the layout that will be used for displaying a single
     *                           {@link Image} element
     * @param onImageClickHelper The {@link OnImageClickHelper} that will handle click actions.
     */
    public Holder(ItemImageBinding binding, OnImageClickHelper onImageClickHelper) {
      super(binding.getRoot());
      this.binding = binding;
      this.onImageClickHelper = onImageClickHelper;
      binding.getRoot()
          .setOnLongClickListener(this::onLongClick);
      binding.getRoot()
          .setOnClickListener(this::onClick);
    }

    private void bind(int position) {
      image = imageList.get(position);
      if (image.getHref() != null) {
        Picasso
            .get()
            .load(String.format(BuildConfig.CONTENT_FORMAT, image.getHref()))
            .into(binding.thumbnailImage);
      }
      binding.thumbnailTitle.setText(image.getTitle());
      binding.thumbnailDescription.setText(image.getDescription());
    }

    @Override
    public void onClick(View view) {
      onImageClickHelper.onImageClick(imageList.get(getAdapterPosition()), getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
      onImageClickHelper
          .onImageLongClick(imageList.get(getAdapterPosition()), getAdapterPosition());
      return true;
    }
  }

}
