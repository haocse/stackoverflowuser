package code.haotran.testing.utils;

import android.content.Context;

import code.haotran.testing.data.UserRepository;
import code.haotran.testing.data.local.UsersDatabase;
import code.haotran.testing.data.local.UsersLocalDataSource;
import code.haotran.testing.data.remote.UsersRemoteDataSource;
import code.haotran.testing.data.remote.api.ApiClient;
import code.haotran.testing.data.remote.api.UserService;

/**
 * Class that handles object creation.
 *
 * @author Hao Tran
 */
public class Injection {

    /**
     * Creates an instance of UsersRemoteDataSource
     */
    public static UsersRemoteDataSource provideUsersRemoteDataSource() {
        UserService apiService = ApiClient.getInstance();
        AppExecutors executors = AppExecutors.getInstance();
        return UsersRemoteDataSource.getInstance(apiService, executors);
    }

    /**
     * Creates an instance of UsersRemoteDataSource
     */
    public static UsersLocalDataSource provideUsersLocalDataSource(Context context) {
        UsersDatabase database = UsersDatabase.getInstance(context.getApplicationContext());
        return UsersLocalDataSource.getInstance(database);
    }

    /**
     * Creates an instance of UserRepository
     */
    public static UserRepository provideUserRepository(Context context) {
        UsersRemoteDataSource remoteDataSource = provideUsersRemoteDataSource();
        UsersLocalDataSource localDataSource = provideUsersLocalDataSource(context);
        AppExecutors executors = AppExecutors.getInstance();
        return UserRepository.getInstance(
                localDataSource,
                remoteDataSource,
                executors);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserRepository repository = provideUserRepository(context); // given context is really important.
        return ViewModelFactory.getInstance(repository);
    }
}
