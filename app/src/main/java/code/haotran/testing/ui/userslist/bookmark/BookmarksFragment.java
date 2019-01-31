package code.haotran.testing.ui.userslist.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import code.haotran.testing.R;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.databinding.FragmentBookmarksUsersBinding;
import code.haotran.testing.ui.userslist.UsersActivity;
import code.haotran.testing.utils.Injection;
import code.haotran.testing.utils.ItemOffsetDecoration;
import code.haotran.testing.utils.ViewModelFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Hao Tran
 */
public class BookmarksFragment extends Fragment {

    private BookmarksViewModel viewModel;
    private FragmentBookmarksUsersBinding binding;

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((UsersActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.discover));

        viewModel = obtainViewModel(getActivity());
        setupListAdapter();
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = binding.usersList.rvUserList;
        final BookmarksAdapter bookmarksAdapter = new BookmarksAdapter();
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        // setup recyclerView
        recyclerView.setAdapter(bookmarksAdapter);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        // observe bookmarks list
        viewModel.getBookmarkedListLiveData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                if (userList.isEmpty()) {
                    // display empty state since there is no bookmarks in database
                    binding.usersList.rvUserList.setVisibility(View.GONE);
                    binding.emptyState.setVisibility(View.VISIBLE);
                } else {
                    binding.usersList.rvUserList.setVisibility(View.VISIBLE);
                    binding.emptyState.setVisibility(View.GONE);
                    bookmarksAdapter.submitList(userList);
                }
            }
        });
    }

    private BookmarksViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(BookmarksViewModel.class);
    }
}
