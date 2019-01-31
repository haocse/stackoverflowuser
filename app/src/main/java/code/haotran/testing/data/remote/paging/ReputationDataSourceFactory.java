package code.haotran.testing.data.remote.paging;

import java.util.concurrent.Executor;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.data.remote.api.UserService;

/**
 * A simple data source factory provides a way to observe the last created data source.
 *
 * @author Hao Tran
 */
public class ReputationDataSourceFactory extends DataSource.Factory<Integer, Reputation> {

    public MutableLiveData<ReputationPageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    private final UserService userService;
    private final Executor networkExecutor;
    private final long userId;

    public ReputationDataSourceFactory(UserService userService,
                                       Executor networkExecutor, long userId) {
        this.userService = userService;
        this.networkExecutor = networkExecutor;
        this.userId = userId;
    }

    @Override
    public DataSource<Integer, Reputation> create() {
        ReputationPageKeyedDataSource userDataSource =
                new ReputationPageKeyedDataSource(userService, networkExecutor, userId);
        sourceLiveData.postValue(userDataSource);
        return userDataSource;
    }
}
