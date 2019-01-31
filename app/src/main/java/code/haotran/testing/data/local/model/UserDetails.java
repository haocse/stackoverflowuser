package code.haotran.testing.data.local.model;


import androidx.room.Embedded;

/**
 *
 * @author Hao Tran
 *
 */
public class UserDetails {

    @Embedded
    public User user = null;
}
