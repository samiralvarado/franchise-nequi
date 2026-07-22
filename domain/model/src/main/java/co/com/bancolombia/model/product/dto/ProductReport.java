package co.com.bancolombia.model.product.dto;


import co.com.bancolombia.model.product.Product;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductReport {
    private String branchName;
    private Product product;
}
