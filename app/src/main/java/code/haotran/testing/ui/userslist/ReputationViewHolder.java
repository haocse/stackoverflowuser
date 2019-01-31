package code.haotran.testing.ui.userslist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import code.haotran.testing.data.local.model.Reputation;
import code.haotran.testing.databinding.ItemReputationBinding;

/**
 * @author Hao Tran
 */
public class ReputationViewHolder extends RecyclerView.ViewHolder {

    private final ItemReputationBinding binding;

    public ReputationViewHolder(@NonNull ItemReputationBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bindTo(final Reputation reputation) {
        binding.setReputation(reputation);
        // user click event
//        binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
//                intent.putExtra(Constants.EXTRA_USER_ID, reputation.getId());
//                view.getContext().startActivity(intent);
//            }
//        });

        binding.executePendingBindings();
    }

    public static ReputationViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemReputationBinding binding =
                ItemReputationBinding.inflate(layoutInflater, parent, false);
        return new ReputationViewHolder(binding);
    }
}
