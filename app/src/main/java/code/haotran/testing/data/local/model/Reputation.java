package code.haotran.testing.data.local.model;

import com.google.gson.annotations.SerializedName;

public class Reputation {
    @SerializedName("post_id")
    private long id;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("reputation_history_type")
    private String reputationType;

    @SerializedName("creation_date")
    private String creationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getReputationType() {
        return reputationType;
    }

    public void setReputationType(String reputationType) {
        this.reputationType = reputationType;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
