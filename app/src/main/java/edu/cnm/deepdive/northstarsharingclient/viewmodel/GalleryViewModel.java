package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GalleryViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<Gallery> gallery;
  private final MutableLiveData<List<Gallery>> galleryList;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final Gall

  public GalleryViewModel(
      @NonNull @NotNull Application application) {
    super(application);
  }
}
