package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import android.app.Application;
import android.hardware.SensorManager;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.northstarsharingclient.model.Gallery;
import edu.cnm.deepdive.northstarsharingclient.service.repository.GalleryRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import java.util.UUID;

/**
 * A {@link androidx.lifecycle.ViewModel} to hold the {@link LiveData} for the {@link Gallery}
 * information.
 */
public class GalleryViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<Gallery> gallery;
  private final MutableLiveData<List<Gallery>> galleryList;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private final GalleryRepository galleryRepository;

  /**
   * Create an instance of the {@link GalleryViewModel}. An {@link Application} Context may be required
   * for functionality of other classes that are used.
   *
   * @param application The context in which the GalleryViewModel is being used.
   */
  public GalleryViewModel(
      @NonNull Application application) {
    super(application);
    gallery = new MutableLiveData<>();
    galleryList = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    galleryRepository = new GalleryRepository(application);
    loadGalleryList();
  }

  /**
   * Return a {@link Gallery} from {@link LiveData} that is being managed by the view model.
   *
   * @return {@link LiveData&lt;Gallery&gt;}
   */
  public LiveData<Gallery> getGallery() {
    return gallery;
  }

  /**
   * Return the {@link List} of all {@link Gallery Galleries} from {@link LiveData} that is being
   * managed by the view model.
   *
   * @return {@link LiveData&lt;List&lt;Gallery&gt;&gt;}
   */
  public LiveData<List<Gallery>> getGalleryList() {
    return galleryList;
  }

  /**
   * Return a {@link Throwable} error from {@link LiveData} that is being managed by the view model.
   *
   * @return {@link LiveData&lt;Throwable&gt;}
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Get a specific {@link Gallery} by looking up it's {@link UUID} identifier}
   *
   * @param id The unique {@link UUID} identifier of a gallery.
   */
  public void getGallery(UUID id) {
    throwable.postValue(null);
    pending.add(
        galleryRepository
            .getGallery(id)
            .subscribe(
                gallery::postValue,
                throwable::postValue
            )
    );
  }

  /**
   * Acquire the list of all {@link Gallery Galleries} from the repository.
   */
  public void loadGalleryList() {
    throwable.postValue(null);
    pending.add(
        galleryRepository.getGalleryList()
                         .subscribe(
                             galleryList::postValue,
                             throwable::postValue
                         )
    );
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }
}
