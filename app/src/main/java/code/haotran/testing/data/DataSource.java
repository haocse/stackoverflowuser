package code.haotran.testing.data;

import code.haotran.testing.data.local.model.RepoUsersResult;
import code.haotran.testing.data.local.model.UserDetails;
import code.haotran.testing.data.local.model.RepoReputationsResult;
import code.haotran.testing.data.local.model.Resource;

import java.util.List;

import androidx.lifecycle.LiveData;
import code.haotran.testing.data.local.model.User;

/**
 * @author Hao Tran
 */
public interface DataSource {

    LiveData<Resource<UserDetails>> loadUser(long userId);

    RepoUsersResult loadUsers();

    RepoReputationsResult loadReputations(long userId);

    LiveData<List<User>> getAllBookmarkedUsers();

    void bookmarkUser(User user);

    void deBookmarkUser(User user);

    void saveUser(User user);
}
