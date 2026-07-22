package co.com.bancolombia.model.product.values;

import lombok.Value;

@Value
public class ProductStock {
    Integer value;

    private ProductStock(Integer value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Product stock must be zero or greater");
        }
        this.value = value;
    }

    public static ProductStock of(Integer value) {
        return new ProductStock(value);
    }
}
