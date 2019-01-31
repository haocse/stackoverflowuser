package code.haotran.testing.data.remote.paging;

import code.haotran.testing.data.local.model.User;
import code.haotran.testing.data.remote.api.UserService;

import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import code.haotran.testing.ui.userslist.UsersFilterType;

/**
 * A simple data source factory provides a way to observe the last created data source.
 *
 * @author Hao Tran
 */
public class UserDataSourceFactory extends DataSource.Factory<Integer, User> {

    public MutableLiveData<UserPageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final UserService userService;
    private final Executor networkExecutor;
    private final UsersFilterType sortBy;

    public UserDataSourceFactory(UserService userService,
                                 Executor networkExecutor, UsersFilterType sortBy) {
        this.userService = userService;
        this.networkExecutor = networkExecutor;
        this.sortBy = sortBy;
    }

    public UserDataSourceFactory(UserService userService,
                                 Executor networkExecutor) {
        this.userService = userService;
        this.networkExecutor = networkExecutor;
        this.sortBy = UsersFilterType.ONLINE_USER;
    }

    @Override
    public DataSource<Integer, User> create() {
        UserPageKeyedDataSource userDataSource =
                new UserPageKeyedDataSource(userService, networkExecutor, sortBy);
        sourceLiveData.postValue(userDataSource);
        return userDataSource;
    }
}
