package code.haotran.testing.data.remote;

import code.haotran.testing.data.local.model.RepoUsersResult;
import code.haotran.testing.data.local.model.RepoReputationsResult;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.data.remote.api.ApiResponse;
import code.haotran.testing.data.remote.api.UserService;
import code.haotran.testing.data.remote.paging.UserDataSourceFactory;
import code.haotran.testing.data.remote.paging.UserPageKeyedDataSource;
import code.haotran.testing.data.remote.paging.ReputationDataSourceFactory;
import code.haotran.testing.data.remote.paging.ReputationPageKeyedDataSource;
import code.haotran.testing.ui.userslist.UsersFilterType;
import code.haotran.testing.utils.AppExecutors;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * @author Hao Tran
 */
public class UsersRemoteDataSource {

    private static final int PAGE_SIZE = 20;

    private final AppExecutors mExecutors;

    private static volatile UsersRemoteDataSource sInstance;

    private final UserService mUserService;

    private UsersRemoteDataSource(UserService userService,
                                  AppExecutors executors) {
        mUserService = userService;
        mExecutors = executors;
    }

    public static UsersRemoteDataSource getInstance(UserService userService,
                                                    AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new UsersRemoteDataSource(userService, executors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ApiResponse<User>> loadUser(final long userId) {
        return mUserService.getUserDetails(userId);
    }

    public RepoUsersResult loadUsersFilteredBy(UsersFilterType sortBy) {
        UserDataSourceFactory sourceFactory =
                new UserDataSourceFactory(mUserService, mExecutors.networkIO(), sortBy);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<User>> usersPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        LiveData<Resource> networkState = Transformations.switchMap(sourceFactory.sourceLiveData, new Function<UserPageKeyedDataSource, LiveData<Resource>>() {
            @Override
            public LiveData<Resource> apply(UserPageKeyedDataSource input) {
                return input.networkState;
            }
        });

        // Get pagedList and network errors exposed to the viewmodel
        return new RepoUsersResult(
                usersPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }

    public RepoUsersResult loadUsers() {
        UserDataSourceFactory sourceFactory =
                new UserDataSourceFactory(mUserService, mExecutors.networkIO());

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<User>> usersPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        LiveData<Resource> networkState = Transformations.switchMap(sourceFactory.sourceLiveData, new Function<UserPageKeyedDataSource, LiveData<Resource>>() {
            @Override
            public LiveData<Resource> apply(UserPageKeyedDataSource input) {
                return input.networkState;
            }
        });

        // Get pagedList and network errors exposed to the viewmodel
        return new RepoUsersResult(
                usersPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }

    public RepoReputationsResult loadReputations(long userId) {
        ReputationDataSourceFactory sourceFactory =
                new ReputationDataSourceFactory(mUserService, mExecutors.networkIO(), userId);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<Reputation>> usersPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        LiveData<Resource> networkState = Transformations.switchMap(sourceFactory.sourceLiveData, new Function<ReputationPageKeyedDataSource, LiveData<Resource>>() {
            @Override
            public LiveData<Resource> apply(ReputationPageKeyedDataSource input) {
                return input.networkState;
            }
        });

        // Get pagedList and network errors exposed to the viewmodel
        return new RepoReputationsResult(
                usersPagedList,
                networkState,
                sourceFactory.sourceLiveData
        );
    }
}
