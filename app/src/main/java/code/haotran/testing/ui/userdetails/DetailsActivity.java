package code.haotran.testing.ui.userdetails;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import code.haotran.testing.R;
import code.haotran.testing.data.local.model.UserDetails;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.databinding.ActivityDetailsBinding;

import code.haotran.testing.ui.userdetails.reputation.DiscoverUsersFragment;
import code.haotran.testing.utils.Constants;
import code.haotran.testing.utils.Injection;
import code.haotran.testing.utils.UiUtils;
import code.haotran.testing.utils.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mBinding;

    private UserDetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        final long userId = getIntent().getLongExtra(Constants.EXTRA_USER_ID, Constants.DEFAULT_ID);

        if (userId == Constants.DEFAULT_ID) {
            closeOnError();
            return;
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mBinding.setLifecycleOwner(this);

        mViewModel = obtainViewModel();
        mViewModel.init(userId);

        setupToolbar();

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);


        // observe result
        mViewModel.getResult().observe(this, new Observer<Resource<UserDetails>>() {
            @Override
            public void onChanged(Resource<UserDetails> resource) {
                if (resource.data != null &&
                        resource.data.user != null) {
                    mViewModel.bookmark(resource.data.user.isBookMarked());
                    invalidateOptionsMenu();
                }
                mBinding.setResource(resource);
                mBinding.setUserDetails(resource.data);
            }
        });

        // handle retry event in case of network failure
        mBinding.networkState.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.retry(userId);
            }
        });

        // Observe snackbar messages
        mViewModel.getSnackbarMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer message) {
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        DetailsActivity.Adapter adapter = new DetailsActivity.Adapter(getSupportFragmentManager());
        DiscoverUsersFragment discoverUsersFragment = DiscoverUsersFragment.newInstance();
        adapter.addFragment(discoverUsersFragment, getString(R.string.users));
//        adapter.addFragment(BookmarksFragment.newInstance(), getString(R.string.bookmarks));

        viewPager.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            handleCollapsedToolbarTitle();
        }
    }


    private UserDetailsViewModel obtainViewModel() {
        ViewModelFactory factory = Injection.provideViewModelFactory(this);
        return ViewModelProviders.of(this, factory).get(UserDetailsViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_details, menu);

        MenuItem bookmarkItem = menu.findItem(R.id.action_bookmark);
        if (mViewModel.isBookmarked()) {
            bookmarkItem.setIcon(R.drawable.ic_bookmark_black_24dp)
                    .setTitle(R.string.action_remove_from_bookmarks);
        } else {
            bookmarkItem.setIcon(R.drawable.ic_bookmark_border_black_24dp)
                    .setTitle(R.string.action_bookmark);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark: {
                mViewModel.onBookmarkClicked();
                invalidateOptionsMenu();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeOnError() {
        throw new IllegalArgumentException("Access denied.");
    }

    /**
     * sets the title on the toolbar only if the toolbar is collapsed
     */
    private void handleCollapsedToolbarTitle() {
//        mBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = true;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                // verify if the toolbar is completely collapsed and set the user name as the title
//                if (scrollRange + verticalOffset == 0) {
//                    mBinding.collapsingToolbar.setTitle(
//                            mViewModel.getResult().getValue().data.user.getTitle());
//                    isShow = true;
//                } else if (isShow) {
//                    // display an empty string when toolbar is expanded
//                    mBinding.collapsingToolbar.setTitle(" ");
//                    isShow = false;
//                }
//            }
//        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
