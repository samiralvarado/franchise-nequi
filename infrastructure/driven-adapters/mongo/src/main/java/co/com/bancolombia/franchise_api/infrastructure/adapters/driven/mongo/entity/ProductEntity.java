package co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class ProductEntity {
    private String id;
    private String name;
    private Integer stock;
}
