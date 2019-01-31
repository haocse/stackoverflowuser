package code.haotran.testing.data.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Hao Tran
 */
public class UsersResponse {

    @SerializedName("items")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
