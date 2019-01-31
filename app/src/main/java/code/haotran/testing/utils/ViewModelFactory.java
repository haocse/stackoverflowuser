package code.haotran.testing.utils;

import code.haotran.testing.data.UserRepository;
import code.haotran.testing.ui.userdetails.UserDetailsViewModel;
import code.haotran.testing.ui.userslist.discover.DiscoverUsersViewModel;
import code.haotran.testing.ui.userslist.bookmark.BookmarksViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import code.haotran.testing.ui.userdetails.reputation.ReputationsViewModel;

/**
 * @author Hao Tran
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final UserRepository repository;

    public static ViewModelFactory getInstance(UserRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(UserRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DiscoverUsersViewModel.class)) {
            //noinspection unchecked
            return (T) new DiscoverUsersViewModel(repository);
        } else if (modelClass.isAssignableFrom(ReputationsViewModel.class)) {
            //noinspection unchecked
            return (T) new ReputationsViewModel(repository);
        } else if (modelClass.isAssignableFrom(BookmarksViewModel.class)) {
            //noinspection unchecked
            return (T) new BookmarksViewModel(repository);
        } else if (modelClass.isAssignableFrom(UserDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new UserDetailsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
