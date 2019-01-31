package code.haotran.testing.ui.userslist.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import code.haotran.testing.R;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.utils.Injection;
import code.haotran.testing.utils.ItemOffsetDecoration;
import code.haotran.testing.utils.ViewModelFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * @author Hao Tran
 */
public class DiscoverUsersFragment extends Fragment {

    private DiscoverUsersViewModel viewModel;

    public static DiscoverUsersFragment newInstance() {
        return new DiscoverUsersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_discover_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = obtainViewModel(getActivity());

        setupListAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static DiscoverUsersViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(DiscoverUsersViewModel.class);
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_user_list);
        final DiscoverUsersAdapter discoverUsersAdapter =
                new DiscoverUsersAdapter(viewModel);
        final GridLayoutManager layoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.span_count));

        // draw network status and errors messages to fit the whole row(3 spans)
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (discoverUsersAdapter.getItemViewType(position)) {
                    case R.layout.item_network_state:
                        return layoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });

        // setup recyclerView
        recyclerView.setAdapter(discoverUsersAdapter);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        // observe paged list
        viewModel.getPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<User>>() {
            @Override
            public void onChanged(PagedList<User> users) {
                Timber.d("On Changed!");
                discoverUsersAdapter.submitList(users);
            }
        });

        // observe network state
        viewModel.getNetworkState().observe(getViewLifecycleOwner(), new Observer<Resource>() {
            @Override
            public void onChanged(Resource resource) {
                discoverUsersAdapter.setNetworkState(resource);
            }
        });
    }
}
