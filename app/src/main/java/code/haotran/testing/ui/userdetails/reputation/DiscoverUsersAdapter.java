package code.haotran.testing.ui.userdetails.reputation;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import code.haotran.testing.R;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.data.local.model.Resource;
import code.haotran.testing.ui.userslist.ReputationViewHolder;

/**
 * Adapter implementation
 *
 * @author Hao Tran
 */
public class DiscoverUsersAdapter extends PagedListAdapter<Reputation, RecyclerView.ViewHolder> {

    private ReputationsViewModel mViewModel;

    private Resource resource = null;

    DiscoverUsersAdapter(ReputationsViewModel viewModel) {
        super(USER_COMPARATOR);

        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_user:
                return ReputationViewHolder.create(parent);
            case R.layout.item_network_state:
                return NetworkStateViewHolder.create(parent, mViewModel);
            default:
                throw new IllegalArgumentException("unknown view type " + viewType);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_user:
                ((ReputationViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.item_network_state:
                ((NetworkStateViewHolder) holder).bindTo(resource);
                break;
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.item_network_state;
        } else {
            return R.layout.item_user;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    private boolean hasExtraRow() {
        return resource != null && resource.status != Resource.Status.SUCCESS;
    }

    public void setNetworkState(Resource resource) {
        Resource previousState = this.resource;
        boolean hadExtraRow = hasExtraRow();
        this.resource = resource;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && !previousState.equals(resource)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private static DiffUtil.ItemCallback<Reputation> USER_COMPARATOR = new DiffUtil.ItemCallback<Reputation>() {
        @Override
        public boolean areItemsTheSame(@NonNull Reputation oldItem, @NonNull Reputation newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reputation oldItem, @NonNull Reputation newItem) {
            return oldItem.equals(newItem);
        }
    };
}
