package code.haotran.testing.data;

import code.haotran.testing.data.local.UsersLocalDataSource;
import code.haotran.testing.data.local.model.UserDetails;
import code.haotran.testing.data.local.model.RepoUsersResult;
import code.haotran.testing.data.local.model.RepoReputationsResult;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.data.remote.UsersRemoteDataSource;
import code.haotran.testing.data.remote.api.ApiResponse;
import code.haotran.testing.utils.AppExecutors;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import timber.log.Timber;

/**
 * Repository implementation that returns a paginated data and loads data directly from network.
 *
 * @author Hao Tran
 */
public class UserRepository implements DataSource {

    private static volatile UserRepository sInstance;

    private final UsersLocalDataSource mLocalDataSource;

    private final UsersRemoteDataSource mRemoteDataSource;

    private final AppExecutors mExecutors;

    private UserRepository(UsersLocalDataSource localDataSource,
                           UsersRemoteDataSource remoteDataSource,
                           AppExecutors executors) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mExecutors = executors;
    }

    public static UserRepository getInstance(UsersLocalDataSource localDataSource,
                                             UsersRemoteDataSource remoteDataSource,
                                             AppExecutors executors) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(localDataSource, remoteDataSource, executors);
                }
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<Resource<UserDetails>> loadUser(final long userId) {
        return new NetworkBoundResource<UserDetails, User>(mExecutors) {

            @Override
            protected void saveCallResult(@NonNull User item) {
                mLocalDataSource.saveUser(item);
                Timber.d("User added to database");
            }

            @Override
            protected boolean shouldFetch(@Nullable UserDetails data) {
                return data == null; // only fetch fresh data if it doesn't exist in database
            }

            @NonNull
            @Override
            protected LiveData<UserDetails> loadFromDb() {
                Timber.d("Loading user from database");
                return mLocalDataSource.getUser(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                Timber.d("Downloading user from network");
                return mRemoteDataSource.loadUser(userId);
            }

            @NonNull
            @Override
            protected void onFetchFailed() {
                // ignored
                Timber.d("Fetch failed!!");
            }
        }.getAsLiveData();
    }

    @Override
    public RepoUsersResult loadUsers() {
        return mRemoteDataSource.loadUsers();
    }

    @Override
    public RepoReputationsResult loadReputations(long userId) {
        return mRemoteDataSource.loadReputations(userId);
    }

    @Override
    public LiveData<List<User>> getAllBookmarkedUsers() {
        return mLocalDataSource.getAllBookmarkedUsers();
    }

    @Override
    public void bookmarkUser(final User user) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Adding user to bookmarks");
                mLocalDataSource.bookmarkUser(user);
            }
        });
    }

    @Override
    public void deBookmarkUser(final User user) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Removing user from bookmarks");
                mLocalDataSource.deBookmarkUser(user);
            }
        });
    }

    @Override
    public void saveUser(final User user) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Adding user to bookmarks");
                mLocalDataSource.saveUser(user);
            }
        });
    }
}
