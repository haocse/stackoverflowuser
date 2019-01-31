package code.haotran.testing.data.local.model;

import code.haotran.testing.data.remote.paging.UserPageKeyedDataSource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

/**
 * @author Hao Tran
 */
public class RepoUsersResult {
    public LiveData<PagedList<User>> data;
    public LiveData<Resource> resource;
    public MutableLiveData<UserPageKeyedDataSource> sourceLiveData;

    public RepoUsersResult(LiveData<PagedList<User>> data,
                           LiveData<Resource> resource,
                           MutableLiveData<UserPageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.resource = resource;
        this.sourceLiveData = sourceLiveData;
    }
}
