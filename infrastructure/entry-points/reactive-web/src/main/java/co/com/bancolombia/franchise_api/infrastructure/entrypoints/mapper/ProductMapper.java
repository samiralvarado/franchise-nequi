package co.com.bancolombia.franchise_api.infrastructure.entrypoints.mapper;


import co.com.bancolombia.franchise_api.infrastructure.entrypoints.dto.ProductRequest;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;

public class ProductMapper {

    public static Product toDomain(ProductRequest request) {
        return Product.builder()
                .name(ProductName.of(request.getName()))
                .stock(ProductStock.of(request.getStock()))
                .build();
    }

}
