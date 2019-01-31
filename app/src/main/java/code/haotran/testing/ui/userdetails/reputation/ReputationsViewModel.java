package code.haotran.testing.ui.userdetails.reputation;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import code.haotran.testing.data.UserRepository;
import code.haotran.testing.data.local.model.RepoReputationsResult;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.data.local.model.Resource;

/**
 * @author Hao Tran
 */
public class ReputationsViewModel extends ViewModel {

    private LiveData<RepoReputationsResult> repoUsersResult;

    private LiveData<PagedList<Reputation>> pagedList;

    private LiveData<Resource> networkState;

    private final UserRepository repository;

//    private MutableLiveData<UsersFilterType> sortBy = new MutableLiveData<>();

    public void init(long userId) {
        if (repoUsersResult != null) {
            return; // load  only once the activity created first time
        }

        MediatorLiveData<RepoReputationsResult> _liveData = new MediatorLiveData<>();
        _liveData.setValue(repository.loadReputations(userId));
        repoUsersResult = _liveData;

        pagedList = Transformations.switchMap(repoUsersResult,
                new Function<RepoReputationsResult, LiveData<PagedList<Reputation>>>() {
                    @Override
                    public LiveData<PagedList<Reputation>> apply(RepoReputationsResult input) {
                        return input.data;
                    }
                });

        networkState = Transformations.switchMap(repoUsersResult, new Function<RepoReputationsResult, LiveData<Resource>>() {
            @Override
            public LiveData<Resource> apply(RepoReputationsResult input) {
                return input.resource;
            }
        });

    }

    public ReputationsViewModel(final UserRepository repository) {
        this.repository = repository;
//        sortBy.setValue(UsersFilterType.ONLINE_USER);

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
//        MediatorLiveData<RepoReputationsResult> _liveData = new MediatorLiveData<>();
//        _liveData.setValue(userRepository.loadReputations());
//        repoUsersResult = _liveData;
//
//
//        pagedList = Transformations.switchMap(repoUsersResult,
//                new Function<RepoReputationsResult, LiveData<PagedList<Reputation>>>() {
//                    @Override
//                    public LiveData<PagedList<Reputation>> apply(RepoReputationsResult input) {
//                        return input.data;
//                    }
//                });
//
//        networkState = Transformations.switchMap(repoUsersResult, new Function<RepoReputationsResult, LiveData<Resource>>() {
//            @Override
//            public LiveData<Resource> apply(RepoReputationsResult input) {
//                return input.resource;
//            }
//        });
    }

    public LiveData<PagedList<Reputation>> getPagedList() {
        return pagedList;
    }

    public LiveData<Resource> getNetworkState() {
        return networkState;
    }


    // retry any failed requests.
    public void retry() {
        repoUsersResult.getValue().sourceLiveData.getValue().retryCallback.invoke();
    }
}