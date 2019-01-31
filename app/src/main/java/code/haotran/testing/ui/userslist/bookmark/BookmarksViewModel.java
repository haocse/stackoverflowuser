package code.haotran.testing.ui.userslist.bookmark;

import code.haotran.testing.data.UserRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import code.haotran.testing.data.local.model.User;

/**
 * @author Hao Tran
 */
public class BookmarksViewModel extends ViewModel {

    private LiveData<List<User>> bookmarkedListLiveData;

    public BookmarksViewModel(UserRepository repository) {
        bookmarkedListLiveData = repository.getAllBookmarkedUsers();
    }

    public LiveData<List<User>> getBookmarkedListLiveData() {
        return bookmarkedListLiveData;
    }
}
