package code.haotran.testing.data.local;

import android.content.Context;

import code.haotran.testing.data.local.dao.UsersDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import code.haotran.testing.data.local.model.User;

/**
 * The Room Database that manages a local database.
 *
 * @author Hao Tran
 */
@Database(
        entities = {User.class},
        version = 4,
        exportSchema = false)
public abstract class UsersDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "Users.db";

    private static UsersDatabase INSTANCE;

    private static final Object sLock = new Object();

    public abstract UsersDao usersDao();

    public static UsersDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context);
            }
            return INSTANCE;
        }
    }

    private static UsersDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                UsersDatabase.class,
                DATABASE_NAME).build();
    }
}
