package code.haotran.testing.data.local.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReputationResponse {
    @SerializedName("items")
    private List<Reputation> reputations;

    public List<Reputation> getReputations() {
        return reputations;
    }

    public void setReputations(List<Reputation> reputation) {
        this.reputations = reputation;
    }
}
