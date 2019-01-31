package code.haotran.testing.ui.userslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import code.haotran.testing.data.local.model.User;
import code.haotran.testing.databinding.ItemUserBinding;
import code.haotran.testing.ui.userdetails.DetailsActivity;
import code.haotran.testing.utils.Constants;

/**
 * @author Hao Tran
 */
public class BookmarkViewHolder extends RecyclerView.ViewHolder {

    private final ItemUserBinding binding;

    public BookmarkViewHolder(@NonNull ItemUserBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bindTo(final User user) {
        binding.setUser(user);
        // user click event
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra(Constants.EXTRA_USER_ID, user.getId());
                view.getContext().startActivity(intent);
            }
        });

        binding.executePendingBindings();
    }

    public static BookmarkViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemUserBinding binding =
                ItemUserBinding.inflate(layoutInflater, parent, false);
        return new BookmarkViewHolder(binding);
    }
}
