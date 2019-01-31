package code.haotran.testing.data.local.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import code.haotran.testing.data.remote.paging.ReputationPageKeyedDataSource;

/**
 * @author Hao Tran
 */
public class RepoReputationsResult {
    public LiveData<PagedList<Reputation>> data;
    public LiveData<Resource> resource;
    public MutableLiveData<ReputationPageKeyedDataSource> sourceLiveData;

    public RepoReputationsResult(LiveData<PagedList<Reputation>> data,
                                 LiveData<Resource> resource,
                                 MutableLiveData<ReputationPageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.resource = resource;
        this.sourceLiveData = sourceLiveData;
    }
}
