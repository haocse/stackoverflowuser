package code.haotran.testing.data.remote.paging;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.data.local.model.ReputationResponse;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.data.remote.api.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 *
 * @author Hao Tran
 */
public class ReputationPageKeyedDataSource extends PageKeyedDataSource<Integer, Reputation> {

    private static final int FIRST_PAGE = 1;

    public MutableLiveData<Resource> networkState = new MutableLiveData<>();

    private final UserService userService;

    private final Executor networkExecutor;


    public RetryCallback retryCallback = null;

    private long userId;

    public ReputationPageKeyedDataSource(UserService userService,
                                         Executor networkExecutor, long userId) {
        this.userService = userService;
        this.networkExecutor = networkExecutor;
        this.userId = userId;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Reputation> callback) {
        networkState.postValue(Resource.loading(null));

        // load data from API
        Call<ReputationResponse> request = userService.getReputations(userId, FIRST_PAGE);

        // we execute sync since this is triggered by refresh
        try {
            Response<ReputationResponse> response = request.execute();
            ReputationResponse data = response.body();
            List<Reputation> userList =
                    data != null ? data.getReputations() : Collections.<Reputation>emptyList();

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
                           @NonNull LoadCallback<Integer, Reputation> callback) {
        // ignored, since we only ever append to our initial load
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, Reputation> callback) {
        networkState.postValue(Resource.loading(null));

        // load data from API
        Call<ReputationResponse> request = userService.getReputations(userId, params.key);

        request.enqueue(new Callback<ReputationResponse>() {
            @Override
            public void onResponse(Call<ReputationResponse> call, Response<ReputationResponse> response) {
                if (response.isSuccessful()) {
                    ReputationResponse data = response.body();

                    List<Reputation> userList =
                            data != null ? data.getReputations() : Collections.<Reputation>emptyList();

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
            public void onFailure(Call<ReputationResponse> call, Throwable t) {
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
