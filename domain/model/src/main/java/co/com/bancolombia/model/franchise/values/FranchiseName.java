package co.com.bancolombia.model.franchise.values;

import lombok.Value;

@Value
public class FranchiseName {
    String value;

    private FranchiseName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Franchise name cannot be null or empty");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Franchise name must have at least 3 characters");
        }
        this.value = value.trim();
    }

    public static FranchiseName of(String value) {
        return new FranchiseName(value);
    }
}
