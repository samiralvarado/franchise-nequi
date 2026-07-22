package co.com.bancolombia.model.branch.values;

import lombok.Value;

@Value
public class BranchName {
    String value;

    private BranchName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Branch name cannot be null or empty");
        }
        if (value.length() < 2) {
            throw new IllegalArgumentException("Branch name must have at least 2 characters");
        }
        this.value = value.trim();
    }

    public static BranchName of(String value) {
        return new BranchName(value);
    }
}
