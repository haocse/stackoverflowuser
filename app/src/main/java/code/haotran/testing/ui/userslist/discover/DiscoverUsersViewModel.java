package code.haotran.testing.ui.userslist.discover;

import androidx.lifecycle.MediatorLiveData;
import code.haotran.testing.data.UserRepository;
import code.haotran.testing.data.local.model.RepoUsersResult;
import code.haotran.testing.data.local.model.Resource;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.ui.userslist.UsersFilterType;

/**
 * @author Hao Tran
 */
public class DiscoverUsersViewModel extends ViewModel {

    private LiveData<RepoUsersResult> repoUsersResult;

    private LiveData<PagedList<User>> pagedList;

    private LiveData<Resource> networkState;


    private MutableLiveData<UsersFilterType> sortBy = new MutableLiveData<>();
    private UserRepository repository;

    public DiscoverUsersViewModel(final UserRepository userRepository) {
        this.repository = userRepository;

        sortBy.setValue(UsersFilterType.ONLINE_USER);

        /*
          (1)
         */
//        repoUsersResult = Transformations.map(sortBy, new Function<UsersFilterType, RepoUsersResult>() { // for switching between different modes.
//            @Override
//            public RepoUsersResult apply(UsersFilterType sort) {
//                return userRepository.loadUsersFilteredBy(sort);
//            }
//        });

        /*
         * (2)
         */
        MediatorLiveData<RepoUsersResult> _liveData = new MediatorLiveData<>();
        _liveData.setValue(userRepository.loadUsers());
        repoUsersResult = _liveData;


        pagedList = Transformations.switchMap(repoUsersResult,
                new Function<RepoUsersResult, LiveData<PagedList<User>>>() {
                    @Override
                    public LiveData<PagedList<User>> apply(RepoUsersResult input) {
                        return input.data;
                    }
                });

        networkState = Transformations.switchMap(repoUsersResult, new Function<RepoUsersResult, LiveData<Resource>>() {
            @Override
            public LiveData<Resource> apply(RepoUsersResult input) {
                return input.resource;
            }
        });
    }

    public LiveData<PagedList<User>> getPagedList() {
        return pagedList;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }


    // retry any failed requests.
    public void retry() {
        repoUsersResult.getValue().sourceLiveData.getValue().retryCallback.invoke();
    }

    public void saveUser(User user) {
        repository.saveUser(user);
    }
}