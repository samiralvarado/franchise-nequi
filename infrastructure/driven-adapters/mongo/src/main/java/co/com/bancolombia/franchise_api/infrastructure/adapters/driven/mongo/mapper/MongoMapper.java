package co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.mapper;


import co.com.bancolombia.franchise_api.infrastructure.adapters.driven.mongo.entity.FranchiseEntity;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.values.FranchiseName;

public class MongoMapper {
    public static Franchise toDomain(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(FranchiseName.of(entity.getName()))
                .branches(entity.getBranches())
                .build();
    }

    public static FranchiseEntity toEntity(Franchise domain) {
        return FranchiseEntity.builder()
                .id(domain.getId())
                .name(domain.getName().getValue())
                .branches(domain.getBranches())
                .build();
    }
}
