package code.haotran.testing.data.remote.api;

import code.haotran.testing.data.local.model.UsersResponse;

import androidx.lifecycle.LiveData;
import code.haotran.testing.data.local.model.ReputationResponse;
import code.haotran.testing.data.local.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST API access points.
 * <p>
 * @author Hao Tran
 */
public interface UserService {

    @GET("users?pagesize=20&site=stackoverflow")
    Call<UsersResponse> getUsers(@Query("page") int page);

    @GET("users/{id}/reputation-history?pagesize=20&site=stackoverflow")
    Call<ReputationResponse> getReputations(@Path("id") long userId, @Query("page") int page);

    @GET("users/{id}?site=stackoverflow") // more information than user/{id}/videos
    LiveData<ApiResponse<User>> getUserDetails(@Path("id") long userId);
}
