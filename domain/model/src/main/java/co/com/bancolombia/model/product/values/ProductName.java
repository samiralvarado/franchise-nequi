package co.com.bancolombia.model.product.values;

import lombok.Value;

@Value
public class ProductName {
    String value;

    private ProductName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (value.length() < 2) {
            throw new IllegalArgumentException("Product name must have at least 2 characters");
        }
        this.value = value.trim();
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
