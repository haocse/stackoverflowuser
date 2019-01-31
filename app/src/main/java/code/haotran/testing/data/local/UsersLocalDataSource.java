package code.haotran.testing.data.local;

import code.haotran.testing.data.local.model.UserDetails;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.utils.AppExecutors;

import java.util.List;

import androidx.lifecycle.LiveData;
import timber.log.Timber;

/**
 * @author Hao Tran
 */
public class UsersLocalDataSource {

    private static volatile UsersLocalDataSource sInstance;

    private final UsersDatabase mDatabase;

    private UsersLocalDataSource(UsersDatabase database) {
        mDatabase = database;
    }

    public static UsersLocalDataSource getInstance(UsersDatabase database) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new UsersLocalDataSource(database);
                }
            }
        }
        return sInstance;
    }

    public void saveUser(User user) {
        mDatabase.usersDao().insertUser(user);
    }

    public LiveData<UserDetails> getUser(long userId) {
        Timber.d("Loading user details.");
        return mDatabase.usersDao().getUser(userId);
    }

    public LiveData<List<User>> getAllBookmarkedUsers() {
        return mDatabase.usersDao().getAllBookmarkedUsers();
    }

    public void bookmarkUser(User user) {
        mDatabase.usersDao().bookmarkUser(user.getId());
    }

    public void deBookmarkUser(User user) {
        mDatabase.usersDao().deBookmarkUser(user.getId());
    }
}
