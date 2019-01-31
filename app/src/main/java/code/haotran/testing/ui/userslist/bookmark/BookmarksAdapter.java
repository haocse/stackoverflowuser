package code.haotran.testing.ui.userslist.bookmark;

import android.view.ViewGroup;

import code.haotran.testing.data.local.model.User;
import code.haotran.testing.ui.userslist.BookmarkViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Hao Tran
 */
public class BookmarksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> mUsersList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BookmarkViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = mUsersList.get(position);
        ((BookmarkViewHolder) holder).bindTo(user);
    }

    @Override
    public int getItemCount() {
        return mUsersList != null ? mUsersList.size() : 0;
    }

    public void submitList(List<User> users) {
        mUsersList = users;
        notifyDataSetChanged();
    }
}
