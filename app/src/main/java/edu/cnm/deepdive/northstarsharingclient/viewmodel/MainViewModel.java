package edu.cnm.deepdive.northstarsharingclient.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.northstarsharingclient.model.User;
import edu.cnm.deepdive.northstarsharingclient.service.repository.UserRepository;
import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<User> user;
  private final UserRepository userRepository;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pendingTask;


  public MainViewModel(
      @NonNull Application application) {
    super(application);
    user = new MutableLiveData<>();
    userRepository = new UserRepository(application);
    account = new MutableLiveData<>(userRepository.getAccount());
    throwable = new MutableLiveData<>();
    pendingTask = new CompositeDisposable();

  }

  public LiveData<User> getUser() {
    return user;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pendingTask.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }

}
