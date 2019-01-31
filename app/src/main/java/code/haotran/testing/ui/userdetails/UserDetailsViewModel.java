package code.haotran.testing.ui.userdetails;

import code.haotran.testing.R;
import code.haotran.testing.data.UserRepository;
import code.haotran.testing.data.local.model.UserDetails;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.utils.SnackbarMessage;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

/**
 * @author Hao Tran
 */
public class UserDetailsViewModel extends ViewModel {

    private final UserRepository repository;

    private LiveData<Resource<UserDetails>> result;

    private MutableLiveData<Long> userIdLiveData = new MutableLiveData<>();

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private boolean isBookmarked;

    public UserDetailsViewModel(final UserRepository repository) {
        this.repository = repository;
    }

    public void init(long userId) {
        if (result != null) {
            return; // load user details only once the activity created first time
        }
        Timber.d("Initializing viewModel");

        result = Transformations.switchMap(userIdLiveData,
                new Function<Long, LiveData<Resource<UserDetails>>>() {
                    @Override
                    public LiveData<Resource<UserDetails>> apply(Long userId) {
                        return repository.loadUser(userId);
                    }
                });

        setUserIdLiveData(userId); // trigger loading user
    }


    public LiveData<Resource<UserDetails>> getResult() {
        return result;
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void bookmark(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    private void setUserIdLiveData(long userId) {
        userIdLiveData.setValue(userId);
    }

    public void retry(long userId) {
        setUserIdLiveData(userId);
    }

    public void onBookmarkClicked() {
        UserDetails userDetails = result.getValue().data;
        if (!isBookmarked) {
            repository.bookmarkUser(userDetails.user);
            isBookmarked = true;
            showSnackbarMessage(R.string.user_added_successfully);
        } else {
            repository.deBookmarkUser(userDetails.user);
            isBookmarked = false;
            showSnackbarMessage(R.string.user_removed_successfully);
        }
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }
}
