package co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.mapper;


import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.BranchEntity;
import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.ProductEntity;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.values.BranchName;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.values.FranchiseName;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.values.ProductName;
import co.com.bancolombia.model.product.values.ProductStock;

public class MongoMapper {
    public static Franchise toDomain(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(FranchiseName.of(entity.getName()))
                .branches(entity.getBranches().stream()
                        .map(MongoMapper::branchToDomain)
                        .toList())
                .build();
    }

    public static FranchiseEntity toEntity(Franchise domain) {
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(domain.getName().getValue())
                .branches(domain.getBranches().stream()
                        .map(MongoMapper::branchToEntity)
                        .toList())
                .build();
    }

    private static Branch branchToDomain(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(BranchName.of(entity.getName()))
                .products(entity.getProducts().stream()
                        .map(MongoMapper::productToDomain)
                        .toList())
                .build();
    }

    private static BranchEntity branchToEntity(Branch branch) {
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName().getValue())
                .products(branch.getProducts().stream()
                        .map(MongoMapper::productToEntity)
                        .toList())
                .build();
    }

    private static Product productToDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(ProductName.of(entity.getName()))
                .stock(ProductStock.of(entity.getStock()))
                .build();
    }

    private static ProductEntity productToEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName().getValue())
                .stock(product.getStock().getValue())
                .build();
    }
}

