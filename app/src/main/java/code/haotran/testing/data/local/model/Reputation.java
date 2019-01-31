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
    private long creationDate;

    @SerializedName("reputation_change")
    private String reputationChange;

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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getReputationChange() {
        return reputationChange;
    }

    public void setReputationChange(String reputationChange) {
        this.reputationChange = reputationChange;
    }
}
