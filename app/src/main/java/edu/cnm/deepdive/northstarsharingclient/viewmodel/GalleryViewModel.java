package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import android.app.Application;
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


  public LiveData<Gallery> getGallery() {
    return gallery;
  }

  public LiveData<List<Gallery>> getGalleryList() {
    return galleryList;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

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
