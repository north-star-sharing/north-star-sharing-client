package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.northstarsharingclient.model.Image;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import edu.cnm.deepdive.northstarsharingclient.service.repository.ImageRepository;
import edu.cnm.deepdive.northstarsharingclient.service.repository.UserRepository;
import io.reactivex.disposables.CompositeDisposable;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class ImageViewModel extends AndroidViewModel implements LifecycleObserver {

  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<User> user;
  private final MutableLiveData<Image> image;
  private final MutableLiveData<List<Image>> imageList;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pendingTask;


  public ImageViewModel(
      @NonNull Application application) {
    super(application);
    userRepository = new UserRepository(application);
    imageRepository = new ImageRepository(application);
    account = new MutableLiveData<>(userRepository.getAccount());
    user = new MutableLiveData<>();
    image = new MutableLiveData<>();
    imageList = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pendingTask = new CompositeDisposable();
//    loadImages();
  }

  public LiveData<User> getUser() {
    return user;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Image> getImage() {
    return image;
  }

  public LiveData<List<Image>> getImageList() {
    return imageList;
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pendingTask.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

  public void store(Uri uri, File file, String title, String description, UUID galleryId) {
    throwable.postValue(null);
    pendingTask.add(
        imageRepository
            .add(uri, file,title, description, galleryId)
            .subscribe(
                (image) -> loadImages(), // TODO explore updating list in place without refreshing.
                this::postThrowable
            )
    );
  }

  public void loadImages() {
    throwable.postValue(null);
    pendingTask.add(
        imageRepository.getAll()
                       .subscribe(
                           imageList::postValue,
                           throwable::postValue
                       )
    );
  }
}
