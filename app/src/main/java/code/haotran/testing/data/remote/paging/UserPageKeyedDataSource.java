package code.haotran.testing.data.remote.paging;

import code.haotran.testing.data.local.model.UsersResponse;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.data.remote.api.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import code.haotran.testing.ui.userslist.UsersFilterType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 *
 * @author Hao Tran
 */
public class UserPageKeyedDataSource extends PageKeyedDataSource<Integer, User> {

    private static final int FIRST_PAGE = 1;

    public MutableLiveData<Resource> networkState = new MutableLiveData<>();

    private final UserService userService;

    private final Executor networkExecutor;


    public RetryCallback retryCallback = null;

    public UserPageKeyedDataSource(UserService userService,
                                   Executor networkExecutor, UsersFilterType sortBy) {
        this.userService = userService;
        this.networkExecutor = networkExecutor;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, User> callback) {
        networkState.postValue(Resource.loading(null));

        // load data from API
        Call<UsersResponse> request = userService.getUsers(FIRST_PAGE);

        // we execute sync since this is triggered by refresh
        try {
            Response<UsersResponse> response = request.execute();
            UsersResponse data = response.body();
            List<User> userList =
                    data != null ? data.getUsers() : Collections.<User>emptyList();

            retryCallback = null;
            networkState.postValue(Resource.success(null));
            callback.onResult(userList, null, FIRST_PAGE + 1);
        } catch (IOException e) {
            // publish error
            retryCallback = new RetryCallback() {
                @Override
                public void invoke() {
                    networkExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadInitial(params, callback);
                        }
                    });

                }
            };
            networkState.postValue(Resource.error(e.getMessage(), null));
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, User> callback) {
        // ignored, since we only ever append to our initial load
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, User> callback) {
        networkState.postValue(Resource.loading(null));

        // load data from API
        Call<UsersResponse> request = userService.getUsers(params.key);

        request.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    UsersResponse data = response.body();
                    List<User> userList =
                            data != null ? data.getUsers() : Collections.<User>emptyList();

                    retryCallback = null;
                    callback.onResult(userList, params.key + 1);
                    networkState.postValue(Resource.success(null));
                } else {
                    retryCallback = new RetryCallback() {
                        @Override
                        public void invoke() {
                            loadAfter(params, callback);
                        }
                    };
                    networkState.postValue(Resource.error("error code: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                retryCallback = new RetryCallback() {
                    @Override
                    public void invoke() {
                        networkExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                loadAfter(params, callback);
                            }
                        });
                    }
                };
                networkState.postValue(Resource.error(t != null ? t.getMessage() : "unknown error", null));
            }
        });
    }

    public interface RetryCallback {
        void invoke();
    }

}
