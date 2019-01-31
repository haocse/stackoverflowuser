package code.haotran.testing.data.local.dao;

import code.haotran.testing.data.local.model.UserDetails;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import code.haotran.testing.data.local.model.User;

/**
 * @author Hao Tran
 */
@Dao
public interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT * FROM user WHERE user.id= :userId")
    LiveData<UserDetails> getUser(long userId);

    @Query("SELECT * FROM user WHERE is_bookmarked = 1")
    LiveData<List<User>> getAllBookmarkedUsers();

    /**
     * Bookmark a user.
     *
     * @return the number of users updated. This should always be 1.
     */
    @Query("UPDATE user SET is_bookmarked = 1 WHERE id = :userId")
    int bookmarkUser(long userId);

    /**
     * De-bookmark a user.
     *
     * @return the number of users updated. This should always be 1.
     */
    @Query("UPDATE user SET is_bookmarked = 0 WHERE id = :userId")
    int deBookmarkUser(long userId);

}
